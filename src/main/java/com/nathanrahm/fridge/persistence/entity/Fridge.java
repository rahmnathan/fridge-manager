package com.nathanrahm.fridge.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Fridge {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="fridge_sequence_generator")
    @SequenceGenerator(name="fridge_sequence_generator", sequenceName="FRIDGE_SEQUENCE")
    private Long id;
}
