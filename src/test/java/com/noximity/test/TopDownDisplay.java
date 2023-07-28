package com.noximity.test;

import net.skypieamc.worldgen.utils.Noise;

import java.util.Random;

public class TopDownDisplay implements Display{

    final int air = 0xa0b9fa;
    final int dirt = 0x8B4513;
    final int stone = 0x808080;
    final int bedrock = 0x000000;
    final int sand = 0xFFD700;
    final int water = 0x0000FF;
    final int grass = 0x00FF00;
    final int sealevel = 63;

    Noise noise = new Noise(new Random());


    @Override
    public int getColour(int x, int y) {
        double secondnoise = noise.sample(x*0.01,y*0.01);
        if (secondnoise > 0){
            return grass;
        }

    return water;
    }
}
