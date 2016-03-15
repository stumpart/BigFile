package com.stumpart.bigfile;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import static org.junit.Assert.*;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileLineWorkerTest {
    private final Logger logger = LoggerFactory.getLogger(FileLineWorkerTest.class);
    ExecutorService executorService;

    @Test
    public void testRun() throws InterruptedException, ExecutionException {
        executorService = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);
        String line = "foo,bar,baz";
        AtomicInteger atomicInteger = new AtomicInteger();
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(1);
        Consumer<String> f = (l) -> {
          assertEquals(line, l);
        };

        arrayBlockingQueue.put(line);
        FileLineWorker fileLineWorker = new FileLineWorker(arrayBlockingQueue, atomicInteger, f);

        Future<?> res =  executorService.submit(fileLineWorker);
        res.get();
    }

    @After
    public void tearDown(){
        executorService.shutdown();
    }
}
