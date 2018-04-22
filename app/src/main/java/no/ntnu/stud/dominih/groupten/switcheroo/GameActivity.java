package no.ntnu.stud.dominih.groupten.switcheroo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import no.ntnu.stud.dominih.groupten.switcheroo.fragments.WaitingFragment;

public class GameActivity extends AppCompatActivity {

    public static final String KEY_PLAYER_TYPE = "Another key sadad";
    public static final String KEY_GAME_ID = "Omann Lemon ooer";
    public static final String PLAYER_TYPE_CLIENT = "Client";
    public static final String PLAYER_TYPE_HOST = "Host";

    private WaitingFragment waitingFragment = new WaitingFragment();

    private GameClientService gameClientService;
    private GameHostService gameHostService;
    private List<String> players;

    private String ownRole;
    private String cachedPayload;
    private String cacheType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {

            String role = arguments.getString(GameActivity.KEY_PLAYER_TYPE);
            ownRole = role;
            String gameId = arguments.getString(GameActivity.KEY_GAME_ID);

            if (GameActivity.PLAYER_TYPE_CLIENT.equals(role)) {

                gameClientService = new GameClientService(gameId);
                gameClientService.subscribeForTransactions(new GameTransactionCallback());

                replaceFragmentWith(waitingFragment);

            } else {

                gameHostService = new GameHostService(gameId);
                gameClientService = new GameClientService(gameId);

                gameHostService.getRegisteredPlayers(new AsyncCallback<String>() {

                    @Override
                    public void onSuccess(List<String> result) {
                        onPlayerListReceived(result);
                    }

                    @Override
                    public void onFailure(Exception cause) {
                        Log.e("GameActivity", "Could not get players because of: " + cause.getLocalizedMessage());
                    }

                });

            }

        }


    }

    private void replaceFragmentWith(Fragment f) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, f)
                .commit();

    }

    private void onPlayerListReceived(List<String> players) {

        players.add(MainActivity.userId);
        this.players = players;

        replaceFragmentWith(new DrawFragment());

    }

    public void finishedDrawing(String imageBase64) {

        replaceFragmentWith(waitingFragment);

        cachedPayload = imageBase64;
        cacheType = GameTransaction.TYPE_IMG;

        GameTransaction t = new GameTransaction("host", GameTransaction.TYPE_DONE, "", MainActivity.userId);
        gameClientService.sendGameTransaction(t);

    }

    public void finishedWriting(String text) {

        replaceFragmentWith(waitingFragment);

        cachedPayload = text;
        cacheType = GameTransaction.TYPE_TEXT;

        GameTransaction t = new GameTransaction("host", GameTransaction.TYPE_DONE, "", MainActivity.userId);
        gameClientService.sendGameTransaction(t);

    }

    private void onNextPlayerReceived(String nextPlayerId) {

        GameTransaction t = new GameTransaction(nextPlayerId, cacheType, cachedPayload, MainActivity.userId);
        gameClientService.sendGameTransaction(t);

    }

    private void receivedTransaction(GameTransaction transaction) {

        if (transaction.recipientId.equals(MainActivity.userId)) {

            // This is my transaction, do something funny, like opening the text
            String transactionType = transaction.type;
            if (transactionType.equals(GameTransaction.TYPE_NEXT)) {

                onNextPlayerReceived(transaction.payload);

            } else if (transactionType.equals(GameTransaction.TYPE_IMG)) {

                // Open the text fragment with arguments, since this is "describe the image"

            } else if (transactionType.equals(GameTransaction.TYPE_TEXT)) {

                // Open the drawing fragment with arguments, since this is "draw the caption"

            }

        } else if (transaction.recipientId.equals("broadcast") && transaction.type.equals(GameTransaction.TYPE_END)) {

            // The payload is the stitched image: Display that in a ScrollView and give the
            // opportunity to save that.
            // See TODO below

        } else if (transaction.recipientId.equals("host") && ownRole.equals(PLAYER_TYPE_HOST)) {

            // Received transaction for the host, those are mostly requests for the next address
            // TODO Instead display a continue / export and end choice for the host.

            String senderId = transaction.senderId;
            int sendersPosition = players.indexOf(senderId);

            if (sendersPosition == (players.size() - 1)) {

                // Last player, end the game
                GameTransaction t = new GameTransaction("broadcast", GameTransaction.TYPE_END, "", MainActivity.userId);
                gameClientService.sendGameTransaction(t);

            } else {

                GameTransaction t = new GameTransaction(senderId, GameTransaction.TYPE_NEXT, players.get(sendersPosition + 1), MainActivity.userId);
                gameClientService.sendGameTransaction(t);

            }

        }

    }

    private class GameTransactionCallback implements AsyncCallback<GameTransaction> {

        @Override
        public void onSuccess(List<GameTransaction> result) {

            for (GameTransaction element : result) {

                receivedTransaction(element);

            }

        }

        @Override
        public void onFailure(Exception cause) {

            Log.e("GameActitivy", "Exception in transactions: " + cause.getLocalizedMessage());

        }

    }

}
