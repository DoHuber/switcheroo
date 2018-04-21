package no.ntnu.stud.dominih.groupten.switcheroo.fragments;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import no.ntnu.stud.dominih.groupten.switcheroo.GameClientService;
import no.ntnu.stud.dominih.groupten.switcheroo.R;

public class QRScanFragment extends Fragment implements QRCodeReaderView.OnQRCodeReadListener {

    private QRCodeReaderView qrCodeReaderView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_qrscan, container, false);

        qrCodeReaderView = v.findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setBackCamera();
        qrCodeReaderView.setTorchEnabled(false);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d("QRScanFragment", "Reached onStart() Method");


    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        qrCodeReaderView.setOnQRCodeReadListener(null);
        new GameClientService().joinGame(text);

        GameFragment gf = new GameFragment();
        Bundle args = new Bundle();
        args.putString(GameFragment.KEY_GAME_ID, text);
        args.putString(GameFragment.KEY_PLAYER_TYPE, GameFragment.PLAYER_TYPE_CLIENT);

        gf.setArguments(args);
        doFragmentTransaction(gf);

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
