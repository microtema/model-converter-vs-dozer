package de.microtema.mapper;

import de.microtema.dto.AddressDTO;
import de.microtema.model.Address;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-20T09:15:22+0100",
    comments = "version: 1.3.0.Beta2, compiler: javac, environment: Java 1.8.0_282 (AdoptOpenJDK)"
)
class Address2AddressDTOMapperImpl implements Address2AddressDTOMapper {

    @Override
    public AddressDTO convert(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDTO addressDTO = new AddressDTO();

        addressDTO.setStreet( address.getStreet() );
        addressDTO.setCity( address.getCity() );
        addressDTO.setZip( address.getZip() );
        addressDTO.setCountry( address.getCountry() );

        return addressDTO;
    }
}
