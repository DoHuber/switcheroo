package no.ntnu.stud.dominih.groupten.switcheroo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import no.ntnu.stud.dominih.groupten.switcheroo.GameActivity;
import no.ntnu.stud.dominih.groupten.switcheroo.R;

public class WaitingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View waitingView = inflater.inflate(R.layout.fragment_waiting, container, false);

        Button quitButton = waitingView.findViewById(R.id.waiting_abort);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    ((GameActivity) getActivity()).backToMainMenu();
                }
            }
        });

        return waitingView;

    }

}
