package com.noximity.test;

import net.skypieamc.worldgen.utils.Noise;

import java.util.Random;

public class TwoDDisplay implements Display{
    Noise noise = new Noise(new Random());

    int rgb(int red, int green, int blue) {
        return (red << 16) | (green << 8) | blue;
    }

    @Override
    public int getColour(int x, int y) {
        double secondnoise = noise.sample(x*0.01,y*0.01);
        secondnoise += 1;
        secondnoise *= (double) 255 /2;
        return rgb((int) secondnoise, (int) secondnoise, (int) secondnoise);
    }
}
