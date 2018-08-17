package com.example.tecto.bgcard.data.model;

public class Card {
    private int card_history_id;
    private int customer_id;
    private int date_add;
    private String typename;
    private int card_image_id;
    private String cardserial;
    private String cardnumber;
    private Float amount;
    private Float rate;
    private Float money;
    private int status;
    private String transaction_id;
    private int status_nap;
    private String ip;
    private String note;
    private String checkout;
    private int status_app;

    public Card() {
    }

    public Card(int card_history_id, int customer_id, int date_add, String typename, int card_image_id, String cardserial, String cardnumber, Float amount, Float rate, Float money, int status, String transaction_id, int status_nap, String ip, String note, String checkout, int status_app) {
        this.card_history_id = card_history_id;
        this.customer_id = customer_id;
        this.date_add = date_add;
        this.typename = typename;
        this.card_image_id = card_image_id;
        this.cardserial = cardserial;
        this.cardnumber = cardnumber;
        this.amount = amount;
        this.rate = rate;
        this.money = money;
        this.status = status;
        this.transaction_id = transaction_id;
        this.status_nap = status_nap;
        this.ip = ip;
        this.note = note;
        this.checkout = checkout;
        this.status_app = status_app;
    }

    public int getCard_history_id() {
        return card_history_id;
    }

    public void setCard_history_id(int card_history_id) {
        this.card_history_id = card_history_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getDate_add() {
        return date_add;
    }

    public void setDate_add(int date_add) {
        this.date_add = date_add;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getCard_image_id() {
        return card_image_id;
    }

    public void setCard_image_id(int card_image_id) {
        this.card_image_id = card_image_id;
    }

    public String getCardserial() {
        return cardserial;
    }

    public void setCardserial(String cardserial) {
        this.cardserial = cardserial;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getStatus_nap() {
        return status_nap;
    }

    public void setStatus_nap(int status_nap) {
        this.status_nap = status_nap;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public int getStatus_app() {
        return status_app;
    }

    public void setStatus_app(int status_app) {
        this.status_app = status_app;
    }
}
