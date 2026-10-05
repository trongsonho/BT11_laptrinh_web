package vn.edu.hcmute.exam.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.webapp.DispatchMode;

import java.io.IOException;

public class SiteMeshFilter_24133049 extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        builder.setDecoratorPrefix("/WEB-INF/decorators/");
        builder.setDispatchMode(DispatchMode.INCLUDE);

        builder.addDecoratorPath("/admin/*", "admin.jsp");
        builder.addDecoratorPath("/*", "user.jsp");

        builder.addExcludedPath("/static/*");
        builder.addExcludedPath("/css/*");
        builder.addExcludedPath("/js/*");
        builder.addExcludedPath("/images/*");
        builder.addExcludedPath("*.css");
        builder.addExcludedPath("*.js");
        builder.addExcludedPath("*.png");
        builder.addExcludedPath("*.jpg");
        builder.addExcludedPath("*.jpeg");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        System.out.println("[SiteMeshFilter_24133049] START: " + req.getRequestURI());
        try {
            super.doFilter(request, response, chain);
        } catch (Throwable t) {
            System.err.println("[SiteMeshFilter_24133049] EXCEPTION in " + req.getRequestURI() + ": " + t.getMessage());
            t.printStackTrace();
            throw t;
        } finally {
            System.out.println("[SiteMeshFilter_24133049] END: " + req.getRequestURI());
        }
    }
}
