package de.microtema.mapper;

import de.microtema.dto.PersonDTO;
import de.microtema.model.Person;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-20T09:59:51+0100",
    comments = "version: 1.3.0.Beta2, compiler: javac, environment: Java 1.8.0_282 (AdoptOpenJDK)"
)
public class Person2PersonDTOMapperImpl implements Person2PersonDTOMapper {

    private final Enum2StringMapper enum2StringMapper = new Enum2StringMapper();

    @Override
    public PersonDTO convert(Person person, Person parent) {
        if ( person == null && parent == null ) {
            return null;
        }

        PersonDTO personDTO = new PersonDTO();

        if ( person != null ) {
            personDTO.setFirstName( person.getPersonName() );
            personDTO.setDob( person.getDateOfBirth() );
            personDTO.setGenreName( enum2StringMapper.convert( person.getGenre() ) );
            personDTO.setId( person.getPersonId() );
            personDTO.setMarried( person.isMarriedStatus() );
            personDTO.setEmail( person.getEmailAddress() );
        }
        if ( parent != null ) {
            personDTO.setLastName( mapParentName( parent ) );
            personDTO.setChildren( mapParentChildren( parent ) );
        }
        personDTO.setCreatedDate( ZonedDateTime.now(ZoneId.of("CET")).format(DateTimeFormatter.ISO_DATE_TIME) );

        return personDTO;
    }

    @Override
    public List<PersonDTO> convertToList(Collection<Person> persons) {
        if ( persons == null ) {
            return null;
        }

        List<PersonDTO> list = new ArrayList<PersonDTO>( persons.size() );
        for ( Person person : persons ) {
            list.add( personToPersonDTO( person ) );
        }

        return list;
    }

    protected PersonDTO personToPersonDTO(Person person) {
        if ( person == null ) {
            return null;
        }

        PersonDTO personDTO = new PersonDTO();

        personDTO.setLastName( person.getLastName() );
        personDTO.setChildren( convertToList( person.getChildren() ) );

        return personDTO;
    }
}
