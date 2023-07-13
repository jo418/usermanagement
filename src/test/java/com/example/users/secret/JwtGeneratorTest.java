package com.example.users.secret;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtGeneratorTest {

    private static final Integer PROVISIONER = 123;
    private static final String ORGANIZATION_NAME = "orgId";
    private JwtGenerator generator = new JwtGenerator();


    @Test
    void generateJwt() {
        String jwt = generator.generateJwt(PROVISIONER, 1);
        assertNotNull(jwt);
        System.out.println("Generated JWT: " + jwt);
        try {
            Jws<Claims> token = generator.parseAndValidateJwt(jwt);
            Integer orgId = (Integer) token.getBody().get(ORGANIZATION_NAME);
            assertEquals(1, orgId);
            Integer provId = (Integer)token.getBody().get("provId");
            assertEquals(123, provId);
            Date expiration = token.getBody().getExpiration();
            Date now = new Date();
            assertTrue(expiration.after(now));
        } catch (Exception e) {
            fail("failed to validate JWT token" + " " + e.getMessage());
        }
        assertTrue(true, "Generated and parsed/validated jwt token.");
    }
}