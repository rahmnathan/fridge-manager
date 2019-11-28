package com.nathanrahm.fridge.control;

import com.nathanrahm.fridge.data.Fridge;
import com.nathanrahm.fridge.data.FridgeRequest;
import com.nathanrahm.fridge.exception.FridgeManagerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class FridgeServiceTest {
    private final FridgeService fridgeService;

    @Autowired
    public FridgeServiceTest(FridgeService fridgeService) {
        this.fridgeService = fridgeService;
    }

    @Test
    public void storeFridgeTest() throws FridgeManagerException {
        String fridgeName = UUID.randomUUID().toString();
        Fridge fridge = Fridge.builder()
                .name(fridgeName)
                .build();

        String id = fridgeService.storeFridge(fridge);
        Fridge resultFridge = fridgeService.getFridgeById(id);

        assertNotNull(resultFridge);
        assertEquals(fridgeName, resultFridge.getName());
    }

    @Test
    public void getFridgeNotFoundTest() {
        assertThrows(FridgeManagerException.class, () -> fridgeService.getFridgeById(UUID.randomUUID().toString()));
    }

    @Test
    public void updateFridgeTest() throws FridgeManagerException {
        String fridgeName = UUID.randomUUID().toString();

        Fridge.FridgeBuilder fridgeBuilder = Fridge.builder()
                .name(fridgeName);

        String fridgeId = fridgeService.storeFridge(fridgeBuilder.build());

        String newName = UUID.randomUUID().toString();
        Fridge updatedFridge = fridgeBuilder.id(fridgeId)
                .name(newName)
                .build();

        fridgeService.updateFridge(fridgeId, updatedFridge);

        Fridge resultFridge = fridgeService.getFridgeById(fridgeId);
        assertEquals(newName, resultFridge.getName());
    }

    @Test
    public void deleteFridgeTest() throws FridgeManagerException {
        String id = fridgeService.storeFridge(new FridgeRequest(new HashMap<>(), UUID.randomUUID().toString()));

        fridgeService.deleteFridge(id);

        assertThrows(FridgeManagerException.class, () -> fridgeService.getFridgeById(id));
    }

    @Test
    public void getFridgeByNameTest() throws FridgeManagerException {
        String name = UUID.randomUUID().toString();

        fridgeService.storeFridge(new FridgeRequest(new HashMap<>(), name));
        Fridge fridge = fridgeService.getFridgeByName(name);

        assertNotNull(fridge);
        assertEquals(name, fridge.getName());
    }

    @Test
    public void getFridgesTest() throws FridgeManagerException {
        fridgeService.storeFridge(new FridgeRequest(new HashMap<>(), UUID.randomUUID().toString()));

        List<Fridge> fridges = fridgeService.getFridges(Pageable.unpaged());
        assertTrue(fridges.size() >= 1);
    }
}
