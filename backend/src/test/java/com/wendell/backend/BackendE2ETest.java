package com.wendell.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackendE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnTokenWhenLoginIsSuccessful() throws Exception {
        ResponseEntity<String> response = login("admin", "123456");
        JsonNode body = objectMapper.readTree(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body.get("id"));
        assertEquals("admin", body.get("username").asText());
        assertFalse(body.get("token").asText().isBlank());
    }

    @Test
    void shouldReturn401WhenProtectedEndpointIsCalledWithoutToken() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl() + "/grades?classroomId=1&disciplineId=1",
                String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldExecuteBasicListingFlow() throws Exception {
        AuthContext auth = authenticate("admin", "123456");

        ResponseEntity<String> classroomsResponse = restTemplate.exchange(
                baseUrl() + "/classrooms/user?userId=" + auth.userId(),
                HttpMethod.GET,
                new HttpEntity<>(withAuth(auth.token())),
                String.class);

        assertEquals(HttpStatus.OK, classroomsResponse.getStatusCode());
        JsonNode classrooms = objectMapper.readTree(classroomsResponse.getBody());
        assertTrue(classrooms.isArray());
        assertTrue(classrooms.size() > 0);

        Long classroomId = classrooms.get(0).get("id").asLong();

        ResponseEntity<String> disciplinesResponse = restTemplate.exchange(
                baseUrl() + "/classrooms/disciplines?userId=" + auth.userId() + "&classroomId=" + classroomId,
                HttpMethod.GET,
                new HttpEntity<>(withAuth(auth.token())),
                String.class);

        assertEquals(HttpStatus.OK, disciplinesResponse.getStatusCode());
        JsonNode disciplines = objectMapper.readTree(disciplinesResponse.getBody());
        assertTrue(disciplines.isArray());
        assertTrue(disciplines.size() > 0);

        Long disciplineId = disciplines.get(0).get("id").asLong();

        ResponseEntity<String> gradesResponse = restTemplate.exchange(
                baseUrl() + "/grades?classroomId=" + classroomId + "&disciplineId=" + disciplineId,
                HttpMethod.GET,
                new HttpEntity<>(withAuth(auth.token())),
                String.class);

        assertEquals(HttpStatus.OK, gradesResponse.getStatusCode());
        JsonNode grades = objectMapper.readTree(gradesResponse.getBody());
        assertTrue(grades.isArray());
        assertTrue(grades.size() > 0);
    }

    @Test
    void shouldUpdateGradeAndCreateAuditRecord() throws Exception {
        AuthContext auth = authenticate("admin", "123456");

        Long classroomId = firstClassroomId(auth);
        Long disciplineId = firstDisciplineId(auth, classroomId);

        ResponseEntity<String> gradesResponse = restTemplate.exchange(
                baseUrl() + "/grades?classroomId=" + classroomId + "&disciplineId=" + disciplineId,
                HttpMethod.GET,
                new HttpEntity<>(withAuth(auth.token())),
                String.class);

        JsonNode grades = objectMapper.readTree(gradesResponse.getBody());
        JsonNode firstStudent = grades.get(0);
        JsonNode firstEvaluation = firstStudent.get("evaluations").get(0);

        Long gradeId = firstEvaluation.get("gradeId").asLong();
        Long studentId = firstStudent.get("studentId").asLong();
        Long evaluationId = firstEvaluation.get("evaluationId").asLong();
        BigDecimal currentValue = firstEvaluation.get("gradeValue").decimalValue();
        BigDecimal updatedValue = nextValue(currentValue);

        Map<String, Object> item = new HashMap<>();
        item.put("gradeId", gradeId);
        item.put("studentId", studentId);
        item.put("evaluationId", evaluationId);
        item.put("gradeValue", updatedValue);

        Map<String, Object> body = new HashMap<>();
        body.put("modifiedByUserId", auth.userId());
        body.put("grades", List.of(item));

        ResponseEntity<Void> updateResponse = restTemplate.exchange(
                baseUrl() + "/grades",
                HttpMethod.PUT,
                new HttpEntity<>(body, withAuth(auth.token())),
                Void.class);

        assertEquals(HttpStatus.NO_CONTENT, updateResponse.getStatusCode());

        ResponseEntity<String> auditResponse = restTemplate.exchange(
                baseUrl() + "/grades/audit?studentId=" + studentId + "&disciplineId=" + disciplineId,
                HttpMethod.GET,
                new HttpEntity<>(withAuth(auth.token())),
                String.class);

        assertEquals(HttpStatus.OK, auditResponse.getStatusCode());
        JsonNode auditRows = objectMapper.readTree(auditResponse.getBody());
        assertTrue(auditRows.isArray());

        boolean found = false;
        for (JsonNode row : auditRows) {
            BigDecimal newValue = row.get("newValue").decimalValue();
            if (row.get("evaluationId").asLong() == evaluationId
                    && newValue.compareTo(updatedValue) == 0
                    && auth.username().equals(row.get("modifiedByUsername").asText())) {
                found = true;
                break;
            }
        }

        assertTrue(found, "Audit record for the grade update was not found");
    }

    @Test
    void shouldReturn401WhenUserAccessesClassroomNotAssignedToUser() throws Exception {
        AuthContext auth = authenticate("joao", "123456");

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/classrooms/disciplines?userId=" + auth.userId() + "&classroomId=5",
                HttpMethod.GET,
                new HttpEntity<>(withAuth(auth.token())),
                String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldReturn401WhenUserAccessesDisciplineNotAssignedToUser() throws Exception {
        AuthContext auth = authenticate("joao", "123456");

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/grades?classroomId=1&disciplineId=5",
                HttpMethod.GET,
                new HttpEntity<>(withAuth(auth.token())),
                String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    private AuthContext authenticate(String username, String password) throws Exception {
        ResponseEntity<String> response = login(username, password);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JsonNode body = objectMapper.readTree(response.getBody());
        return new AuthContext(body.get("id").asLong(), body.get("username").asText(), body.get("token").asText());
    }

    private ResponseEntity<String> login(String username, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return restTemplate.postForEntity(baseUrl() + "/user/login", new HttpEntity<>(body, headers), String.class);
    }

    private Long firstClassroomId(AuthContext auth) throws Exception {
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/classrooms/user?userId=" + auth.userId(),
                HttpMethod.GET,
                new HttpEntity<>(withAuth(auth.token())),
                String.class);

        JsonNode classrooms = objectMapper.readTree(response.getBody());
        return classrooms.get(0).get("id").asLong();
    }

    private Long firstDisciplineId(AuthContext auth, Long classroomId) throws Exception {
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/classrooms/disciplines?userId=" + auth.userId() + "&classroomId=" + classroomId,
                HttpMethod.GET,
                new HttpEntity<>(withAuth(auth.token())),
                String.class);

        JsonNode disciplines = objectMapper.readTree(response.getBody());
        return disciplines.get(0).get("id").asLong();
    }

    private BigDecimal nextValue(BigDecimal currentValue) {
        BigDecimal increased = currentValue.add(new BigDecimal("0.10"));
        if (increased.compareTo(BigDecimal.TEN) <= 0) {
            return increased;
        }
        return currentValue.subtract(new BigDecimal("0.10"));
    }

    private HttpHeaders withAuth(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private record AuthContext(Long userId, String username, String token) {
    }
}
