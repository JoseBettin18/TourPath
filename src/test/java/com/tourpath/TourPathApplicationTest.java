package com.tourpath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("TourPathApplication - Context Load Test")
class TourPathApplicationTest {

    @Test
    @DisplayName("El contexto de Spring arranca correctamente")
    void contextLoads() {
        // Si no lanza excepción, el contexto está bien configurado
    }
}
