package com.jsocket;

@FunctionalInterface
public interface IEchoHandler {
    public void handle(String response);
}
