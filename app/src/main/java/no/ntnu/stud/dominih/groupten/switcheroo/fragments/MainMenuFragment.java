package no.ntnu.stud.dominih.groupten.switcheroo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import no.ntnu.stud.dominih.groupten.switcheroo.GameHostService;
import no.ntnu.stud.dominih.groupten.switcheroo.MainActivity;
import no.ntnu.stud.dominih.groupten.switcheroo.R;

public class MainMenuFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_mainmenu, container, false);

        Button joinButton = v.findViewById(R.id.join_button);
        Button hostButton = v.findViewById(R.id.host_button);

        setupJoinButton(joinButton);
        setupHostButton(hostButton);

        return v;

    }

    private void setupHostButton(Button hostButton) {
        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GameHostService hostService = new GameHostService();
                String id = hostService.startNewGame();

                Bundle args = new Bundle();
                args.putString(DisplayQRFragment.KEY_GAME_ID_GENFRAGMENT, id);
                DisplayQRFragment displayFragment = new DisplayQRFragment();
                displayFragment.setArguments(args);

                doFragmentTransaction(displayFragment);

            }
        });
    }

    private void setupJoinButton(Button joinButton) {
        joinButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               if (MainActivity.areCameraPermissionsGranted) {

                   doFragmentTransaction(new QRScanFragment());

               } else {

                   doFragmentTransaction(new TextJoinFragment());

               }

            }

        });
    }

    private void doFragmentTransaction(Fragment fragment) {

        FragmentManager fm = getFragmentManager();
        if (fm != null) {

            fm
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, fragment)
                    .addToBackStack("")
                    .commit();

        }

    }

}
