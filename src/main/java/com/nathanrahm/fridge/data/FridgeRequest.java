package com.nathanrahm.fridge.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class FridgeRequest {
    private final Map<String, Integer> items;
    private final String name;
}
