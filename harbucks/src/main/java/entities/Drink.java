package entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class Drink {

    private String name;

    private Map<String, Double> ingredients;
}
