package vn.edu.hcmute.exam.filter;

import jakarta.servlet.*;

import java.io.IOException;

public class EncodingFilter_24133049 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }
}
