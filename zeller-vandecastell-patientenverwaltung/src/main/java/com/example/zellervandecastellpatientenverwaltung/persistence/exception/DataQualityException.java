package com.example.zellervandecastellpatientenverwaltung.persistence.exception;

public class DataQualityException extends RuntimeException {
    public DataQualityException(String message) {
        super(message);
    }

    public static DataQualityException forUnsupportedEnumLiteral(char literal, Class<?> enumClass, String validValues) {
        String message = String.format("Unsupported enum literal '%c' for enum class %s in the DB, Valid values are: %s",
                literal, enumClass.getSimpleName(), validValues);
        throw new DataQualityException(message);
    }
}


//package com.example.zellervandecastellpatientenverwaltung.persistence.exception;
//
//public class DataQualityException extends RuntimeException {
//
//
//    private final Character literal;
//    private final Class<? extends Enum<?>> enumClass;
//    private final String validValues;
//
//
//
//    public DataQualityException(String message, Character literal, Class<? extends Enum<?>> enumClass, String validValues) {
//        super(message);
//        this.literal = literal;
//        this.enumClass = enumClass;
//        this.validValues = validValues;
//    }
//
//    public static DataQualityException forUnsupportedEnumLiteral(Character literal, Class<? extends Enum<?>> enumClass, String validValues) {
//        String s = "Unsupported enum literal '" + literal + "' for enum class " + enumClass.getSimpleName() + "in the DB, Valid values are: " + validValues;
//        return new DataQualityException(s, literal, enumClass, validValues);
//    }
//
//
//}
