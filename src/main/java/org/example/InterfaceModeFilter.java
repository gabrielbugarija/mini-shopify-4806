package org.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

/** Keeps the selected interface in the session and guards shop/product writes. */
@Component
public class InterfaceModeFilter extends OncePerRequestFilter {
    private static final String MODE = "interfaceMode";
    private static final Set<String> READ_METHODS = Set.of("GET", "HEAD", "OPTIONS");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("GET".equals(request.getMethod())) {
            if ("/merchant".equals(path)) {
                request.getSession().setAttribute(MODE, "merchant");
            } else if ("/".equals(path) || "/products/page".equals(path)
                    || path.matches("/products/[^/]+/page")) {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.removeAttribute(MODE);
                }
            }
        }

        boolean shopOrProduct = path.equals("/shops") || path.startsWith("/shops/")
                || path.equals("/products") || path.startsWith("/products/")
                || path.startsWith("/merchant/");
        HttpSession session = request.getSession(false);
        boolean merchant = session != null && "merchant".equals(session.getAttribute(MODE));
        if (shopOrProduct && !READ_METHODS.contains(request.getMethod()) && !merchant) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Switch to merchant mode to add or edit shops and products.");
            return;
        }
        chain.doFilter(request, response);
    }
}
