package org.example.bike_rent.entity.bike;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BrandTest {
    @Test
    void testNoArgsConstructorAndSetters() {
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("Trek");
        assertEquals(1L, brand.getId());
        assertEquals("Trek", brand.getName());
    }

    @Test
    void testAllArgsConstructor() {
        Brand brand = new Brand(2L, "Specialized");
        assertEquals(2L, brand.getId());
        assertEquals("Specialized", brand.getName());
    }
}

