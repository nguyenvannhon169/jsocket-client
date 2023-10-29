package com.jsocket;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        JsClient client = new JsClient("192.168.1.4", 3456);

        client.onEcho("Hi", (response) -> {
            System.out.println(response);
        });

        client.connect();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            if(scanner.nextLine().equals("OK")) {
                System.exit(0);
            }
        }
    }
}