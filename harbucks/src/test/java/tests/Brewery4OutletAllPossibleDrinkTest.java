package tests;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import service.CoffeeMaker;

import java.io.File;

public class Brewery4OutletAllPossibleDrinkTest {

    CoffeeMaker coffeeMaker;

    @Test
    public void sampleInput4OutletAllPossibleTest() throws Exception {
        final String filePath = "test4.json";
        File file = new File(CoffeeMaker.class.getClassLoader().getResource(filePath).getFile());
        String jsonInput = FileUtils.readFileToString(file, "UTF-8");
        coffeeMaker = CoffeeMaker.getInstance(jsonInput);
        coffeeMaker.brew();
    }

}
