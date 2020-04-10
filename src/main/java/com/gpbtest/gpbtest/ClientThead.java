package com.gpbtest.gpbtest;


import java.io.IOException;

public class ClientThead extends Thread {
    String threadName;

    public ClientThead(String name) {
        threadName = name;
    }



    public void run() {
        Long cTime = System.nanoTime();
        CLient client = new CLient();
        for(int i = 0; i < 5000; i ++) {
            String resp1 = client.sendMessage("hello" + i);
            if(!resp1.equals("hello" + i)) {
                System.out.println("error send message");
            }
        }
        String resp1 = client.sendMessage("Bue.");
        System.out.println(resp1);
        try {
            client.stop();
            Long cFinish = System.nanoTime();
            System.out.println("Thread " + threadName + " finish at: " + (cFinish - cTime));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
