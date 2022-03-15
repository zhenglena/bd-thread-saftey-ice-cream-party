package com.amazon.ata.threadsafety.suppliers;

import com.amazon.ata.threadsafety.FlavorRequestQueue;
import com.amazon.ata.threadsafety.IceCreamMaker;
import com.amazon.ata.threadsafety.dao.CartonDao;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.amazon.ata.threadsafety.model.Flavor.CHOCOLATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Phase0Test {
    CartonDao cartonDao = new CartonDao();
    FlavorRequestQueue flavorRequestQueue = new FlavorRequestQueue();

    @Test
    public void constructor_iceCreamMaker_succeeds() throws Exception {
        // GIVEN
        // We're running this test

        // WHEN
        // We create an IceCreamMaker
        IceCreamMaker iceCreamMaker = new IceCreamMaker(cartonDao, flavorRequestQueue);

        // THEN
        assertNotNull(iceCreamMaker, "Expected to be able to create an IceCreamMaker!");
    }

}
