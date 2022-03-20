package de.microtema.converter;

import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.api.JMapperAPI;
import de.microtema.dozer.Enum2StringConverter;
import de.microtema.dto.AddressDTO;
import de.microtema.dto.PersonDTO;
import de.microtema.mapper.Person2PersonDTOMapper;
import de.microtema.model.Address;
import de.microtema.model.Person;
import de.microtema.model.converter.Converter;
import de.microtema.model.converter.MetaConverter;
import de.seven.fate.model.builder.annotation.Model;
import de.seven.fate.model.builder.annotation.Models;
import de.seven.fate.model.builder.util.ClassUtil;
import de.seven.fate.model.builder.util.FieldInjectionUtil;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

import static com.googlecode.jmapper.api.JMapperAPI.attribute;
import static com.googlecode.jmapper.api.JMapperAPI.mappedClass;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Person2PersonDTOConverterTest {

    @Models(size = 1_000)
    List<Person> persons;

    @Model
    Person parent;

    @Model
    String personName;

    @BeforeEach
    void setUp() {
        FieldInjectionUtil.injectFields(this);

        persons.get(0).setPersonName(personName);
    }

    @Test
    void modelConverter() {

        MetaConverter<PersonDTO, Person, String> sut = ClassUtil.createInstance(Person2PersonDTOConverter.class);

        long startTimeInMillis = System.currentTimeMillis();

        List<PersonDTO> personDTOS = sut.convertList(persons, parent.getLastName());

        assertCollection(personDTOS);

        assertEquals(persons.get(0).getPersonName(), personDTOS.get(0).getFirstName());

        long duration = System.currentTimeMillis() - startTimeInMillis;

        System.out.println(duration + " - Model Converter");
    }


    @Test
    void mapStruct() {

        long startTimeInMillis = System.currentTimeMillis();

        Person2PersonDTOMapper sut = Mappers.getMapper(Person2PersonDTOMapper.class);

        List<PersonDTO> personDTOS = sut.convertToList(persons, parent);

        assertCollection(personDTOS);

        //assertEquals(persons.get(0).getLastName(), personDTOS.get(0).getLastName());

        long duration = System.currentTimeMillis() - startTimeInMillis;

        System.out.println(duration + " - MapStruct");
    }


    @Test
    void dozer() {

        long startTimeInMillis = System.currentTimeMillis();

        DozerBeanMapper sut = ClassUtil.createInstance(DozerBeanMapper.class);

        BeanMappingBuilder builder = new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(Person.class, PersonDTO.class)
                        .fields("personId", "id")
                        .fields("genre", "genreName", FieldsMappingOptions.customConverter(Enum2StringConverter.class));
            }
        };

        sut.addMapping(builder);

        List<PersonDTO> personDTOS = persons.stream().map(it -> sut.map(it, PersonDTO.class)).collect(Collectors.toList());

        assertCollection(personDTOS);

        long duration = System.currentTimeMillis() - startTimeInMillis;

        System.out.println(duration + " - Dozer");
    }


    @Test
    void jMap() {

        long startTimeInMillis = System.currentTimeMillis();

        JMapperAPI jmapperAPI = new JMapperAPI()
                .add(mappedClass(PersonDTO.class)
                        .add(attribute("id").value("personId")));

        JMapper<PersonDTO, Person> sut = new JMapper(PersonDTO.class, Person.class, jmapperAPI);


        List<PersonDTO> personDTOS = persons.stream().map(sut::getDestination).collect(Collectors.toList());

        assertCollection(personDTOS);

        long duration = System.currentTimeMillis() - startTimeInMillis;

        System.out.println(duration + " - JMapper");
    }

    @Test
    void orika() {

        long startTimeInMillis = System.currentTimeMillis();

        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(Address.class, AddressDTO.class).register();

        mapperFactory.classMap(Person.class, PersonDTO.class)
                // Map different field names but same type
                .field("personId", "id")
                .customize(new CustomMapper<Person, PersonDTO>() {
                    @Override
                    public void mapAtoB(Person person, PersonDTO personDTO, MappingContext context) {

                        // Map different field type
                        personDTO.setGenreName(person.getGenre().name());

                        // Map collections with complex types
                        List<AddressDTO> addressDTOs = persons.stream().map(it -> mapperFactory.getMapperFacade().map(it, AddressDTO.class)).collect(Collectors.toList());

                        personDTO.setAddresses(addressDTOs);
                    }
                })
                .register();


        MapperFacade sut = mapperFactory.getMapperFacade();

        List<PersonDTO> personDTOS = persons.stream().map(it -> sut.map(it, PersonDTO.class)).collect(Collectors.toList());

        assertCollection(personDTOS);

        long duration = System.currentTimeMillis() - startTimeInMillis;

        System.out.println(duration + " - Orika");
    }

    @Test
    void modelmapper() {

        long startTimeInMillis = System.currentTimeMillis();

        ModelMapper modelMapper = new ModelMapper();
        List<PersonDTO> personDTOS = persons.stream().map(it -> modelMapper.map(it, PersonDTO.class)).collect(Collectors.toList());
        assertCollection(personDTOS);

        long duration = System.currentTimeMillis() - startTimeInMillis;

        System.out.println(duration + " - Modelmapper");
    }

    void assertCollection(List<PersonDTO> personDTOS) {

        assertEquals(persons.size(), personDTOS.size());

        for (int index = 0; index < persons.size(); index++) {

            Person person = persons.get(index);
            PersonDTO personDto = personDTOS.get(index);

            // assertEquals(person.getPersonId(), personDto.getId());
            //  assertEquals(person.getGenre().name(), personDto.getGenreName());
            //  assertEquals(person.getAddresses().size(), personDto.getAddresses().size());
        }
    }
}
