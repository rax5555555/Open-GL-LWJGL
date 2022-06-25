package com.rax.renderer;

public class Timer {
    private static double frame_cap = 1.0/60.0;
    private static double frame_time = 0;
    private static int frame = 0;
    private static double time = Timer.getTime();
    private static double unprocessed = 0;

    public static double getTime() {
        return (double) System.nanoTime()/(double) 1000000000L;
    }

    public static void getFPS() {
        double time_2 = Timer.getTime();
        double passed = time_2 - time;
        unprocessed = unprocessed + passed;
        frame_time += passed;
        time = time_2;

        while (unprocessed >= frame_cap) {
            unprocessed = unprocessed - frame_cap;
            if (frame_time >= 1.0) {
                frame_time = 0;
                System.out.println("FPS = " + frame);
                frame = 0;
            }
        }
        frame++;
    }

}
