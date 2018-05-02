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

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 7734;
    public static boolean areCameraPermissionsGranted = false;
    public static String userId = "";

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

            areCameraPermissionsGranted = requestCode == CAMERA_PERMISSION_REQUEST_CODE
                    && grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;

            showMainMenuFragment();


    }
}
