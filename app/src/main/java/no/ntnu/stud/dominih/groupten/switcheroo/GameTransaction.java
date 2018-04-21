package no.ntnu.stud.dominih.groupten.switcheroo;

public class GameTransaction {

    public GameTransaction(String recipientId, String payloadType, String payload) {
        this.recipientId = recipientId;
        this.payloadType = payloadType;
        this.payload = payload;
    }

    String recipientId;
    String payloadType;
    String payload;



}
