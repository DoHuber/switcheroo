package no.ntnu.stud.dominih.groupten.switcheroo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GameClientService {

    private String gameId;

    public GameClientService() {}

    public GameClientService(String gameId) {
        this.gameId = gameId;
    }

    public void joinGame(String gameId) {

        this.gameId = gameId;
        DatabaseReference gameReference = FirebaseDatabase.getInstance().getReference("switcheroo").child(gameId);
        DatabaseReference newPlayer = gameReference.child("players").push();
        newPlayer.setValue(MockupValues.USER_NAME_MOCKUP);

    }

    public void subscribeForTransactions(final AsyncCallback<GameTransaction> callback) {

        DatabaseReference gameReference = FirebaseDatabase.getInstance().getReference("switcheroo").child(gameId);
        gameReference.child("log").addChildEventListener(new AbstractChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                List<GameTransaction> result = new ArrayList<>();
                GameTransaction transaction = dataSnapshot.getValue(GameTransaction.class);
                result.add(transaction);

                callback.onSuccess(result);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                callback.onFailure(databaseError.toException());

            }
        });
    }


}
