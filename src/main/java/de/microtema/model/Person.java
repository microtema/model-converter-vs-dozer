package de.microtema.model;

import de.microtema.enums.GENRE;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class Person {

    @NotNull
    private Long personId;

    @NotNull
    private String personName;

    @NotNull
    private String lastName;

    @NotNull
    private Date dateOfBirth;

    @NotNull
    private GENRE genre;

    private String emailAddress;

    private boolean marriedStatus;

    @NotNull
    private List<Address> addressList;

    @NotNull
    private List<Person> children;
}
