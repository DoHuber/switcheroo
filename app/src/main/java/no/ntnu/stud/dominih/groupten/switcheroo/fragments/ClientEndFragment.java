package no.ntnu.stud.dominih.groupten.switcheroo.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import no.ntnu.stud.dominih.groupten.switcheroo.R;

public class ClientEndFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_clientend, container, false);

        Button mainMenu = v.findViewById(R.id.clientend_button_main_menu);
        Button exportButton = v.findViewById(R.id.clientend_button_export);

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity() != null) {

                    getActivity().finish();

                }

            }
        });

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),"Not implemented yet.", Toast.LENGTH_LONG).show();
                // TODO Implement the clientside-export and open a file chooser for sending the picture.
                // Then send the image as whole to the other players.

            }
        });

        ImageView fullImageView = v.findViewById(R.id.clientend_imageview);
        Bundle args = getArguments();

        if (args != null) {

            String base64 = args.getString("full-image");
            byte[] imageBytes = Base64.decode(base64, Base64.DEFAULT);
            Bitmap fullImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            fullImageView.setImageBitmap(fullImage);

        }

        return v;

    }

}
