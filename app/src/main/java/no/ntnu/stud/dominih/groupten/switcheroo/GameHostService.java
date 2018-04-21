package no.ntnu.stud.dominih.groupten.switcheroo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameHostService {

    private String gameId;

    public GameHostService() {

    }

    public GameHostService(String gameId) {
        this.gameId = gameId;
    }

    public String startNewGame() {

        DatabaseReference newGame = FirebaseDatabase.getInstance().getReference("switcheroo").push();
        gameId = newGame.getKey();
        newGame.setValue(new Game(gameId));

        return gameId;

    }

    public void sendGameTransaction(GameTransaction transaction) {

        DatabaseReference gameReference = FirebaseDatabase.getInstance().getReference("switcheroo").child(gameId);
        DatabaseReference newLogChild = gameReference.child("log").push();

        newLogChild.setValue(transaction);

    }

    public String getGameId() {
        return gameId;
    }

}
