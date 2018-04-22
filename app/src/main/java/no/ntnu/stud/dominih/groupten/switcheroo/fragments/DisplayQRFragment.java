package no.ntnu.stud.dominih.groupten.switcheroo.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import no.ntnu.stud.dominih.groupten.switcheroo.GameActivity;
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

                Bundle extras = new Bundle();
                extras.putString(GameActivity.KEY_GAME_ID, gameId);
                extras.putString(GameActivity.KEY_PLAYER_TYPE, GameActivity.PLAYER_TYPE_HOST);

                Intent i = new Intent(getActivity(), GameActivity.class);
                i.putExtras(extras);

                startActivity(i);

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
            qrCodeDisplay.setImageResource(R.drawable.error);
        }

    }

}

