package service;

import entities.Drink;

public class Brew implements Runnable {

    private Drink drink;

    Brew(Drink drink) {
        this.drink = drink;
    }

    /**
     * Brews the dink on different threads to ensure parallel processing
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void run() {
        InventoryService.getInstance().deductIngredients(drink);
    }
}
