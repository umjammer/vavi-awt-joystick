/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.rococoa.spi;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Rumbler;


/**
 * RococoaRumbler.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-03-27 nsano initial version <br>
 */
public class RococoaRumbler implements Rumbler {

    private final int reportId;
    private final Component.Identifier identifier;
    private float value;

    public RococoaRumbler(int reportId, Component.Identifier identifier) {
        this.reportId = reportId;
        this.identifier = identifier;
    }

    @Override
    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String getOutputName() {
        return identifier.getName();
    }

    @Override
    public Identifier getOutputIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return getOutputName();
    }
}
