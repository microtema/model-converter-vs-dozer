package de.microtema.converter;

import de.microtema.dto.PersonDTO;
import de.microtema.mapper.Enum2StringMapper;
import de.microtema.model.Person;
import de.microtema.model.converter.MetaConverter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class Person2PersonDTOConverter implements MetaConverter<PersonDTO, Person, String> {

    private final Enum2StringMapper enum2StringMapper;
    private final Address2AddressDTOConverter address2AddressDTOConverter;

    @Override
    public PersonDTO convert(Person orig, String familyName) {

        if (orig == null) {
            return null;
        }

        PersonDTO dest = new PersonDTO();

        // property mapping
        dest.setId(orig.getPersonId());
        dest.setEmail(orig.getEmailAddress());
        dest.setDob(orig.getDateOfBirth());
        dest.setFirstName(orig.getPersonName());
        dest.setMarried(orig.isMarriedStatus());

        // delegate to other converter
        dest.setGenreName(enum2StringMapper.convert(orig.getGenre()));
        dest.setAddresses(address2AddressDTOConverter.convertList(orig.getAddressList()));

        // custom handling
        dest.setLastName(Optional.ofNullable(familyName).orElse("unknown"));

        // nested collections
        dest.setChildren(convertList(orig.getChildren(), familyName));

        return dest;
    }
}
