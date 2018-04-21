package no.ntnu.stud.dominih.groupten.switcheroo.fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import no.ntnu.stud.dominih.groupten.switcheroo.R;

public class DisplayQRFragment extends Fragment {

    public static final String KEY_GAME_ID_GENFRAGMENT = "SOmkey for barcodes";
    private ImageView qrCodeDisplay;
    private String gameId = "ERROR";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_displayqr, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            gameId = arguments.getString(KEY_GAME_ID_GENFRAGMENT);
        }

        qrCodeDisplay = v.findViewById(R.id.qr_code_display);

        Button startButton = v.findViewById(R.id.start_game_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GameFragment gf = new GameFragment();
                Bundle args = new Bundle();
                args.putString(GameFragment.KEY_GAME_ID, gameId);
                args.putString(GameFragment.KEY_PLAYER_TYPE, GameFragment.PLAYER_TYPE_HOST);

                gf.setArguments(args);
                doFragmentTransaction(gf);

            }
        });

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();

        QRCodeWriter writer = new QRCodeWriter();
        try {

            BitMatrix bitMatrix = writer.encode(gameId, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            qrCodeDisplay.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
            // TODO Implement text-based fallback if QR-Code-generation fails
        }

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

