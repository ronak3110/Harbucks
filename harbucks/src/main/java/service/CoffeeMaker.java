package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Barista;
import entities.Drink;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

import static constants.BrewConstants.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoffeeMaker {

    static CoffeeMaker coffeeMaker;
    Barista barista;
    ThreadPoolExecutor executor;

    /**
     * Reads the json input via {@link ObjectMapper} instance and initialises our coffee maker. Additionally initialises
     * an {@link ThreadPoolExecutor} with the specified capacity and outlets to serve in parallel.
     *
     * @param jsonInput input for the coffee maker which fills all the ingredients and beverages to serve
     * @throws IOException Any exception caused during initialising the coffee maker.
     */
    private CoffeeMaker(String jsonInput) throws IOException {
        JsonNode jsonNode = OBJECT_MAPPER.readValue(jsonInput, JsonNode.class);

        // assuming the input will be in the same format specified in the sample test
        this.barista = OBJECT_MAPPER.treeToValue(jsonNode.get(MACHINE), Barista.class);
        int outlet = barista.getNozzle().getCount();

        //limiting max requests acceptance size to 50 here. Can be tweaked as per the requirements
        executor = new ThreadPoolExecutor(outlet, outlet, 5, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(MAX_REQUEST_CAPACITY));
    }

    /**
     * @param jsonInput input for the coffee maker which fills all the ingredients and beverages to serve
     * @return {@link CoffeeMaker} Existing instance or creates a new Coffee maker instance if it doesn't exist.
     * @throws IOException Any exception caused during initialising the coffee maker.
     */
    public static CoffeeMaker getInstance(final String jsonInput) throws IOException {
        //using singleton pattern here to ensure we have only 1 coffee maker instance.
        if (coffeeMaker == null) {
            coffeeMaker = new CoffeeMaker(jsonInput);
        }
        return coffeeMaker;
    }

    /**
     * Starts brewing the drinks specified by the init input params. The brewing will happen via a {@link
     * ThreadPoolExecutor} task that will brew the drinks in parallel. Number of nozzles/outlets is the throttler here.
     */
    public void brew() {
        InventoryService.getInstance().addInventories(barista.getIngredients());
        Map<String, Map<String, Double>> beverages = barista.getBeverages();
        beverages.keySet().parallelStream().forEach(key -> coffeeMaker.addBeverageRequest(key, beverages.get(key)));
    }

    /**
     * Equivalent to pressing the button for the drink on a real coffee maker Passes the request to a task executor
     * {@link Brew} service to process
     *
     * @param drinkName   Name of the drink to brew
     * @param ingredients Ingredients required for the drink
     */
    private void addBeverageRequest(String drinkName, Map<String, Double> ingredients) {
        Drink drink = new Drink(drinkName, ingredients);
        executor.execute(new Brew(drink));
    }
}