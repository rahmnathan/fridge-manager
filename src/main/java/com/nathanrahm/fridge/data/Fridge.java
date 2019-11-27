package com.nathanrahm.fridge.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@EqualsAndHashCode(callSuper = true)
public class Fridge extends FridgeRequest {
    private final LocalDateTime created;
    private final LocalDateTime updated;
    private final String id;

    @Builder
    public Fridge(String name, LocalDateTime created, LocalDateTime updated, String id) {
        super(name);
        this.created = created;
        this.updated = updated;
        this.id = id;
    }
}
