package org.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Application {

    public static List<TaskGroup> getGroupIds(int n) {
        return IntStream.range(0, n).mapToObj(i -> new TaskGroup(UUID.randomUUID())).collect(Collectors.toList());
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Future<String>> futures = new ArrayList<>();
        /*
        Test Configuration
         */
        int numberOfTasks = 10;
        int numberOfGroups = 4;

        /*
        Generating Groups as requested
         */
        List<TaskGroup> taskGroups = getGroupIds(numberOfGroups);

        int taskGroupCounter = -1;
        System.out.println(new Date());
        /*
        Started Submitting Tasks
         */
        for (int i = 0; i < numberOfTasks; i++) {
            if (taskGroupCounter > taskGroups.size()-2)
                taskGroupCounter = 0;
            else
                taskGroupCounter = taskGroupCounter +1;
            final AtomicReference<Integer> reference = new AtomicReference<>();
            reference.set(taskGroupCounter);
            Task<String> task = new Task<>(
                    UUID.randomUUID(),
                    taskGroups.get(taskGroupCounter),
                    TaskType.READ,
                    () -> new Load().generateRandomString(taskGroups.get(reference.get())));
            futures.add(new TaskExecutorImpl().submitTask(task));
            System.out.println("Submitted task :" + task.taskUUID()  + " Group :" +task.taskGroup().groupUUID());

        }

        /*
        Getting the results sequentially in order as requested
         */
        System.out.println(new Date());
        for (Future<String> future : futures) {
            System.out.println(future.get());
        }
        System.out.println(new Date());
    }
}
