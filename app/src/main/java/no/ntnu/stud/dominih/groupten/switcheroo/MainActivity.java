package no.ntnu.stud.dominih.groupten.switcheroo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import no.ntnu.stud.dominih.groupten.switcheroo.fragments.MainMenuFragment;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 7734;
    public static boolean cameraPermissionsGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);

        } else {

            gotoMainMenu();

        }

    }

    private void gotoMainMenu() {
        MainMenuFragment menuFragment = new MainMenuFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, menuFragment)
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {

            cameraPermissionsGranted = requestCode == CAMERA_PERMISSION_REQUEST_CODE
                    && grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;

            gotoMainMenu();


    }
}
