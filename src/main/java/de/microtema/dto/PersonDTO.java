package de.microtema.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

@Data
public class PersonDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private Date dob;

    private String genreName;

    private String email;

    private boolean married;

    private String jobTitle;

    private String createdDate;

    private Collection<AddressDTO> addresses;

    private Collection<PersonDTO> children;
}
