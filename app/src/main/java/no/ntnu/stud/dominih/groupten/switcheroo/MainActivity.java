package no.ntnu.stud.dominih.groupten.switcheroo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import no.ntnu.stud.dominih.groupten.switcheroo.fragments.MainMenuFragment;

/**
 * MainActivity of this application. This app is structured as follows: both the MainActivity and
 * the GameActivity only contain a container layout for Fragments. Depending on what the app wants
 * to do at a given point, different fragments are put in the container. This greatly helped
 * in modularizing the app both logically and during the development process.
 *
 * The MainActivity handles all tasks that are related to hosting or joining a game, up until
 * the game actually starts, at which point the GameActivity takes over.
 *
 * @author Dominik Huber
 * @see GameActivity
 *
 */
public class MainActivity extends AppCompatActivity {

    private static final int    CAMERA_PERMISSION_REQUEST_CODE = 7734;
    public static boolean       areCameraPermissionsGranted = false;
    public static String        userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ensureUserAuthentication();

    }

    private void ensureUserAuthentication() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser candidateUser = auth.getCurrentUser();

        if (candidateUser != null) {

            setUsername(candidateUser);
            checkCameraPermissions();

        } else {

            signInAnonymously(auth);

        }

    }

    /**
     * Tries to sign in the user anonymously, ensuring that users so signed in can access the
     * database and by extension, the application.
     *
     * Source: this code is heavily based on:
     * https://firebase.google.com/docs/auth/android/anonymous-auth
     *
     * @param auth an instance of FirebaseAuth
     */
    private void signInAnonymously(FirebaseAuth auth) {
        auth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            setUsername(task.getResult().getUser());
                            checkCameraPermissions();

                        } else {

                            Toast.makeText(MainActivity.this, "Fatal error: Authentication failure. Please try again.", Toast.LENGTH_LONG).show();
                            MainActivity.this.finish();

                        }

                    }
                });
    }

    private void setUsername(FirebaseUser user) {

        MainActivity.userId = user.getUid();

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

            // If the result is for camera permissions
            // and there are one or more results
            // and the first result is positive
            // set the appropriate flag to true,
            // this will later make the app use the camera or text fallback Fragment

            areCameraPermissionsGranted = requestCode == CAMERA_PERMISSION_REQUEST_CODE
                    && grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;

            showMainMenuFragment();


    }
}
