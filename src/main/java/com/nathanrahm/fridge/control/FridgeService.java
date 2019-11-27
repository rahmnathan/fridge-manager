package com.nathanrahm.fridge.control;

import com.nathanrahm.fridge.data.Fridge;
import com.nathanrahm.fridge.exception.FridgeManagerCode;
import com.nathanrahm.fridge.exception.FridgeManagerException;
import com.nathanrahm.fridge.persistence.FridgeRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FridgeService {
    private final FridgeRepository repository;

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

    public String storeFridge(Fridge fridge) {
        com.nathanrahm.fridge.persistence.Fridge fridgeEntity = com.nathanrahm.fridge.persistence.Fridge.fromDTO(fridge);
        String fridgeId = UUID.randomUUID().toString();
        fridgeEntity.setFridgeId(fridgeId);

        repository.save(fridgeEntity);

        return fridgeId;
    }
}
