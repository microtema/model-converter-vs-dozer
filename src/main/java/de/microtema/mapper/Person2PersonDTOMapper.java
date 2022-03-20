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
import java.util.stream.Collectors;

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
            @Mapping(target = "lastName", source = "parent.lastName", qualifiedByName = "mapParentName"), // default value is ignored on null parent

            // delegate
            @Mapping(target = "addresses", source = "person.addressList"),

            // default values
            @Mapping(target = "jobTitle", ignore = true), // explicit declaring

            // expression
            @Mapping(target = "createdDate", expression = "java(ZonedDateTime.now(ZoneId.of(\"CET\")).format(DateTimeFormatter.ISO_DATE_TIME))") // complex expression
    })
    PersonDTO convert(Person person, Person parent);

    default List<PersonDTO> convertToList(Collection<Person> persons, Person parent) { // return null instead of empty List on null persons

        return persons.stream().map(it -> convert(it, parent)).collect(Collectors.toList());
    }

    @Named("personName")
    default String mapParentName(String parentLastName) {

        if (parentLastName == null) {
            return "unknown";
        }

        return parentLastName;
    }

    @Named("mapParentChildren")
    default List<PersonDTO> mapParentChildren(Person person) {

        List<Person> children = person.getChildren();

        if (children == null) {
            return Collections.emptyList();
        }

        return children.stream().map(it -> convert(it, it)).collect(Collectors.toList());
    }
}
