package com.fox.rssreader.auth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccessTokenAuthenticationFilter extends GenericFilterBean {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Token ";

    public AccessTokenAuthenticationFilter() {
        //super("/graphql/**");
    }

    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return false;
        //return super.requiresAuthentication(request, response);
    }

    public Boolean authenticate(ServletRequest request, ServletResponse response) throws AuthenticationException {
        return false;
//        String header = ((HttpServletRequest)request).getHeader(AUTHORIZATION_HEADER);
//        Authentication authentication =
//                UsernamePasswordAuthenticationToken.unauthenticated("user","doesnotmatter");
//        if (header == null || !header.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
//            return authentication;
//        }
//
//        String authenticationToken = header.substring(AUTHORIZATION_HEADER_PREFIX.length()).strip();
//
//        AccessTokenAuthenticationToken token = new AccessTokenAuthenticationToken();
//        return token;
        //todo
        // return getAuthenticationManager().authenticate(token);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (authenticate(request, response))
            return;
        chain.doFilter(request, response);
    }
}
