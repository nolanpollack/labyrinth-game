package com.northeastern.labyrinth.Util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class DaemonThreadFactory implements ThreadFactory {
    private final ThreadFactory threadFactory = Executors.defaultThreadFactory();

    @Override
    public Thread newThread(Runnable r) {
        Thread t = threadFactory.newThread(r);
        t.setDaemon(true);
        return t;
    }
}
