package de.microtema.mapper;


import de.microtema.dto.AddressDTO;
import de.microtema.model.Address;
import org.mapstruct.Mapper;

@Mapper
interface Address2AddressDTOMapper {

    AddressDTO convert(Address address);
}
