package no.ntnu.stud.dominih.groupten.switcheroo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import no.ntnu.stud.dominih.groupten.switcheroo.GameActivity;
import no.ntnu.stud.dominih.groupten.switcheroo.R;

public class EndOfGameFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View endGameView = inflater.inflate(R.layout.fragment_endofgame, container, false);
        final GameActivity parent = (GameActivity) getActivity();

        Button continueButton = endGameView.findViewById(R.id.endgame_button_continue);
        Button mainMenuButton = endGameView.findViewById(R.id.endgame_button_main_menu);
        Button exportButton = endGameView.findViewById(R.id.endgame_button_export);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (parent != null) {

                    parent.continueGame();

                }

            }
        });

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (parent != null) {

                    parent.endGame();
                    parent.finish();

                }

            }
        });

        exportButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),"Not implemented yet.", Toast.LENGTH_LONG).show();
                // TODO Implement the export and open a file chooser for sending the picture.
                // Then send the image as whole to the other players.

            }

        });

        return endGameView;

    }

}
