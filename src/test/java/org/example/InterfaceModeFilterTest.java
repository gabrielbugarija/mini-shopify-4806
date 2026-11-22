package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.*;

class InterfaceModeFilterTest {
    private final InterfaceModeFilter filter = new InterfaceModeFilter();

    private MockHttpServletResponse visit(String method, String path, MockHttpSession session)
            throws Exception {
        
        MockHttpServletRequest request = new MockHttpServletRequest(method, path);
        request.setServletPath(path);
        
        if (session != null) request.setSession(session);
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilter(request, response, new MockFilterChain());
            return response;
    }

    @Test
    void shopperLimits() throws Exception {
        for (String method : new String[]{"POST", "PUT", "PATCH", "DELETE"}) {
            for (String path : new String[]{"/shops", "/shops/1", "/products", "/products/1"}) {
                assertEquals(403, visit(method, path, null).getStatus());
            }
        }
        assertEquals(200, visit("GET", "/products", null).getStatus());
    }

    @Test
    void merchentAcsess() throws Exception {
        MockHttpSession session = new MockHttpSession();
        visit("GET", "/merchant", session);
        assertEquals(200, visit("POST", "/shops", session).getStatus());
        assertEquals(200, visit("PUT", "/products/1", session).getStatus());
        assertEquals(403, visit("POST", "/products", new MockHttpSession()).getStatus());
        visit("GET", "/", session);
        assertEquals(403, visit("POST", "/shops", session).getStatus());
    }
}
