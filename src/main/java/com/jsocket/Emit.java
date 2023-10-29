package com.jsocket;

import com.google.gson.Gson;
import java.util.List;

public class Emit {

    protected List<JsClient> clientList;
    protected Gson gson;

    public Emit(List<JsClient> clientList) {
        this.clientList = clientList;
        this.gson = new Gson();
    }

    public void dispatch(String command, String content) {
        for (JsClient client: this.clientList) {
            client.output.println("JSocket =##= " + gson.toJson(new Message(command, content)));
        }
    }
}
