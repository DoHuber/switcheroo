package no.ntnu.stud.dominih.groupten.switcheroo;

@SuppressWarnings("WeakerAccess")
public class GameTransaction {

    public static final String TYPE_TEXT = "yg3T8ZVASCfR3RrVsJEU";
    public static final String TYPE_IMG = "c7DqWBBDzQOuRegsYQpJ";
    public static final String TYPE_END = "ipuSZKMHfb3YYcY92eYs";
    public static final String TYPE_DONE = "paQx4p5YbDFlPkNCWJSr";
    public static final String TYPE_NEXT = "5IxagGhVlAuARY3dCM4m";

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
