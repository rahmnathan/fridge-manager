package com.nathanrahm.fridge.web;

import com.nathanrahm.fridge.control.FridgeService;
import com.nathanrahm.fridge.data.Fridge;
import com.nathanrahm.fridge.data.FridgeRequest;
import com.nathanrahm.fridge.exception.FridgeManagerException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.nathanrahm.fridge.web.PathConstants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
public class FridgeController {
    private final Logger logger = LoggerFactory.getLogger(FridgeController.class);
    private final FridgeService fridgeService;

    @GetMapping(value = V1_ROOT + "/{id}", produces = APPLICATION_JSON_VALUE)
    public Fridge getFridgeById(@PathVariable("id") String id) throws FridgeManagerException {
        logger.info("Request received for fridge with id: {}", id);

        Fridge fridge = fridgeService.getFridgeById(id);

        logger.info("Successfully processed request for fridge by id.");
        return fridge;
    }

    @GetMapping(value = V1_NAME + "/{name}", produces = APPLICATION_JSON_VALUE)
    public Fridge getFridgeByName(@PathVariable("name") String name) throws FridgeManagerException {
        logger.info("Request received for fridge with name: {}", name);

        Fridge fridge = fridgeService.getFridgeByName(name);

        logger.info("Successfully processed request for fridge by name.");
        return fridge;
    }

    @GetMapping(value = V1_ROOT, produces = APPLICATION_JSON_VALUE)
    public List<Fridge> getFridges(Pageable pageable) {
        logger.info("Request received for fridge list.");
        logger.debug("Paged request: {}", pageable);

        List<Fridge> fridges = fridgeService.getFridges(pageable);

        logger.info("Successfully processed request for fridge list. Returning list of size: {}.", fridges.size());
        return fridges;
    }

    @PostMapping(value = V1_ROOT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Fridge> storeFridge(@RequestBody FridgeRequest fridgeRequest) throws FridgeManagerException {
        logger.info("Request received to store fridge.");
        logger.debug("Request body: {}", fridgeRequest);

        Fridge fridge = fridgeService.storeFridge(fridgeRequest);

        logger.info("Successfully processed request to store fridge.");
        logger.debug("Response body: {}", fridge);
        return ResponseEntity.created(URI.create(V1_ROOT + "/" + fridge.getId())).body(fridge);
    }

    @PatchMapping(value = V1_ROOT + "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Fridge> updateFridge(@RequestBody FridgeRequest fridgeRequest, @PathVariable("id") String id) throws FridgeManagerException {
        logger.info("Request received to update fridge with id: {}.", id);
        logger.debug("Request body: {}", fridgeRequest);

        Fridge fridge = fridgeService.updateFridge(id, fridgeRequest);

        logger.info("Successfully processed request for fridge list.");
        logger.debug("Response body: {}", fridge);
        return ResponseEntity.created(URI.create(V1_ROOT + "/" + id)).body(fridge);
    }

    @DeleteMapping(value = V1_ROOT + "/{id}")
    public void deleteFridge(@PathVariable("id") String id) {
        logger.info("Request received to delete fridge with id: {}.", id);

        fridgeService.deleteFridge(id);

        logger.info("Successfully processed request to delete fridge.");
    }

    @PatchMapping(value = V1_ROOT + "/{id}/items", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Fridge updateItems(@RequestBody Map<String, Integer> items, @PathVariable("id") String id) throws FridgeManagerException {
        logger.info("Request received to update items in fridge with id: {}.", id);
        logger.debug("Request body: {}", items);

        Fridge fridge = fridgeService.updateItems(id, items);

        logger.info("Successfully processed request to update items in fridge.");
        logger.debug("Response body: {}", fridge);
        return fridge;
    }
}
