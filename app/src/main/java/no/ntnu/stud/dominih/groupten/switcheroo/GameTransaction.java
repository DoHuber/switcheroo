package no.ntnu.stud.dominih.groupten.switcheroo;

@SuppressWarnings("WeakerAccess")
public class GameTransaction {

    public static final String TYPE_TEXT = "no.ntnu.stud.dominih.groupten.switcheroo.transactions.text";
    public static final String TYPE_IMG = "no.ntnu.stud.dominih.groupten.switcheroo.transactions.image";
    public static final String TYPE_END = "no.ntnu.stud.dominih.groupten.switcheroo.transactions.endofgame";
    public static final String TYPE_DONE = "no.ntnu.stud.dominih.groupten.switcheroo.transactions.done";
    public static final String TYPE_NEXT = "no.ntnu.stud.dominih.groupten.switcheroo.transactions.next";

    public GameTransaction() {
    }

    public GameTransaction(String recipientId, String type, String payload, String senderId) {
        this.recipientId = recipientId;
        this.type = type;
        this.payload = payload;
        this.senderId = senderId;
    }

    String recipientId;
    String type;
    String payload;
    String senderId;

    @Override
    public String toString() {
        return "GameTransaction{" +
                "recipientId='" + recipientId + '\'' +
                ", type='" + type + '\'' +
                ", payload='" + payload + '\'' +
                ", senderId='" + senderId + '\'' +
                '}';
    }
}
