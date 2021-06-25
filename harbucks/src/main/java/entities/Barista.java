package entities;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Barista {

    Nozzle nozzle;

    Map<String, Double> ingredients;

    Map<String, Map<String, Double>> beverages;

    @JsonCreator
    @Builder
    public Barista(@JsonProperty("outlets") Nozzle nozzle,
                   @JsonProperty("total_items_quantity") Map<String, Double> ingredients,
                   @JsonProperty("beverages") Map<String, Map<String, Double>> beverages) {
        this.nozzle = nozzle;
        this.ingredients = ingredients;
        this.beverages = beverages;
    }
}
