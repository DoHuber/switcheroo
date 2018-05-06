package no.ntnu.stud.dominih.groupten.switcheroo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles and abstracts all game-related tasks on the client side. Note that the host also
 * has a instance of this class, as a host is always also a client, but not vice versa.
 *
 * @see GameHostService
 * @see GameActivity
 *
 * @author Dominik Huber
 *
 */
public class GameClientService {

    private String gameId;

    public GameClientService() {}

    public GameClientService(String gameId) {
        this.gameId = gameId;
    }

    /**
     * Tries to join the game as specified by the gameId String
     *
     * @param gameId id of the game to join
     */
    public void joinGame(String gameId) {

        this.gameId = gameId;
        DatabaseReference gameReference = FirebaseDatabase.getInstance().getReference("switcheroo").child(gameId);
        DatabaseReference newPlayer = gameReference.child("players").push();
        newPlayer.setValue(MainActivity.userId);

    }

    /**
     * Sends a GameTransaction that will reach all listening clients in the current game.
     *
     * @param transaction GameTransaction to broadcast
     */
    public void sendGameTransaction(GameTransaction transaction) {

        DatabaseReference gameReference = FirebaseDatabase.getInstance().getReference("switcheroo").child(gameId);
        DatabaseReference newLogChild = gameReference.child("log").push();

        newLogChild.setValue(transaction);

    }

    /**
     * Subscribes to transactions for the game that has been joined. This will (obviously) not
     * work if no game has been joined, by simply never receiving any transactions.
     *
     * @param callback AsyncCallback that handles incoming Transactions
     */
    public void subscribeForTransactions(final AsyncCallback<GameTransaction> callback) {

        DatabaseReference gameReference = FirebaseDatabase.getInstance().getReference("switcheroo").child(gameId);
        gameReference.child("log").addChildEventListener(new GameTransactionListener(callback));
    }

    /**
     * Helper (inner) class that makes GameTransactions from DataSnapshots, handles failures, etc.
     * Needs an AsyncCallback to notify with anything that's happening.
     *
     */
    private static class GameTransactionListener extends AbstractChildEventListener {

        private final AsyncCallback<GameTransaction> callback;

        public GameTransactionListener(AsyncCallback<GameTransaction> callback) {
            this.callback = callback;
        }

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
    }
}
