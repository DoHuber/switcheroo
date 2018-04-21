package no.ntnu.stud.dominih.groupten.switcheroo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameClientService {

    private String gameId;

    public void joinGame(String gameId) {

        this.gameId = gameId;
        DatabaseReference gameReference = FirebaseDatabase.getInstance().getReference("switcheroo").child(gameId);
        DatabaseReference newPlayer = gameReference.child("players").push();
        newPlayer.setValue(MockupValues.USER_NAME_MOCKUP);

    }


}
