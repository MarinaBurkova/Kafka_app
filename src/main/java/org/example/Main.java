package org.example;

public class Main {
    public static void main(String[] args) {
        JsonTransformConsumer consumer = new JsonTransformConsumer();
        Thread consumerThread = new Thread(consumer::startConsuming);
        consumerThread.start();

        try {
            consumerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}