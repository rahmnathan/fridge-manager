package com.nathanrahm.fridge.web;

import com.nathanrahm.fridge.control.FridgeService;
import com.nathanrahm.fridge.data.Fridge;
import com.nathanrahm.fridge.exception.FridgeManagerException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.nathanrahm.fridge.web.PathConstants.*;

@RestController
@AllArgsConstructor
public class FridgeController {
    private final Logger logger = LoggerFactory.getLogger(FridgeController.class);
    private final FridgeService fridgeService;

    @GetMapping(value = V1_ROOT + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Fridge getFridgeById(@PathVariable("id") String id) throws FridgeManagerException {
        logger.info("Request received for fridge with id: {}", id);

        Fridge fridge = fridgeService.getFridgeById(id);

        logger.info("Successfully processed request for fridge by id.");
        return fridge;
    }

    @GetMapping(value = V1_NAME + "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Fridge getFridgeByName(@PathVariable("name") String name) throws FridgeManagerException {
        logger.info("Request received for fridge with name: {}", name);

        Fridge fridge = fridgeService.getFridgeByName(name);

        logger.info("Successfully processed request for fridge by name.");
        return fridge;
    }

    @GetMapping(value = V1_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Fridge> getFridges(Pageable pageable) {
        logger.info("Request received for fridge list.");
        logger.debug("Paged request: {}", pageable);

        List<Fridge> fridges = fridgeService.getFridges(pageable);

        logger.info("Successfully processed request for fridge list.");
        return fridges;
    }

    @PutMapping(value = V1_ROOT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity storeFridge(@RequestBody Fridge fridge) {
        logger.info("Request received to store fridge.");
        logger.debug("Request body: {}", fridge);

        String id = fridgeService.storeFridge(fridge);

        logger.info("Successfully processed request for fridge list.");
        return ResponseEntity.created(URI.create(V1_ROOT + "/" + id)).build();
    }
}
