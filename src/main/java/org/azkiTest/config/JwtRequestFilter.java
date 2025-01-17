package org.azkiTest.config;//package org.azkiTest.config;
//
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//
//@Component
//public class JwtRequestFilter extends OncePerRequestFilter {
//
//    private JwtTokenUtil jwtUtil;
//
//    public JwtRequestFilter(JwtTokenUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String header = request.getHeader("Authorization");
//        String username = null;
//        String token = null;
//
//        // Check for the Authorization header and validate it
//        if (header != null && header.startsWith("Bearer ")) {
//            try {
//                token = header.substring(7);  // Extract token
//                username = jwtUtil.extractUsername(token);  // Extract username from token
//            } catch (Exception e) {
//                // Log invalid token extraction
//                logger.error("Error extracting token: " + e.getMessage());
//                filterChain.doFilter(request, response);  // Continue filter chain
//                return;
//            }
//        }
//
//        // Proceed if the token and username are valid
//        if (username != null && token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            try {
//                if (jwtUtil.validateToken(token)) {
//                    UsernamePasswordAuthenticationToken authentication =
//                            new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
//                    SecurityContextHolder.getContext().setAuthentication(authentication);  // Set authentication context
//                }
//            } catch (Exception e) {
//                // Handle token validation failure
//                logger.error("Error validating token: " + e.getMessage());
//                filterChain.doFilter(request, response);  // Continue filter chain
//                return;
//            }
//        }
//
//        // Continue the filter chain
//        filterChain.doFilter(request, response);
//    }
//
//}
//
