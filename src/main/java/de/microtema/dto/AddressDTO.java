package de.microtema.dto;

import lombok.Data;

@Data
public class AddressDTO {

    private String street;

    private String city;

    private String zip;

    private String country;
}
