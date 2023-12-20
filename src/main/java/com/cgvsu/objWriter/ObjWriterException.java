package com.cgvsu.objWriter;

public class ObjWriterException extends RuntimeException {
    public ObjWriterException(String errorMessage) {
        super("Error writing OBJ file: " + errorMessage);
    }
}
