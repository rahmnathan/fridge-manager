package com.nathanrahm.fridge.web;

import com.nathanrahm.fridge.data.FridgeRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FridgeControllerTest {
    private final TestRestTemplate template;

    @LocalServerPort
    private int port;

    @Autowired
    public FridgeControllerTest(TestRestTemplate template) {
        this.template = template;
    }

    @Test
    public void storeFridgeTest() {
        URI location = template.postForLocation("http://localhost:" + port + PathConstants.V1_ROOT, new FridgeRequest("Name"), new HashMap<>());
        assertNotNull(location);
        assertTrue(location.toString().matches(PathConstants.V1_ROOT + "/(.*)"));
    }
}
