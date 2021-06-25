package service;

import entities.Drink;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

import static constants.BrewConstants.*;

@Singleton
public class InventoryService {

    private static InventoryService inventoryService = new InventoryService();
    private Map<String, Double> inventory = new HashMap<>();

    /**
     * Using singleton pattern here to ensure we have only 1 inventory instance.
     *
     * @return existing instance of {@link InventoryService}
     */
    public static InventoryService getInstance() {
        return inventoryService;
    }

    /**
     * Fill/Re-fill of the inventories of the coffee maker. If the ingredient is present, it will add the desired
     * quantity to the existing value. If the ingredient is not present, it will add the ingredient.
     * <p>
     * This method can be further improved to maintain a maximum value that can be put in the inventory for each
     * ingredient to mimic a physical coffee maker compartments to avoid overflow of ingredient values.
     *
     * @param ingredients {@link HashMap} of the ingredients name and quantity
     */
    public void addInventories(Map<String, Double> ingredients) {
        ingredients.forEach((key, value) -> {
            Double existingQuantity = inventory.getOrDefault(key, DEFAULT_INGREDIENT_QUANTITY);
            inventory.put(key, existingQuantity + value);
        });
    }

    /**
     * Prepares the {@link Drink} specified in the input. Drink will have a name and ingredients to prepare. If the
     * inventory has enough items to prepare the drink, the drink will be served normally. Else there will be a message
     * shown to specify the first ingredient that fell short for making. This message would be a cue to refill the
     * inventory using addInventories(..) method.
     * <p>
     * added synchronized keyword to ensure only 1 thread is able to access this at a time to avoid any race condition
     * in deduction of ingredients.
     *
     * @param drink Drink to prepare.
     */
    public synchronized void deductIngredients(Drink drink) {
        if (checkIfDrinkPossible(drink)) {
            for (Map.Entry<String, Double> entry : drink.getIngredients().entrySet()) {
                String ingredient = entry.getKey();
                Double currentQuantity = inventory.getOrDefault(ingredient, DEFAULT_INGREDIENT_QUANTITY);
                inventory.put(ingredient, currentQuantity - entry.getValue());
            }
            System.out.println(String.format(PREPARED_MESSAGE, drink.getName()));
        }
    }

    /**
     * @return true if the drink can be prepared by current state of inventory false, if any of the ingredient falls
     * short
     */
    private synchronized boolean checkIfDrinkPossible(Drink drink) {
        for (Map.Entry<String, Double> entry : drink.getIngredients().entrySet()) {
            String ingredient = entry.getKey();
            Double currentQuantity = inventory.getOrDefault(ingredient, DEFAULT_INGREDIENT_QUANTITY);
            if (currentQuantity < entry.getValue()) {
                System.out.println(String.format(CANT_PREPARE_MESSAGE, drink.getName(), ingredient));
                return false;
            }
        }
        return true;
    }
}
