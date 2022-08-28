package com.gtss.mnp_manager.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Request Filter to support SPA (Single Page Application) routing
 * Browsers append header "Sec-Fetch-Mode=navigate" to indicate a navigation request
 */
@Component
public class ReactRoutingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String mode = request.getHeader("Sec-Fetch-Mode");

        if (mode != null && mode.equals("navigate")) {
            RequestDispatcher requestDispatcher =
                    request.getRequestDispatcher("/");
            requestDispatcher.forward(request, response);

        } else {
            filterChain.doFilter(request, response);
        }

    }

}
