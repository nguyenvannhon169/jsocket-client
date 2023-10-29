package com.jsocket;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

public class JsClient {
    protected Socket socket;
    protected String address;
    protected int port;
    protected boolean isConnected;

    protected PrintWriter output;
    protected BufferedReader input;
    protected Thread thread;

    protected HashMap<String, IEchoHandler> echoHandlers;

    public JsClient(String address, int port) {
        this.address = address;
        this.port = port;
        this.isConnected = false;
        this.echoHandlers = new HashMap<>();
    }

    private void setup() {
        try {
            socket = new Socket(address, port);
            this.output = new PrintWriter(socket.getOutputStream(), true);
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String request = new Gson().toJson(new Message("connection", "Hello JSocket"));
            output.println("A =##=" + request);
            this.thread = new Thread(() -> {
                while (!socket.isClosed()) {
                    try {
                        if (input.read() != -1) {
                            String[] response = input.readLine().split("=##=");
                            if(response.length == 2 && !response[1].isBlank()) {
                                Message message = new Gson().fromJson(response[1], Message.class);
                                if (echoHandlers.containsKey(message.command)) {
                                    echoHandlers.get(message.command).handle(message.content);
                                }
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (socket.isClosed()) break;
                }
            });

            thread.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void connect(int port) {
        this.port = port;
        setup();
    }

    public void connect() {
       setup();
    }

    public void onEcho(String command ,IEchoHandler echoHandler) {
        if (echoHandlers.containsKey(command)) {
            System.out.println("Command ["+command+"] duplicated.");
        }
        echoHandlers.put(command, echoHandler);
    }

    public Emit onEmit() {
        return new Emit(List.of(this));
    }
}
