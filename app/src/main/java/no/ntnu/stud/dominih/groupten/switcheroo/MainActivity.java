package no.ntnu.stud.dominih.groupten.switcheroo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import no.ntnu.stud.dominih.groupten.switcheroo.fragments.MainMenuFragment;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 7734;
    public static boolean areCameraPermissionsGranted = false;
    public static String userId = "";

    private static final String KEY_PREFERENCES = "no.ntnu.stud.dominih.groupten.switcheroo.preferencekey";
    private static final String KEY_USERNAME = "no.ntnu.stud.dominih.groupten.switcheroo.preferencekey.username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private String getUsername() {

        SharedPreferences preferences = getSharedPreferences(KEY_PREFERENCES, MODE_PRIVATE);
        return preferences.getString(KEY_USERNAME, "");

    }

    private void setUsername(String username) {

        MainActivity.userId = username;

        SharedPreferences preferences = getSharedPreferences(KEY_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USERNAME, username)
                .apply();

    }

    @Override
    protected void onStart() {
        super.onStart();

        performStartupChecks();

    }

    private void performStartupChecks() {

        if (getUsername().equals("")) {

            askForUsername();

        } else {

            userId = getUsername();
            checkCameraPermissions();

        }

    }

    private void checkCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);

        } else {

            areCameraPermissionsGranted = true;
            showMainMenuFragment();

        }
    }

    private void askForUsername() {

        final EditText usernameField = new EditText(this);
        usernameField.setHint("New username");

        AlertDialog.Builder bob = new AlertDialog.Builder(this);
        bob.setTitle("No username found")
                .setView(usernameField)
                .setPositiveButton("OK", null);

        final AlertDialog dialog = bob.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String text = usernameField.getText().toString();
                if (text.length() > 15 || text.contains("host") || text.contains("broadcast")) {

                    usernameField.setError("Too long or forbidden name");

                } else {

                    setUsername(text);
                    dialog.dismiss();
                    checkCameraPermissions();

                }

            }
        });

    }

    private void showMainMenuFragment() {
        MainMenuFragment menuFragment = new MainMenuFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, menuFragment)
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {

            areCameraPermissionsGranted = requestCode == CAMERA_PERMISSION_REQUEST_CODE
                    && grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;

            showMainMenuFragment();


    }
}
