package io.github.rsk3110.riskgame;

/**
 * Generic exception for level loading.
 *
 * @author Mark Johnson
 */
public class LevelLoadException extends RuntimeException {

    public LevelLoadException(String message) {
        super(message);
    }

    public LevelLoadException(Throwable t) {
        super(t);
    }
}
