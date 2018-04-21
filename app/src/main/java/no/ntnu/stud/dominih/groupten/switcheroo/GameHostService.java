package no.ntnu.stud.dominih.groupten.switcheroo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameHostService {

    // private GenericDatabaseService<String> stringDbService = new GenericDatabaseService<>("switcheroo", String.class);
    private String gameId;

    public GameHostService() {

        // stringDbService.init();

    }

    public String startNewGame() {

        DatabaseReference newGame = FirebaseDatabase.getInstance().getReference("switcheroo").push();
        gameId = newGame.getKey();
        newGame.setValue(new Game(gameId));

        return gameId;

    }

    public String getGameId() {
        return gameId;
    }

}
