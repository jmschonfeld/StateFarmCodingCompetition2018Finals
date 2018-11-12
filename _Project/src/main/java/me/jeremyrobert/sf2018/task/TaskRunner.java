package me.jeremyrobert.sf2018.task;

import java.lang.reflect.Array;

public class TaskRunner {

    public static <T> T[] runTasks(Task<T>[] tasks, Class<T> clazz) {
        Thread[] threads = new Thread[tasks.length];
        T[] results = (T[]) Array.newInstance(clazz, threads.length);
        for (int i = 0; i < tasks.length; i++) {
            final int j = i;
            threads[i] = new Thread(() -> {
                results[j] = tasks[j].run();
            });
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
