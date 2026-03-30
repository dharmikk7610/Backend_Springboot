package com.Util;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Service.CustomUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class Jwtfilter extends OncePerRequestFilter {
	

	    private final Jwtutils jwtUtil;
	    private final CustomUserDetailsService userDetailsService;

	    public Jwtfilter(Jwtutils jwtUtil, CustomUserDetailsService userDetailsService) {
	        this.jwtUtil = jwtUtil;
	        this.userDetailsService = userDetailsService;
	    }

	    @Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
	                                    FilterChain filterChain) throws ServletException, IOException {

	        String path = request.getRequestURI();

	        //  Allow login and signup without JWT
	        if (path.startsWith("/public/") ) {
	            filterChain.doFilter(request, response);
	            return;
	        }

	        String authHeader = request.getHeader("Authorization");
	        String token = null;
	        String username = null;
	        String role = null ;

	        //  Token Extract
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            token = authHeader.substring(7);

	            try {
	            	System.out.println("mytoken-->"+token);
	                username = jwtUtil.extractUsername(token);
	                System.out.println("username--->"+username);
	                role = jwtUtil.extractRole(token);
	                System.out.println("Role => "+role);
	            } catch (ExpiredJwtException e) {
	                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                response.getWriter().write("Token expired!");
	                System.out.println("auth filter 61");
	                return;
	            } catch (Exception e) {
	                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                response.getWriter().write("Invalid token!");
	                System.out.println("auth filter 61");

	                return;
	            }
	        } else {
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.getWriter().write("Please login first (Token missing)");
	            System.out.println("auth filter 73");

	            return;
	        }

	        //  Authenticate user
	        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	        	System.out.println("in jwtfilwe service ...>");
	            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

	            if (jwtUtil.validateToken(token)) {

	                UsernamePasswordAuthenticationToken authToken =
	                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

	                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

	                SecurityContextHolder.getContext().setAuthentication(authToken);
	            }
	        }

	        filterChain.doFilter(request, response);
	    }
	}
