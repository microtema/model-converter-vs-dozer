package de.microtema.mapper;


import de.microtema.enums.GENRE;

public class Enum2StringMapper {

    public String convert(GENRE genre) {

        if (genre == null) {
            return null;
        }

        return genre.name();
    }
}
