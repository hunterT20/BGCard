package com.example.tecto.bgcard.data.model;

public class CardTrue {
    private int card_true_id;
    private String cardserial;
    private String cardnumber;
    private int date_check;
    private int status;

    public CardTrue() {
    }

    public CardTrue(int card_true_id, String cardserial, String cardnumber, int date_check, int status) {
        this.card_true_id = card_true_id;
        this.cardserial = cardserial;
        this.cardnumber = cardnumber;
        this.date_check = date_check;
        this.status = status;
    }

    public int getCard_true_id() {
        return card_true_id;
    }

    public void setCard_true_id(int card_true_id) {
        this.card_true_id = card_true_id;
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

    public int getDate_check() {
        return date_check;
    }

    public void setDate_check(int date_check) {
        this.date_check = date_check;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
