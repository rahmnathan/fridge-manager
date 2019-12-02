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
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class FridgeService {
    private final FridgeRepository repository;
    private final ServiceConfig config;

    public Fridge getFridgeById(String id) throws FridgeManagerException {
        return repository.findByFridgeId(id).map(com.nathanrahm.fridge.persistence.Fridge::toDTO)
                .orElseThrow(() -> new FridgeManagerException(FridgeManagerCode.FRIDGE_NOT_FOUND, "Fridge not found for ID."));
    }

    public Fridge getFridgeByName(String name) throws FridgeManagerException {
        return repository.findByName(name).map(com.nathanrahm.fridge.persistence.Fridge::toDTO)
                .orElseThrow(() -> new FridgeManagerException(FridgeManagerCode.FRIDGE_NOT_FOUND, "Fridge not found for ID."));
    }

    public List<Fridge> getFridges(Pageable pageable) {
        return repository.findAll(pageable).stream()
                .map(com.nathanrahm.fridge.persistence.Fridge::toDTO)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public Fridge storeFridge(FridgeRequest fridgeRequest) throws FridgeManagerException {
        validateItems(fridgeRequest.getItems());
        if(repository.existsByName(fridgeRequest.getName())){
            throw new FridgeManagerException(FridgeManagerCode.FRIDGE_EXISTS, "Fridge with name already exists.");
        }

        com.nathanrahm.fridge.persistence.Fridge fridgeEntity = com.nathanrahm.fridge.persistence.Fridge.fromFridgeRequest(fridgeRequest);
        String fridgeId = UUID.randomUUID().toString();
        fridgeEntity.setFridgeId(fridgeId);

        return repository.save(fridgeEntity).toDTO();
    }

    @Transactional
    public void deleteFridge(String id) {
        repository.deleteByFridgeId(id);
    }

    @Transactional
    public Fridge updateFridge(String id, FridgeRequest fridge) throws FridgeManagerException {
        validateItems(fridge.getItems());

        com.nathanrahm.fridge.persistence.Fridge fridgeEntity = repository.findByFridgeId(id)
                .orElseThrow(() -> new FridgeManagerException(FridgeManagerCode.FRIDGE_NOT_FOUND, "Requested fridge does not exist."));

        if(!StringUtils.isEmpty(fridge.getName()) && !fridge.getName().equals(fridgeEntity.getName())
                && repository.existsByName(fridge.getName())){
            throw new FridgeManagerException(FridgeManagerCode.FRIDGE_NAME_EXISTS, "Fridge with name already exists.");
        }

        fridgeEntity.mergeFridgeRequest(fridge);
        return repository.save(fridgeEntity).toDTO();
    }

    @Transactional
    public Fridge updateItems(String id, Map<String, Integer> items) throws FridgeManagerException {
        com.nathanrahm.fridge.persistence.Fridge fridgeEntity = repository.findByFridgeId(id)
                .orElseThrow(() -> new FridgeManagerException(FridgeManagerCode.FRIDGE_NOT_FOUND, "Requested fridge does not exist."));

        fridgeEntity.updateItems(items);
        validateItems(fridgeEntity.getItems());
        return repository.save(fridgeEntity).toDTO();
    }

    private void validateItems(Map<String, Integer> items) throws FridgeManagerException {
        if(items == null){
            return;
        }

        for(Map.Entry<String, Integer> requestItem : items.entrySet()){
            validateItem(requestItem);
        }
    }

    private void validateItem(Map.Entry<String, Integer> requestItem) throws FridgeManagerException {
        if(config.getFridgeItemMaximums().containsKey(requestItem.getKey())) {
            Integer configuredMaximum = config.getFridgeItemMaximums().get(requestItem.getKey());
            if(requestItem.getValue() > configuredMaximum) {
                throw new FridgeManagerException(FridgeManagerCode.ITEM_LIMIT_EXCEEDED, "Item limit exceeded for " + requestItem.getKey());
            }
        }
    }
}
