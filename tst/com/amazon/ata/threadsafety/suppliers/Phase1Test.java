package com.amazon.ata.threadsafety.classroom.suppliers;

import com.amazon.ata.threadsafety.classroom.FlavorRequestQueue;
import com.amazon.ata.threadsafety.classroom.IceCreamMaker;
import com.amazon.ata.threadsafety.classroom.dao.CartonDao;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.amazon.ata.threadsafety.classroom.model.Flavor.CHOCOLATE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Phase1Test {
    CartonDao cartonDao = new CartonDao();
    FlavorRequestQueue flavorRequestQueue = new FlavorRequestQueue();

    @Test
    public void flavorRequestQueue_iceCreamMakersAfterRequests_handles10000Requests() throws Exception {
        // GIVEN
        // 20 ice cream makers
        List<IceCreamMaker> iceCreamMakers = IntStream.range(0, 20)
            .mapToObj(i -> new IceCreamMaker(cartonDao, flavorRequestQueue))
            .collect(Collectors.toList());
        // 10K requests
        int numRequests = 10000;
        IntStream.range(0, numRequests)
            .forEach(i -> flavorRequestQueue.needFlavor(CHOCOLATE));

        // WHEN
        // We start the IceCreamMakers on their own threads
        ExecutorService executorService = Executors.newCachedThreadPool();
        iceCreamMakers.forEach(executorService::submit);
        executorService.shutdown();
        Thread.sleep(2000L);

        // THEN
        assertEquals(0, flavorRequestQueue.requestCount(),
            "All requests not fulfilled! " + cartonDao.inventoryOfFlavor(CHOCOLATE) + " completed.");
    }

}
