package no.ntnu.stud.dominih.groupten.switcheroo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import no.ntnu.stud.dominih.groupten.switcheroo.fragments.ClientEndFragment;
import no.ntnu.stud.dominih.groupten.switcheroo.fragments.DrawingFragment;
import no.ntnu.stud.dominih.groupten.switcheroo.fragments.HostEndFragment;
import no.ntnu.stud.dominih.groupten.switcheroo.fragments.WaitingFragment;
import no.ntnu.stud.dominih.groupten.switcheroo.fragments.WritingFragment;

public class GameActivity extends AppCompatActivity {

    public static final String KEY_PLAYER_TYPE = "Another key sadad";
    public static final String KEY_GAME_ID = "Omann Lemon ooer";
    public static final String PLAYER_TYPE_CLIENT = "Client";
    public static final String PLAYER_TYPE_HOST = "Host";

    private final WaitingFragment waitingFragment = new WaitingFragment();

    private GameClientService gameClientService;
    private List<String> players = new ArrayList<>();

    private String ownRole;
    private String cachedPayload;
    private String cacheType;
    private String cachedSenderId;

    private List<GameTransaction> transactionCache = new ArrayList<>();


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

                GameHostService gameHostService = new GameHostService(gameId);
                gameClientService = new GameClientService(gameId);

                gameClientService.subscribeForTransactions(new GameTransactionCallback());

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

        this.players.add(MainActivity.userId);
        this.players.addAll(players);

        DrawingFragment drawingFragment = new DrawingFragment();
        Bundle arguments = new Bundle();
        arguments.putBoolean(DrawingFragment.KEY_HAS_TEXT, false);
        drawingFragment.setArguments(arguments);

        replaceFragmentWith(drawingFragment);

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

        Log.d("GameActivity", "Transcation received: " + transaction.toString());

        if (ownRole.equals(PLAYER_TYPE_HOST)) {

            if (transaction.type.equals(GameTransaction.TYPE_IMG) || transaction.type.equals(GameTransaction.TYPE_TEXT)) {

                transactionCache.add(transaction);

            }

        }

        if (transaction.recipientId.equals(MainActivity.userId)) {

            // This is my transaction, do something funny, like opening the text
            String transactionType = transaction.type;
            switch (transactionType) {
                case GameTransaction.TYPE_NEXT:

                    onNextPlayerReceived(transaction.payload);

                    break;
                case GameTransaction.TYPE_IMG: {

                    Bundle arguments = new Bundle();
                    arguments.putString(WritingFragment.KEY_ENCODED_IMAGE, transaction.payload);

                    WritingFragment writingFragment = new WritingFragment();
                    writingFragment.setArguments(arguments);

                    replaceFragmentWith(writingFragment);


                    break;
                }
                case GameTransaction.TYPE_TEXT: {

                    Bundle arguments = new Bundle();
                    arguments.putBoolean(DrawingFragment.KEY_HAS_TEXT, true);
                    arguments.putString(DrawingFragment.KEY_TEXT_CAPTION, transaction.payload);

                    DrawingFragment drawingFragment = new DrawingFragment();
                    drawingFragment.setArguments(arguments);

                    replaceFragmentWith(drawingFragment);

                    break;
                }
            }

        } else if (transaction.recipientId.equals("broadcast") && transaction.type.equals(GameTransaction.TYPE_END)) {

            ClientEndFragment cef = new ClientEndFragment();
            Bundle arguments = new Bundle();
            arguments.putString("full-image", transaction.payload);
            cef.setArguments(arguments);

            replaceFragmentWith(cef);

        } else if (transaction.recipientId.equals("host") && ownRole.equals(PLAYER_TYPE_HOST)) {

            String senderId = transaction.senderId;
            int sendersPosition = players.indexOf(senderId);

            if (sendersPosition == (players.size() - 1)) {

                cachedSenderId = senderId;
                replaceFragmentWith(new HostEndFragment());

            } else {

                GameTransaction t = new GameTransaction(senderId, GameTransaction.TYPE_NEXT, players.get(sendersPosition + 1), MainActivity.userId);
                gameClientService.sendGameTransaction(t);

            }

        }

        if (ownRole.equals(PLAYER_TYPE_HOST)) {

            if (transaction.type.equals(GameTransaction.TYPE_IMG) || transaction.type.equals(GameTransaction.TYPE_TEXT)) {

                transactionCache.add(transaction);

            }

        }

    }

    public void continueGame() {

        replaceFragmentWith(waitingFragment);
        GameTransaction t = new GameTransaction(cachedSenderId, GameTransaction.TYPE_NEXT, players.get(0), MainActivity.userId);
        gameClientService.sendGameTransaction(t);

    }

    public void endGame() {

        Bitmap fullImage = new ImageExporter().exportToBitmap(transactionCache);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        fullImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        String imgString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        GameTransaction t = new GameTransaction("broadcast", GameTransaction.TYPE_END, imgString, MainActivity.userId);
        gameClientService.sendGameTransaction(t);

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
