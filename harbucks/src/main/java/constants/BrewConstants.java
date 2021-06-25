package constants;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class BrewConstants {

    public static final int MAX_REQUEST_CAPACITY = 50;
    public static final String MACHINE = "machine";
    public static final double DEFAULT_INGREDIENT_QUANTITY = 0D;
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String PREPARED_MESSAGE = "%s is prepared";
    public static final String CANT_PREPARE_MESSAGE = "%s cannot be prepared because %s is not available";

    private BrewConstants() {
    }
}
