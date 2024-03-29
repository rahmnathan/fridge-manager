package com.nathanrahm.fridge.persistence;

import com.nathanrahm.fridge.data.FridgeRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fridges", indexes = {
        @Index(name = "idx_fridge_name", columnList = "name", unique = true),
        @Index(name = "idx_fridge_id", columnList = "fridgeId", unique = true)
})
public class Fridge {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="fridge_sequence_generator")
    @SequenceGenerator(name="fridge_sequence_generator", sequenceName="FRIDGE_SEQUENCE")
    private Long id;
    private LocalDateTime created;
    private LocalDateTime updated;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Integer> items;
    private String fridgeId;
    private String name;

    @PrePersist
    public void setCreated(){
        created = LocalDateTime.now();
        updated = created;
    }

    @PreUpdate
    public void setUpdated(){
        updated = LocalDateTime.now();
    }

    public com.nathanrahm.fridge.data.Fridge toDTO() {
        return com.nathanrahm.fridge.data.Fridge.builder()
                .created(this.created)
                .updated(this.updated)
                .name(this.name)
                .items(this.items)
                .id(this.fridgeId)
                .build();
    }

    public static Fridge fromFridgeRequest(FridgeRequest fridge) {
        return builder()
                .name(fridge.getName())
                .items(fridge.getItems())
                .build();
    }

    public void mergeFridgeRequest(FridgeRequest fridge){
        this.items = fridge.getItems();
        this.name = fridge.getName();
    }

    public void updateItems(Map<String, Integer> inputItems) {
        if (this.items == null) {
            this.items = new HashMap<>();
        }

        for (Map.Entry<String, Integer> inputItemEntry : inputItems.entrySet()) {
            String key = inputItemEntry.getKey();
            Integer value = items.getOrDefault(key, 0) + inputItemEntry.getValue();
            if (value <= 0) {
                items.remove(key);
            } else {
                items.put(key, value);
            }
        }
    }
}
