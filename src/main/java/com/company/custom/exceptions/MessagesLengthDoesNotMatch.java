package main.java.com.company.custom.exceptions;

public class MessagesLengthDoesNotMatch extends Exception {
    public MessagesLengthDoesNotMatch(String errorMessage) {
        super(errorMessage);
    }
}