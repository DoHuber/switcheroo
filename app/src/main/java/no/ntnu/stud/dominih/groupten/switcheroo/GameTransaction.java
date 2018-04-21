package no.ntnu.stud.dominih.groupten.switcheroo;

@SuppressWarnings("WeakerAccess")
public class GameTransaction {

    public static final String TYPE_TEXT = "text/plain";
    public static final String TYPE_IMG = "image/*";
    public static final String TYPE_END = "null";

    public GameTransaction() {}

    public GameTransaction(String recipientId, String payloadType, String payload, String nextRecipient) {
        this.recipientId = recipientId;
        this.payloadType = payloadType;
        this.payload = payload;
        this.nextRecipient = nextRecipient;
    }

    String recipientId;
    String payloadType;
    String payload;
    String nextRecipient;

    @Override
    public String toString() {
        return "GameTransaction{" +
                "recipientId='" + recipientId + '\'' +
                ", payloadType='" + payloadType + '\'' +
                ", payload='" + payload + '\'' +
                ", nextRecipient='" + nextRecipient + '\'' +
                '}';
    }
}
