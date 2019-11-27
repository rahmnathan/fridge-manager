package com.nathanrahm.fridge.control;

import com.nathanrahm.fridge.config.ServiceConfig;
import com.nathanrahm.fridge.data.Fridge;
import com.nathanrahm.fridge.data.FridgeRequest;
import com.nathanrahm.fridge.exception.FridgeManagerCode;
import com.nathanrahm.fridge.exception.FridgeManagerException;
import com.nathanrahm.fridge.persistence.FridgeRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class FridgeService {
    private final FridgeRepository repository;
    private final ServiceConfig config;

    public Fridge getFridgeById(String id) throws FridgeManagerException {
        Optional<com.nathanrahm.fridge.persistence.Fridge> fridge = repository.findByFridgeId(id);
        if(fridge.isPresent()){
            return fridge.get().toDTO();
        }

        throw new FridgeManagerException(FridgeManagerCode.FRIDGE_NOT_FOUND, "Fridge not found for ID.");
    }

    public Fridge getFridgeByName(String name) throws FridgeManagerException {
        Optional<com.nathanrahm.fridge.persistence.Fridge> fridge = repository.findByName(name);
        if(fridge.isPresent()){
            return fridge.get().toDTO();
        }

        throw new FridgeManagerException(FridgeManagerCode.FRIDGE_NOT_FOUND, "Fridge not found for ID.");
    }

    public List<Fridge> getFridges(Pageable pageable) {
        return repository.findAll(pageable).stream()
                .map(com.nathanrahm.fridge.persistence.Fridge::toDTO)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public String storeFridge(FridgeRequest fridgeRequest) {
        com.nathanrahm.fridge.persistence.Fridge fridgeEntity = com.nathanrahm.fridge.persistence.Fridge.fromFridgeRequest(fridgeRequest);
        String fridgeId = UUID.randomUUID().toString();
        fridgeEntity.setFridgeId(fridgeId);

        repository.save(fridgeEntity);

        return fridgeId;
    }

    @Transactional
    public void deleteFridge(String id) {
        repository.deleteByFridgeId(id);
    }

    @Transactional
    public void updateFridge(String id, FridgeRequest fridge) throws FridgeManagerException {
        validateItems(fridge.getItems());

        Optional<com.nathanrahm.fridge.persistence.Fridge> fridgeOptional = repository.findByFridgeId(id);
        if(fridgeOptional.isEmpty()){
            throw new FridgeManagerException(FridgeManagerCode.FRIDGE_NOT_FOUND, "Requested fridge does not exist.");
        }

        com.nathanrahm.fridge.persistence.Fridge fridgeEntity = fridgeOptional.get();
        fridgeEntity.mergeFridgeRequest(fridge);
        repository.save(fridgeEntity);
    }

    private void validateItems(Map<String, Integer> items) throws FridgeManagerException {
        if(items == null){
            return;
        }

        for(Map.Entry<String, Integer> requestItem : items.entrySet()){
            if(config.getFridgeItemMaximums().containsKey(requestItem.getKey())) {
                Integer configuredMaximum = config.getFridgeItemMaximums().get(requestItem.getKey());
                if(requestItem.getValue() > configuredMaximum) {
                    throw new FridgeManagerException(FridgeManagerCode.ITEM_LIMIT_EXCEEDED, "Item limit exceeded for " + requestItem.getKey());
                }
            }
        }
    }
}
