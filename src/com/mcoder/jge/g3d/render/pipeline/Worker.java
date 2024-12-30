package com.mcoder.jge.g3d.render.pipeline;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

public class Worker extends Thread {
    private final Function<Object[], Object[]> job;
    private final LinkedList<Object[]> inputQueue, outputQueue;
    private Worker next;
    private final Semaphore pendingJobs;

    public Worker(Function<Object[], Object[]> job) {
        super();
        this.job = job;
        inputQueue = new LinkedList<>();
        outputQueue = new LinkedList<>();
        pendingJobs = new Semaphore(0);
    }

    public void signal(Object... input) {
        inputQueue.add(input);
        pendingJobs.release();
    }

    @Override
    public void run() {
        super.run();
        while(isAlive()) {
            try {
                pendingJobs.acquire();
                Object[] output = job.apply(inputQueue.poll());
                outputQueue.add(output);
                if (next != null)
                    next.signal(output);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Object[] pollOutput() {
        return outputQueue.poll();
    }

    public void setNext(Worker next) {
        this.next = next;
    }
}
