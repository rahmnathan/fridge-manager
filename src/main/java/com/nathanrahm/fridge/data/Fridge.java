package com.nathanrahm.fridge.data;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class Fridge {
    private final LocalDateTime created;
    private final LocalDateTime updated;
    private final String name;
    private final String id;
}
