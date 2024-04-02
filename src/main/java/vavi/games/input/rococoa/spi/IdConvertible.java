/*
 * Copyright (c) 2024 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.games.input.rococoa.spi;


import net.java.games.input.Component.Identifier;


/**
 * IdConvertible.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2024-01-31 nsano initial version <br>
 */
public interface IdConvertible {

    Identifier normalize(String id);
}
