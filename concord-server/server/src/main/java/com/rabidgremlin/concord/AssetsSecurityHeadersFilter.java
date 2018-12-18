package com.rabidgremlin.concord;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter to add recommended security headers to GET requests for content.
 */
public class AssetsSecurityHeadersFilter
    implements Filter
{
  private Logger log = LoggerFactory.getLogger(AssetsSecurityHeadersFilter.class);

  @Override
  public void init(FilterConfig filterConfig)
    throws ServletException
  {
    // do nothing
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException
  {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;

    if (httpServletRequest.getMethod().equals("GET"))
    {
      log.debug("Adding security headers to {}", httpServletRequest.getRequestURI());
      // swagger UI has inline script :(
      httpServletResponse.setHeader("Content-Security-Policy", "script-src 'self' 'unsafe-inline'");
      // swagger UI has inline script :(
      httpServletResponse.setHeader("X-Content-Security-Policy", "script-src 'self' 'unsafe-inline'");
      httpServletResponse.setHeader("X-Frame-Options", "DENY");
      httpServletResponse.setHeader("X-XSS-Protection", "1; mode=block");
      httpServletResponse.setHeader("X-Content-Type-Options", "nosniff");
    }

    chain.doFilter(request, response);
  }

  @Override
  public void destroy()
  {
    // do noting
  }

}
