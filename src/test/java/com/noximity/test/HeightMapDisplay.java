package com.noximity.test;

import net.skypieamc.worldgen.utils.Noise;

import java.util.Random;

public class HeightMapDisplay implements Display{

    final int air = 0xa0b9fa;
    final int dirt = 0x8B4513;
    final int stone = 0x808080;
    final int bedrock = 0x000000;
    final int sand = 0xFFD700;
    final int water = 0x0000FF;
    final int grass = 0x00FF00;
    final int sealevel = 63;

    final Noise noise = new Noise(new Random());
    final Noise noise2 = new Noise(new Random());



    @Override
    public int getColour(int x, int y) {
        double height = noise.sample(x * 0.01) * 100;
        height = height+sealevel-5;
        height = height+ noise2.sample(x * 0.08) * 20;



        if (y > height) {
            if (y >sealevel){
                return air;
            }
            return water;
        }

        if (y > height-1) {
            if (y<sealevel){
                return sand;
            }
            return grass;
        }

        if (y > height-5) {
            return dirt;
        }

        return stone;
    }
}
