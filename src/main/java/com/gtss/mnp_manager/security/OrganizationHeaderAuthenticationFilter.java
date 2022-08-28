package com.gtss.mnp_manager.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gtss.mnp_manager.utils.OrganizationHeaderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

/** 
 * Authorize Mobile Number Porting requests using "organization" header
*/

@Component
public class OrganizationHeaderAuthenticationFilter extends GenericFilterBean {

    @Autowired
    private OrganizationHeaderValidator organizationHeaderValidator;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        if (!path.startsWith("/api/v1/mnp")) {
            chain.doFilter(request, response);
            return;
        }

        String organizationHeader = httpRequest.getHeader("organization");

        if (!organizationHeaderValidator
                .isHeaderExists(organizationHeader)) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Organization header not specified.");
            return;
        }
        if (!organizationHeaderValidator.validate(organizationHeader)) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Organization header not valid.");
            return;
        }

        chain.doFilter(request, response);
    }

}
