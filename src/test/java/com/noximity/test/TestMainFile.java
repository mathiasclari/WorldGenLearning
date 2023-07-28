package com.noximity.test;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMainFile extends PanelTest{

    private Map<IntPoint, ViewChunk> chunks = new HashMap<>();
    private Display display;
    private int selected = 0;

    @Override
    public void redraw(boolean reuse) {
        if (reuse) {
            for (ViewChunk chunk : this.chunks.values()) {
                chunk.shouldDestroy = true;
            }
        }
        else {
            this.chunks.clear();
        }

        super.redraw(reuse);

        for (IntPoint pos : List.copyOf(this.chunks.keySet())) {
            if (this.chunks.get(pos).shouldDestroy) {
                this.chunks.remove(pos);
            }
        }
    }

    @Override
    protected int getColour(int x, int y) {
        // adjust positions
        x >>= 2;
        y = -(y >> 2);

        IntPoint key = new IntPoint(x >> ViewChunk.CHUNK_SHIFT, y >> ViewChunk.CHUNK_SHIFT);

        ViewChunk chunk = this.chunks.get(key);

        if (chunk == null) {
            synchronized (this.display) {
                // computeIfAbsent since could be added by another thread
                chunk = this.chunks.computeIfAbsent(key, this.display::genChunk);
            }
        }

        chunk.shouldDestroy = false;
        return chunk.get(x & ViewChunk.CHUNK_MOD, y & ViewChunk.CHUNK_MOD);
    }


    public static void main(String[] args) {
        TestMainFile test = new TestMainFile();
        Display displays[] = {
            new HeightMapDisplay(),
                new TwoDDisplay(),
                new TopDownDisplay()
        };

        test.display = displays[0];
        test.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        int code = e.getKeyCode();

                        // special actions here
                        if (code == 37) {
                            // mode special action
                            test.display.modifyView(-1);
                            test.redraw(false);
                        }
                        else if (code == 39 || e.getKeyChar() == ' ') {
                            // mode special action
                            test.display.modifyView(1);
                            test.redraw(false);
                        }
                        else {
                            // mode type
                            int chnumV = Character.getNumericValue(e.getKeyChar());
                            if (chnumV == 0) chnumV = 10; // because of arrangement on keyboard

                            if (chnumV > 0 && chnumV <= displays.length && chnumV != test.selected) {
                                test.selected = chnumV;
                                test.display = displays[chnumV - 1];
                                test.redraw(false);
                            }
                        }
                    }
                });
        test.start("Earth");
    }
}
