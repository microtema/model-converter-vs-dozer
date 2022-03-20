package de.microtema.mapper;

import de.microtema.dto.AddressDTO;
import de.microtema.dto.PersonDTO;
import de.microtema.model.Address;
import de.microtema.model.Person;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Generated;
import org.mapstruct.factory.Mappers;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-20T12:35:39+0100",
    comments = "version: 1.3.0.Beta2, compiler: javac, environment: Java 1.8.0_282 (AdoptOpenJDK)"
)
public class Person2PersonDTOMapperImpl implements Person2PersonDTOMapper {

    private final Address2AddressDTOMapper address2AddressDTOMapper = Mappers.getMapper( Address2AddressDTOMapper.class );
    private final Enum2StringMapper enum2StringMapper = new Enum2StringMapper();

    @Override
    public PersonDTO convert(Person person, Person parent) {
        if ( person == null && parent == null ) {
            return null;
        }

        PersonDTO personDTO = new PersonDTO();

        if ( person != null ) {
            personDTO.setFirstName( person.getPersonName() );
            personDTO.setAddresses( addressListToAddressDTOCollection( person.getAddressList() ) );
            personDTO.setDob( person.getDateOfBirth() );
            personDTO.setGenreName( enum2StringMapper.convert( person.getGenre() ) );
            personDTO.setId( person.getPersonId() );
            personDTO.setMarried( person.isMarriedStatus() );
            personDTO.setEmail( person.getEmailAddress() );
        }
        if ( parent != null ) {
            personDTO.setLastName( mapParentName( parent.getLastName() ) );
            personDTO.setChildren( mapParentChildren( parent ) );
        }
        personDTO.setCreatedDate( ZonedDateTime.now(ZoneId.of("CET")).format(DateTimeFormatter.ISO_DATE_TIME) );

        return personDTO;
    }

    protected Collection<AddressDTO> addressListToAddressDTOCollection(List<Address> list) {
        if ( list == null ) {
            return null;
        }

        Collection<AddressDTO> collection = new ArrayList<AddressDTO>( list.size() );
        for ( Address address : list ) {
            collection.add( address2AddressDTOMapper.convert( address ) );
        }

        return collection;
    }
}
