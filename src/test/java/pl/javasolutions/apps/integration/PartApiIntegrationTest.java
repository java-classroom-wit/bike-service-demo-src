package pl.javasolutions.apps.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import pl.javasolutions.apps.dto.PartDTO;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PartApiIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void fullCrudFlow() {
        // 1. POST – create
        PartDTO newPart = new PartDTO("Klocki testowe", "Shimano", new BigDecimal("45.00"));

        ResponseEntity<String> createResponse = restTemplate.postForEntity(
                "/api/parts", newPart, String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getHeaders().getLocation()).isNotNull();

        String location = createResponse.getHeaders().getLocation().getPath();

        // 2. GET – read created
        ResponseEntity<String> getResponse = restTemplate.getForEntity(location, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).contains("Klocki testowe");

        // 3. PUT – update
        PartDTO updated = new PartDTO("Klocki PRO", "Shimano", new BigDecimal("69.99"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PartDTO> updateEntity = new HttpEntity<>(updated, headers);

        ResponseEntity<String> putResponse = restTemplate.exchange(
                location, HttpMethod.PUT, updateEntity, String.class);
        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(putResponse.getBody()).contains("Klocki PRO");

        // 4. DELETE
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                location, HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // 5. GET – verify deleted
        ResponseEntity<String> notFoundResponse = restTemplate.getForEntity(
                location, String.class);
        assertThat(notFoundResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}

