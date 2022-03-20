package de.microtema.mapper;

import de.microtema.dto.PersonDTO;
import de.microtema.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Mapper(uses = {Address2AddressDTOMapper.class, Enum2StringMapper.class}, imports = {ZonedDateTime.class, ZoneId.class, DateTimeFormatter.class})
public interface Person2PersonDTOMapper {

    @Mappings({
            // field mapping
            @Mapping(target = "id", source = "person.personId"), // error prune of code refactoring
            @Mapping(target = "email", source = "person.emailAddress"),
            @Mapping(target = "dob", source = "person.dateOfBirth"),
            @Mapping(target = "firstName", source = "person.personName"),
            @Mapping(target = "married", source = "person.marriedStatus"),
            @Mapping(target = "genreName", source = "person.genre"),

            // qualifier
            @Mapping(target = "children", source = "parent", qualifiedByName = "mapParentChildren"), // default value is ignored on null parent
            @Mapping(target = "lastName", source = "parent", qualifiedByName = "mapParentName"), // default value is ignored on null parent

            // default values
            @Mapping(target = "jobTitle", ignore = true), // explicit declaring

            // expression
            @Mapping(target = "createdDate", expression = "java(ZonedDateTime.now(ZoneId.of(\"CET\")).format(DateTimeFormatter.ISO_DATE_TIME))") // complex expression
    })
    PersonDTO convert(Person person, Person parent);

    List<PersonDTO> convertToList(Collection<Person> persons); // return null instead of empty List on null persons

    default List<PersonDTO> convertToList(Collection<Person> persons, Person parent) {

        List<PersonDTO> personDTOS = convertToList(persons);

        personDTOS.forEach(it -> it.setLastName(mapParentName(parent)));
        personDTOS.forEach(it -> it.getChildren().forEach(c -> c.setLastName(it.getLastName())));

        return personDTOS;
    }

    @Named("personName")
    default String mapParentName(Person person) {

        if (person == null) {
            return "unknown";
        }

        return person.getLastName();
    }

    @Named("mapParentChildren")
    default List<PersonDTO> mapParentChildren(Person person) {

        if (person == null) {
            return Collections.emptyList();
        }

        return convertToList(person.getChildren(), person);
    }
}
