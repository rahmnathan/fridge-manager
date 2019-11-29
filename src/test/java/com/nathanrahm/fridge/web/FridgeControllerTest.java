package com.nathanrahm.fridge.web;

import com.nathanrahm.fridge.data.Fridge;
import com.nathanrahm.fridge.data.FridgeRequest;
import com.nathanrahm.fridge.exception.FridgeManagerCode;
import com.nathanrahm.fridge.exception.FridgeManagerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FridgeControllerTest {
    private final TestRestTemplate template;
    private String baseUrl = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    public FridgeControllerTest(TestRestTemplate template) {
        this.template = template;
    }

    @Test
    public void storeFridgeTest() {
        URI location = template.postForLocation(baseUrl + port + PathConstants.V1_ROOT, new FridgeRequest(new HashMap<>(), UUID.randomUUID().toString()), new HashMap<>());
        assertTrue(location.toString().matches(PathConstants.V1_ROOT + "/(.*)"));
    }

    @Test
    public void getFridgeByIdTest() {
        String fridgeName = UUID.randomUUID().toString();
        URI location = template.postForLocation(baseUrl + port + PathConstants.V1_ROOT, new FridgeRequest(new HashMap<>(), fridgeName), new HashMap<>());

        Fridge fridge = template.getForObject(baseUrl + port + location.toString(), Fridge.class);
        assertEquals(fridgeName, fridge.getName());
    }

    @Test
    public void getFridgeByNameTest() {
        String fridgeName = UUID.randomUUID().toString();
        template.postForLocation(baseUrl + port + PathConstants.V1_ROOT, new FridgeRequest(new HashMap<>(), fridgeName), new HashMap<>());
        Fridge fridge = template.getForObject(baseUrl + port + PathConstants.V1_NAME + "/" + fridgeName, Fridge.class);
        assertEquals(fridgeName, fridge.getName());
    }

    @Test
    public void updateFridgeTest() {
        String fridgeName = UUID.randomUUID().toString();
        URI location = template.postForLocation(baseUrl + port + PathConstants.V1_ROOT, new FridgeRequest(new HashMap<>(), fridgeName), new HashMap<>());

        String newFridgeName = UUID.randomUUID().toString();
        String itemName = "soda";
        Integer itemCount = 4;

        Fridge fridge = template.patchForObject(baseUrl + port + location.toString(), new FridgeRequest(Map.of(itemName, itemCount), newFridgeName), Fridge.class, new HashMap<>());

        assertEquals(newFridgeName, fridge.getName());
        assertEquals(itemCount, fridge.getItems().get(itemName));
    }

    @Test
    public void deleteFridgeTest() {
        String fridgeName = UUID.randomUUID().toString();
        URI location = template.postForLocation(baseUrl + port + PathConstants.V1_ROOT, new FridgeRequest(new HashMap<>(), fridgeName), new HashMap<>());

        template.delete(baseUrl + port + location.toString());
        ResponseEntity<FridgeManagerResponse> fridgeResponse = template.getForEntity(baseUrl + port + location.toString(), FridgeManagerResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, fridgeResponse.getStatusCode());
        assertEquals(FridgeManagerCode.FRIDGE_NOT_FOUND, fridgeResponse.getBody().getCode());
    }

    @Test
    public void updateFridgeItemsTest() {
        String fridgeName = UUID.randomUUID().toString();
        URI location = template.postForLocation(baseUrl + port + PathConstants.V1_ROOT, new FridgeRequest(new HashMap<>(), fridgeName), new HashMap<>());

        String itemName = "soda";
        Integer itemCount = 4;

        Fridge fridge = template.patchForObject(baseUrl + port + location.toString() + "/items", Map.of(itemName, itemCount), Fridge.class, new HashMap<>());

        assertEquals(itemCount, fridge.getItems().get(itemName));
    }

    @Test
    public void getFridgesTest() {
        for(int i = 0; i < 10; i++){
            FridgeRequest fridgeRequest = new FridgeRequest(Collections.emptyMap(), UUID.randomUUID().toString());
            template.postForLocation(baseUrl + port + PathConstants.V1_ROOT, fridgeRequest, Collections.emptyMap());
        }

        String requestParams = String.format("?page=%d&size=%d", 0, 3);
        Collection<Fridge> fridges = template.getForObject(baseUrl + port + PathConstants.V1_ROOT + requestParams, Collection.class, Collections.emptyMap());

        assertEquals(3, fridges.size());
    }
}
