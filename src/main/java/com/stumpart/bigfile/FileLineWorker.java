package com.stumpart.bigfile;


import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class FileLineWorker implements Runnable {

    private final ArrayBlockingQueue queue;
    private final AtomicInteger atom;
    private final Consumer<String> func;

    public FileLineWorker(ArrayBlockingQueue q, AtomicInteger atom, Consumer<String> f){
        this.queue = q;
        this.atom  = atom;
        this.func  = f;
    }

    @Override
    public void run(){
        try{
            //using polling, Instead of take. Be careful not to block if the queue is empty.
            //We desire a more asynchronous way of taking the element
            Optional<String> line	= Optional.ofNullable((String)queue.poll());
            func.accept(line.orElseThrow(() -> new RuntimeException("No line supplied")));
            atom.incrementAndGet();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

}
