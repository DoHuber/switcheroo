package no.ntnu.stud.dominih.groupten.switcheroo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import no.ntnu.stud.dominih.groupten.switcheroo.GameActivity;
import no.ntnu.stud.dominih.groupten.switcheroo.R;

public class TextJoinFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View textjoinView = inflater.inflate(R.layout.fragment_textjoin, container, false);

        final EditText textField = textjoinView.findViewById(R.id.textjoin_edittext);
        Button submitButton = textjoinView.findViewById(R.id.textjoin_submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String gameId = textField.getText().toString();
                launchGameActivity(gameId);

            }
        });

        return textjoinView;

    }

    private void launchGameActivity(String gameId) {
        Bundle extras = new Bundle();
        extras.putString(GameActivity.KEY_GAME_ID, gameId);
        extras.putString(GameActivity.KEY_PLAYER_TYPE, GameActivity.PLAYER_TYPE_CLIENT);

        Intent i = new Intent(getActivity(), GameActivity.class);
        i.putExtras(extras);

        startActivity(i);
    }

}
