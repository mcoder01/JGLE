package com.mcoder.jge.g3d.render.pipeline;

import java.util.ArrayList;

public class Pipeline extends ArrayList<Worker> {
    @Override
    public boolean add(Worker worker) {
        if (size() == Runtime.getRuntime().availableProcessors())
            throw new RuntimeException("Exceeded maximum number of workers!");

        boolean result = super.add(worker);
        if (size() >= 2) get(size()-2).setNext(worker);
        worker.start();
        return result;
    }

    public void submit(Object... input) {
        get(0).signal(input);
    }
}
