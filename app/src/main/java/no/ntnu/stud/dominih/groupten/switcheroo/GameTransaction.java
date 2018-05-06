package no.ntnu.stud.dominih.groupten.switcheroo;

// Model class, this is why "cosmetic" warnings are supressed
@SuppressWarnings("WeakerAccess")

/**
 * Please refer to the report for a detailed description of the protocol and the meaning of the
 * TYPE constants.
 *
 * @see Game
 * @author Dominik Huber
 */
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
