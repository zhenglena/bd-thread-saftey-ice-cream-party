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

public class Phase2Test {
    CartonDao cartonDao = new CartonDao();
    FlavorRequestQueue flavorRequestQueue = new FlavorRequestQueue();

    @Test
    public void flavorRequestQueue_10000Requests_handles10000Requests() throws Exception {
        // GIVEN
        // 20 ice cream makers, executing in their own threads
        List<IceCreamMaker> iceCreamMakers = IntStream.range(0, 20)
            .mapToObj(i -> new IceCreamMaker(cartonDao, flavorRequestQueue))
            .collect(Collectors.toList());
        ExecutorService executorService = Executors.newCachedThreadPool();
        iceCreamMakers.forEach(executorService::submit);
        executorService.shutdown();
        // 10K requests to be made
        int numRequests = 10000;

        // WHEN
        // We actually make the requests
        IntStream.range(0, numRequests)
            .forEach(i -> flavorRequestQueue.needFlavor(CHOCOLATE));
        Thread.sleep(2000L);

        // THEN
        // The service processes all the requests without deadlocking
        assertEquals(0, flavorRequestQueue.requestCount(),
            "All requests not fulfilled! " + cartonDao.inventoryOfFlavor(CHOCOLATE) + " completed.");
    }

}
