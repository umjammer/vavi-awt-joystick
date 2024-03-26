/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.robot;


/**
 * KeyWithCoordinate.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-26 nsano initial version <br>
 */
public class KeyWithCoordinate {

    @FunctionalInterface
    public interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }

    private final int code;
    private final TriConsumer<Integer, Integer, Integer> pressActionWithCoordinat;
    private final TriConsumer<Integer, Integer, Integer> releaseActionWithCoordinat;
    private boolean pressed;

    public KeyWithCoordinate(int code, TriConsumer<Integer, Integer, Integer> pressActionWithCoordinate, TriConsumer<Integer, Integer, Integer> releaseActionWithCoordinate) {
        this.code = code;
        this.pressActionWithCoordinat = pressActionWithCoordinate;
        this.releaseActionWithCoordinat = releaseActionWithCoordinate;
    }

    public void press(int x, int y) {
        if (!pressed) {
            pressActionWithCoordinat.accept(code, x, y);
            pressed = true;
        }
    }

    public void release(int x, int y) {
        if (pressed) {
            releaseActionWithCoordinat.accept(code, x, y);
            pressed = false;
        }
    }
}
