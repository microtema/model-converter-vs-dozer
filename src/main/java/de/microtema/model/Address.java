package de.microtema.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Address {

    @NotNull
    private String street;

    private String city;

    private String zip;

    private String country;
}
