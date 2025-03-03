package com.sanish.url.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@NoArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtUtils jwtTokenProvider;

    private UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try{
            //Getting JWT Token from HTTP Request Header
            String jwtToken = jwtTokenProvider.getJwtFromHeader(request);

            //Validate Token, extract username and get corresponding User details
            if(jwtToken != null && jwtTokenProvider.validateToken(jwtToken)){
                String username = jwtTokenProvider.getUsernameFromJwtToken(jwtToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if(userDetails != null){
                    //Update security context details only when user details are successfully fetched from userDetailsService
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //Update Security Context
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        filterChain.doFilter(request, response); //Forward the request to next element in filter chain
    }
}
