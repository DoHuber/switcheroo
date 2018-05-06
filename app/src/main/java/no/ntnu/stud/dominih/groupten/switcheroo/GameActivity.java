package no.ntnu.stud.dominih.groupten.switcheroo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import no.ntnu.stud.dominih.groupten.switcheroo.fragments.ClientEndFragment;
import no.ntnu.stud.dominih.groupten.switcheroo.fragments.DrawingFragment;
import no.ntnu.stud.dominih.groupten.switcheroo.fragments.HostEndFragment;
import no.ntnu.stud.dominih.groupten.switcheroo.fragments.WaitingFragment;
import no.ntnu.stud.dominih.groupten.switcheroo.fragments.WritingFragment;

/**
 * Second Activity of the application. This handles the actual game, going from the host starting
 * the game to the host ending the game, or the player ending it manually, whatever comes earlier.
 *
 * This Activity contains both the host and client logic, in order to enable users to be both
 * host and client at the same time. The game is controlled primarily by GameTranscation objects,
 * sent via Firebase. The host always has first turn and will draw. The GameTransactions follow
 * an asynchronous, stateless protocol, exact documentation can be found in the report.
 *
 * The Activity will switch around the correct fragments depending on what it is doing at the
 * moment.
 *
 * @see MainActivity
 * @see Game
 * @see GameTransaction
 *
 * @author Dominik Huber
 *
 */
public class GameActivity extends AppCompatActivity {

    // Constants
    public static final String KEY_PLAYER_TYPE = "Another key sadad";
    public static final String KEY_GAME_ID = "Omann Lemon ooer";
    public static final String PLAYER_TYPE_CLIENT = "Client";
    public static final String PLAYER_TYPE_HOST = "Host";

    // A fragment that just displays a loading bar / the circle indicator.
    private final WaitingFragment waitingFragment = new WaitingFragment();

    private GameClientService gameClientService;
    private final List<String> players = new ArrayList<>();

    private String ownRole;
    private String cachedPayload;
    private String cacheType;
    private String cachedSenderId;

    private final List<GameTransaction> transactionCache = new ArrayList<>();
    private boolean gameEnding = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Keep the screen on as game status is lost if the Activity stops - see future work for that
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {

            processArguments(arguments);

        }


    }

    private void processArguments(Bundle arguments) {

        String role = arguments.getString(GameActivity.KEY_PLAYER_TYPE);
        ownRole = role;
        String gameId = arguments.getString(GameActivity.KEY_GAME_ID);

        if (GameActivity.PLAYER_TYPE_CLIENT.equals(role)) {

            subscribeAndWait(gameId);

        } else {

            getPlayerList(gameId);

        }
    }

    private void subscribeAndWait(String gameId) {

        gameClientService = new GameClientService(gameId);
        gameClientService.subscribeForTransactions(new GameTransactionCallback());

        replaceFragmentWith(waitingFragment);

    }

    private void getPlayerList(String gameId) {

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

    private void replaceFragmentWith(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .commit();

    }

    /**
     * As soon as the player list has been received from the Firebase database, the host will
     * add himself and the other players to the local player list and then start the game by
     * opening the DrawingFragment for himself.
     *
     * @param players A list of player unique usernames
     */
    private void onPlayerListReceived(List<String> players) {

        this.players.add(MainActivity.userId);
        this.players.addAll(players);

        DrawingFragment drawingFragment = new DrawingFragment();
        Bundle arguments = new Bundle();

        // Since this is the first round, the Fragment does not get a caption to draw
        arguments.putBoolean(DrawingFragment.KEY_HAS_TEXT, false);
        drawingFragment.setArguments(arguments);

        replaceFragmentWith(drawingFragment);

    }

    /**
     * This is called by the DrawingFragment once the player has finished drawing. This will cause
     * the client to send a GameTransaction indicating that they are done to the host, thereby
     * requesting the username of the next player, to whom the image will be sent.
     *
     * @param imageBase64 An images bytes encoded in Base64
     */
    public void finishedDrawing(String imageBase64) {

        replaceFragmentWith(waitingFragment);

        cachedPayload = imageBase64;
        cacheType = GameTransaction.TYPE_IMG;

        GameTransaction t = new GameTransaction("host", GameTransaction.TYPE_DONE, "", MainActivity.userId);
        gameClientService.sendGameTransaction(t);

    }

    /**
     * Called by the WritingFragment once the player has finished describing the image. This will
     * send a GameTransaction to the host asking for the next players username.
     *
     * @param text String with the image description
     */
    public void finishedWriting(String text) {

        replaceFragmentWith(waitingFragment);

        cachedPayload = text;
        cacheType = GameTransaction.TYPE_TEXT;

        GameTransaction t = new GameTransaction("host", GameTransaction.TYPE_DONE, "", MainActivity.userId);
        gameClientService.sendGameTransaction(t);

    }

    /**
     * Once the next players id has been received from the host, the cached transaction is sent
     * to the next player for continuing the game.
     *
     * @param nextPlayerId Unique username of player whose turn it is next
     */
    private void onNextPlayerReceived(String nextPlayerId) {

        GameTransaction t = new GameTransaction(nextPlayerId, cacheType, cachedPayload, MainActivity.userId);
        gameClientService.sendGameTransaction(t);

    }

    /**
     * Master method for handling all GameTransactions, see the report for more detailed protocol
     * documentation.
     *
     * @param transaction The received GameTransaction
     */
    private void receivedTransaction(GameTransaction transaction) {

        Log.d("GameActivity", "Transcation received: " + transaction.toString());

        if (ownRole.equals(PLAYER_TYPE_HOST)) {

            if (transaction.type.equals(GameTransaction.TYPE_IMG) || transaction.type.equals(GameTransaction.TYPE_TEXT)) {

                // First of all, the host logs any and all image or text transactions for creating
                // the big image at the end of the game
                transactionCache.add(transaction);

            }

        }

        // ---------------------------------------------------------------------------------------//

        if (transaction.recipientId.equals(MainActivity.userId)) {

            // This is my transaction, do something funny, like opening the text
            String transactionType = transaction.type;
            switch (transactionType) {

                // Next player's turn, call the method
                case GameTransaction.TYPE_NEXT: {

                    onNextPlayerReceived(transaction.payload);

                    break;
                }

                // Image transaction, open the WritingFragment for describing the image
                case GameTransaction.TYPE_IMG: {

                    Bundle arguments = new Bundle();
                    arguments.putString(WritingFragment.KEY_ENCODED_IMAGE, transaction.payload);

                    WritingFragment writingFragment = new WritingFragment();
                    writingFragment.setArguments(arguments);

                    replaceFragmentWith(writingFragment);

                    break;
                }

                // Text transaction, open the DrawingFragment for drawing the text
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


        }
        // Brodacast transaction, type END. Game has ended, the payload is the full image, open
        // the final fragment for displaying and sharing the image.
        else if (transaction.recipientId.equals("broadcast") && transaction.type.equals(GameTransaction.TYPE_END)) {

            ClientEndFragment cef = new ClientEndFragment();
            Bundle arguments = new Bundle();
            arguments.putString("full-image", transaction.payload);
            cef.setArguments(arguments);

            replaceFragmentWith(cef);

        }
        // Transaction for the host, this user is the host. Handle various host duties.
        else if (transaction.recipientId.equals("host") && ownRole.equals(PLAYER_TYPE_HOST)) {

            String senderId = transaction.senderId;
            int sendersPosition = players.indexOf(senderId);

            if (sendersPosition == (players.size() - 1)) {

                cachedSenderId = senderId;
                replaceFragmentWith(new HostEndFragment());

            } else {

                GameTransaction t = new GameTransaction(senderId, GameTransaction.TYPE_NEXT, players.get(sendersPosition + 1), MainActivity.userId);
                gameClientService.sendGameTransaction(t);

            }

            if (gameEnding) {

                finalizeTheEnd();

            }

        }

    }

    public void continueGame() {

        replaceFragmentWith(waitingFragment);
        GameTransaction t = new GameTransaction(cachedSenderId, GameTransaction.TYPE_NEXT, players.get(0), MainActivity.userId);
        gameClientService.sendGameTransaction(t);

    }

    public void endGame() {

        gameEnding = true;
        GameTransaction t = new GameTransaction(cachedSenderId, GameTransaction.TYPE_NEXT, "host", MainActivity.userId);
        gameClientService.sendGameTransaction(t);

    }

    private void finalizeTheEnd() {

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

    public void backToMainMenu() {

        this.finish();

    }

}
