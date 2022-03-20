# Model converter vs others mappers...

| Name | Feature | Description | Performance (1000) | Link |
| --- | --- | --- | --- | --- |
| model-converter | convert, update, support collections | pure java interface converter | 12 |
| jmapper | map | pure java converter | 502 |
| dozer | map | pure java interface converter | 253 |
| orika | map | pure java interface converter | 631 |
| mapstruct | convert, support collections | generated java converter from interface, Therefore, doing a refactoring will lead to a compile error | 20 |
| Modelmapper | map | generated java converter from interface | 233 |

## Model Converter

> Model Converter reduce Boilerplate Code and give more Time for Feature functionality Through a simple set of interface and saving 60% of development time

> Model Converter is pure java classes converter

## MapStruct

> MapStruct is a Java annotation processor for the generation of type-safe bean mapping classes.

> Keep in mind that MapStruct is an annotation processor, and the expression written in expression will be written one to one in the generated Java class. Therefore, doing a refactoring will lead to a compile error.

> Another disadvantage of MapStruct is that it currently has no support for immutable fields (through constructors for example?), which is a big bummer because I use them most of the time.
 
> The produced *Impl class is pretty readable, and if you ever decide to drop MapStruct, you still have some pretty code that works without any framework. However, the disadvantage of this in my opinion is that you have to generate code and config the pom.xml or other build tool descriptor.

> Caution: You have to recompile after each change of the abstract mapper class
Sometimes does not work very well with Lombok (another annotation-based code generator) 

> Another disadvantage of MapStruct is that when you use expressions you have to understand the syntax and here you need more time to learn them.

> Another disadvantage of MapStruct is that return null instead of empty collection. If the point of my methods is to return a Collection, the method never returns null. Null is ambiguous. Instead I return Collection.emptyXXX().



