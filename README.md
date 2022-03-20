# Model converter vs others mappers...

| Name | Description | Performance (1000) | Link |
| --- | --- | --- | --- |
| model-converter | Reducing Boilerplate Code with pure java interface converter | 12 | [Docu](https://bitbucket.org/modelconverter/model-converter-api.git)
| mapstruct | MapStruct is a code generator that greatly simplifies the implementation of mappings between Java bean types based on a convention over configuration approach | 58 | [Docu](https://mapstruct.org)
| dozer | Dozer is a Java Bean to Java Bean mapper that recursively copies data from one object to another | 253 | [Docu](http://dozer.sourceforge.net/)
| orika | Orika simpler, lighter and faster Java bean mapping | 631 | [Docu](https://orika-mapper.github.io/orika-docs/)
| jmapper | With JMapper we have all the advantages of dynamic mapping with the performance of static code, with 0 memory consumption. | 502 | [Docu](https://jmapper-framework.github.io/jmapper-core/)
| Modelmapper | Conversion to a destination type or property can be delegated to a Converter. Converters generally take the place of any implicit or explicit mappings between two types (see below for exceptions). | 233 | [Docu](http://modelmapper.org/user-manual/converters/)

## Model Converter

> Model Converter reduce Boilerplate Code and give more Time for Feature functionality Through a simple set of interface and saving 60% of development time

> Model Converter is pure java classes converter

> Model Converter follow the clean code pattern by restrict numbers of parameters to mostly one and in some cases to two.

```
@RequiredArgsConstructor
public class Person2PersonDTOConverter implements MetaConverter<PersonDTO, Person, Person> {

    private final Enum2StringMapper enum2StringMapper;
    private final Address2AddressDTOConverter address2AddressDTOConverter;

    @Override
    public PersonDTO convert(Person orig, Person parent) {

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
        dest.setLastName(Optional.ofNullable(parent).map(Person::getLastName).orElse("unknown"));

        // nested collections
        dest.setChildren(convertList(orig.getChildren(), orig));

        return dest;
    }
}
```

## MapStruct

> MapStruct is a Java annotation processor for the generation of type-safe bean mapping classes.

> Keep in mind that MapStruct is an annotation processor, and the expression written in expression will be written one to one in the generated Java class. Therefore, doing a refactoring will lead to a compile error.

> Another disadvantage of MapStruct is that it currently has no support for immutable fields (through constructors for example?), which is a big bummer because I use them most of the time.
 
> The produced *Impl class is pretty readable, and if you ever decide to drop MapStruct, you still have some pretty code that works without any framework. However, the disadvantage of this in my opinion is that you have to generate code and config the pom.xml or other build tool descriptor.

> Caution: You have to recompile after each change of the abstract mapper class
Sometimes does not work very well with Lombok (another annotation-based code generator) 

> Another disadvantage of MapStruct is that when you use expressions you have to understand the syntax and here you need more time to learn them.

> Another disadvantage of MapStruct is that return null instead of empty collection. If the point of my methods is to return a Collection, the method never returns null. Null is ambiguous. Instead I return Collection.emptyXXX().

```
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
```



