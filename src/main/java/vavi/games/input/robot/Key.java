/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.robot;

import java.util.function.Consumer;


/**
 * Key.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-26 nsano initial version <br>
 */
public class Key {

    private final int code;
    private final Consumer<Integer> pressAction;
    private final Consumer<Integer> releaseAction;
    private boolean pressed;

    public Key(int code, Consumer<Integer> pressAction, Consumer<Integer> releaseAction) {
        this.code = code;
        this.pressAction = pressAction;
        this.releaseAction = releaseAction;
    }

    public void press() {
        if (!pressed) {
            pressAction.accept(code);
            pressed = true;
        }
    }

    public void release() {
        if (pressed) {
            releaseAction.accept(code);
            pressed = false;
        }
    }
}
