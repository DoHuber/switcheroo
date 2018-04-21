package no.ntnu.stud.dominih.groupten.switcheroo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import no.ntnu.stud.dominih.groupten.switcheroo.AsyncCallback;
import no.ntnu.stud.dominih.groupten.switcheroo.GameClientService;
import no.ntnu.stud.dominih.groupten.switcheroo.GameHostService;
import no.ntnu.stud.dominih.groupten.switcheroo.GameTransaction;
import no.ntnu.stud.dominih.groupten.switcheroo.R;

public class GameFragment extends Fragment {

    public static final String KEY_PLAYER_TYPE = "Another key sadad";
    public static final String KEY_GAME_ID = "Omann Lemon ooer";

    public static final String PLAYER_TYPE_CLIENT = "Client";
    public static final String PLAYER_TYPE_HOST = "Host";

    private GameClientService gameClientService;
    private GameHostService gameHostService;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {

            String role = arguments.getString(KEY_PLAYER_TYPE);
            String gameId = arguments.getString(KEY_GAME_ID);

            if (PLAYER_TYPE_CLIENT.equals(role)) {

                gameClientService = new GameClientService(gameId);
                gameClientService.subscribeForTransactions(new GameTransactionCallback());

            } else {

                gameHostService = new GameHostService(gameId);

                // Basically do an ICMP ping
                gameHostService.sendGameTransaction(new GameTransaction("noone", GameTransaction.TYPE_TEXT, "Test hello world", "noone"));
                gameHostService.sendGameTransaction(new GameTransaction("noone", GameTransaction.TYPE_TEXT, "Test hello world", "noone"));
                gameHostService.sendGameTransaction(new GameTransaction("noone", GameTransaction.TYPE_TEXT, "Test hello world", "noone"));
                gameHostService.sendGameTransaction(new GameTransaction("noone", GameTransaction.TYPE_TEXT, "Test hello world", "noone"));
            }

        }

        return inflater.inflate(R.layout.fragment_game, container, false);

    }

    private static class GameTransactionCallback implements AsyncCallback<GameTransaction> {

        @Override
        public void onSuccess(List<GameTransaction> result) {

            for (GameTransaction element : result) {

                Log.d("GameFragment", "Received transaction: " + element.toString());

            }

        }

        @Override
        public void onFailure(Exception cause) {
                // Ignored
        }

    }
}
