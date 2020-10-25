package io.github.rsk3110.riskgame;

public class LevelLoadException extends RuntimeException {

    public LevelLoadException(String message) {
        super(message);
    }

    public LevelLoadException(Throwable t) {
        super(t);
    }
}
