package org.example;

import java.util.Date;

public class Load {
    public String generateRandomString(TaskGroup taskGroup){
        System.out.println("Started Task Group : "+taskGroup.groupUUID() +" "+ new Date());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Ending Task Group  : "+taskGroup.groupUUID() +" "+ new Date());
        return String.valueOf(Math.random());
    }
}
