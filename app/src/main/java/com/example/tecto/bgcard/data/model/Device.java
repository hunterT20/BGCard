package com.example.tecto.bgcard.data.model;

public class Device {
    private int balance;
    private String listener;
    private Boolean running;
    private String status;

    public Device() {
    }

    public Device(int balance, String listener, Boolean running, String status) {
        this.balance = balance;
        this.listener = listener;
        this.running = running;
        this.status = status;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
