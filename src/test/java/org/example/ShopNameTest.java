package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:shop-names",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class ShopNameTest {
    @Autowired MockMvc mvc;
    @Autowired ShopRepo shops;

    @ParameterizedTest
    @ValueSource(strings = {"Book Nook", "  bOOK nOOK  "})
    void apiRejectsTakenName(String duplicate) throws Exception {
        shops.deleteAll();
        shops.save(new Shop("Book Nook"));
        mvc.perform(post("/shops").sessionAttr("interfaceMode", "merchant")
                        .param("name", duplicate).param("description", "").param("tagsText", ""))
                .andExpect(status().isConflict());
        assertEquals("Book Nook", shops.findAll().get(0).getName());
        assertEquals(1, shops.count());
    }

    @Test
    void merchantCanCorrectTakenNameAndCreateShop() throws Exception {
        shops.deleteAll();
        shops.save(new Shop("Book Nook"));
        mvc.perform(post("/merchant/shops/create").sessionAttr("interfaceMode", "merchant")
                        .param("name", "Book Nook").param("description", "Used books").param("tagsText", "books"))
                .andExpect(view().name("create-shop"))
                .andExpect(model().attribute("errorMessage", "A shop with that name already exists."))
                .andExpect(model().attribute("description", "Used books"))
                .andExpect(model().attribute("tagsText", "books"));
        mvc.perform(post("/merchant/shops/create").sessionAttr("interfaceMode", "merchant")
                        .param("name", "Book Nook West"))
                .andExpect(redirectedUrl("/merchant"));
        assertEquals(2, shops.count());
    }
}
