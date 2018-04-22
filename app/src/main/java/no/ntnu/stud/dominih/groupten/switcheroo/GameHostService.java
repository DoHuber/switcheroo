package no.ntnu.stud.dominih.groupten.switcheroo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

    public void getRegisteredPlayers(final AsyncCallback<String> callback) {

        DatabaseReference gameReference = FirebaseDatabase.getInstance().getReference("switcheroo").child(gameId);
        DatabaseReference playersReference = gameReference.child("players");
        playersReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> result = new ArrayList<>();

                for (DataSnapshot element : dataSnapshot.getChildren()) {

                    if (element.getValue() != null) {

                        String boi = element.getValue().toString();
                        result.add(boi);

                    }

                }

                callback.onSuccess(result);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError.toException());
            }

        });

    }

}
