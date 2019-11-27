package com.nathanrahm.fridge.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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
                .id(this.fridgeId)
                .build();
    }

    public static Fridge fromDTO(com.nathanrahm.fridge.data.Fridge fridge) {
        return builder()
                .created(fridge.getCreated())
                .updated(fridge.getUpdated())
                .name(fridge.getName())
                .fridgeId(fridge.getId())
                .build();
    }
}
