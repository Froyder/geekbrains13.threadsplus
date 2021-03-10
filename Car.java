package ru.geek.homeworks.lesson13threadsplus;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {

    static boolean winner = false;
    private static int CARS_COUNT;

    private Race race;
    private int speed;
    private String name;
    private CyclicBarrier startBarrier;
    private CountDownLatch finishLine;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }

    public Car (Race race, int speed, CyclicBarrier startBarrier, CountDownLatch finishLine) {
        this.race = race;
        this.speed = speed;
        this.startBarrier = startBarrier;
        this.finishLine = finishLine;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится...");
            Thread.sleep(800 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов.");
            startBarrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(200 + (int)(Math.random() * 800));
            System.out.println(this.name + " начал заезд!");
            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);
            }
        } catch (NullPointerException | InterruptedException w) {
            w.printStackTrace();
        }
        finishLine.countDown();

        if (!winner) {
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> " + this.name + " прошел все этапы и одержал победу в гонке! ");
            winner = true;
        }
    }
}