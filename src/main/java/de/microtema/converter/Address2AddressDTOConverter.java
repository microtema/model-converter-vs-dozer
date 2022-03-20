package de.microtema.converter;

import de.microtema.dto.AddressDTO;
import de.microtema.model.Address;
import de.microtema.model.converter.Converter;

public class Address2AddressDTOConverter implements Converter<AddressDTO, Address> {

    @Override
    public AddressDTO convert(Address orig) {

        AddressDTO dest = new AddressDTO();

        dest.setCity(orig.getCity());
        dest.setCountry(orig.getCountry());
        dest.setStreet(orig.getStreet());
        dest.setZip(orig.getZip());

        return dest;
    }
}
