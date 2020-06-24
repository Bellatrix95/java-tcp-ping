package main.java.com.company.utils;

import main.java.com.company.Message;

import java.util.HashMap;


public class Analysis {
    private int messagesSend;
    private int messagesReceved;
    private HashMap<Integer, Message> messages;

    public Analysis(HashMap<Integer, Message> messages) {
        this.messages = messages;
    }

    public float avrgAB() {
        return (float) 1.2;
    };
    public float avrgABA() {
        return (float) 1.2;
    };
    public float avrgBA() {
        return (float) 1.2;
    };
    public float maxABA() {
        return (float) 1.2;
    };
}
