package no.ntnu.stud.dominih.groupten.switcheroo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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

                Toast.makeText(getContext(), "Not implemented yet.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setupJoinButton(Button joinButton) {
        joinButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d("MainMenuFrag", "The camera permissions currently are: " + Boolean.toString(MainActivity.cameraPermissionsGranted));

                FragmentManager fm = getFragmentManager();
                if (fm != null) {

                    // TODO Add some checks for the camera permission,
                    // if there is none, shell out a plain text fragment.

                    fm
                            .beginTransaction()
                            .replace(R.id.main_fragment_container, new QRScanFragment())
                            .addToBackStack("")
                            .commit();

                }

            }
        });
    }

}
