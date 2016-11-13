package com.jobscheduler.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
    	final HttpServletRequest request = (HttpServletRequest) req;

        Cookie[] cookies = request.getCookies();
        int i;
        for(i = 0 ; i < cookies.length; i++) {
        	if(cookies[i].getName().equalsIgnoreCase("access_token"))
        		break;
        }
        final String token = cookies[i].getValue();
        if(token == null || token.isEmpty())
        	throw new ServletException("Missing or invalid token header.");
        
//        final String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new ServletException("Missing or invalid Authorization header.");
//        }
//
//        final String token = authHeader.substring(7); // The part after "Bearer "

        try {
            final Claims claims = Jwts.parser().setSigningKey("QEYFNSG@#$")
                .parseClaimsJws(token).getBody();
            request.setAttribute("claims", claims);
        }
        catch (final SignatureException e) {
            throw new ServletException("Invalid token.");
        }

        chain.doFilter(req, res);
    }

}
