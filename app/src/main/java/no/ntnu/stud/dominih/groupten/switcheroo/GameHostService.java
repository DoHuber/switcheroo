package no.ntnu.stud.dominih.groupten.switcheroo;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GameHostService {

    private String gameId;
    private static final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000;

    public GameHostService() {

    }

    public GameHostService(String gameId) {
        this.gameId = gameId;
    }

    public String startNewGame() {

        databaseMaintenance();

        DatabaseReference newGame = FirebaseDatabase.getInstance().getReference("switcheroo").push();
        gameId = newGame.getKey();
        newGame.setValue(new Game(gameId));

        return gameId;

    }

    public void databaseMaintenance() {

        final DatabaseReference lastCleanedMillis = FirebaseDatabase.getInstance().getReference("last-cleaned");
        lastCleanedMillis.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long lastCleaned = makeLongFromSnapshot(dataSnapshot);
                final long cutoffTime = System.currentTimeMillis() - ONE_DAY_MILLIS;

                if (lastCleaned < cutoffTime) {

                    FirebaseDatabase.getInstance().getReference("switcheroo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot element : dataSnapshot.getChildren()) {

                                Game game = makeGameFromSnapshot(element);
                                if (Long.parseLong(game.lastUpdate, 16) < cutoffTime && !game.getGameid().equals("")) {

                                    element.getRef().removeValue();

                                }

                            }

                            lastCleanedMillis.setValue(System.currentTimeMillis());

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("GameHostService", "Something went wrong: " + databaseError.getMessage());
                        }
                    });


                }

            }

            private Game makeGameFromSnapshot(DataSnapshot snapshot) {

                try {

                    return snapshot.getValue(Game.class);

                } catch (DatabaseException e) {

                    Log.e("GameHostService", "DB-Exception: " + e.getLocalizedMessage());
                    snapshot.getRef().removeValue();
                    return new Game("");

                }

            }

            private long makeLongFromSnapshot(DataSnapshot snapshot) {

                try {

                    Object value = snapshot.getValue();
                    if (value != null) {

                        return Long.parseLong(value.toString());

                    }

                } catch (Exception e) {
                    Log.e("GameHostService", "An interesting exception came up: " + e.getLocalizedMessage());
                }

                return 0L;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("GameHostService", "Something went wrong: " + databaseError.getMessage());
            }
        });


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
