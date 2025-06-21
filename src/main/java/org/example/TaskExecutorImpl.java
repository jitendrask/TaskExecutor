package org.example;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskExecutorImpl implements TaskExecutor{

    private final ExecutorService pool;
    public TaskExecutorImpl(){
        /*
            Virtual Thread Task Executor created to maximise
            use of System resources.

         */
        pool = Executors.newVirtualThreadPerTaskExecutor();
    }
    @Override
    public <T> Future<T> submitTask(Task<T> task) {
        return CompletableFuture.supplyAsync(()-> {
            try {
                /*
                Lock is placed on the Group so that only one task
                can be performed from a group.
                 */
                synchronized (task.taskGroup()) {
                    return task.taskAction().call();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, pool);
    }
}
