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

    private GameClientService gameClientService;
    private GameHostService gameHostService;
    private List<String> players;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {

            String role = arguments.getString(GameActivity.KEY_PLAYER_TYPE);
            String gameId = arguments.getString(GameActivity.KEY_GAME_ID);

            if (GameActivity.PLAYER_TYPE_CLIENT.equals(role)) {

                gameClientService = new GameClientService(gameId);
                gameClientService.subscribeForTransactions(new GameTransactionCallback());

                doFragmentTransaction(new WaitingFragment());

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

    private void doFragmentTransaction(Fragment f) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, f)
                .commit();

    }

    private void onPlayerListReceived(List<String> players) {

        players.add(MainActivity.mockupUsername);
        this.players = players;

        doFragmentTransaction(new DrawFragment());

    }

    public void finishedDrawing(String imageBase64) {

        doFragmentTransaction(new WaitingFragment());
        GameTransaction transaction = new GameTransaction(players.get(0),
                GameTransaction.TYPE_IMG, imageBase64, players.get(1));
        gameHostService.sendGameTransaction(transaction);

    }

    public void finishedWriting(String text) {




    }

    private void receivedTransaction(GameTransaction transaction) {

        if (transaction.recipientId.equals(MainActivity.mockupUsername)) {

            // This is my transaction, do something funny, like opening the text


        } else if (transaction.recipientId.equals("all")) {

            // The game has ended, probably.

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
