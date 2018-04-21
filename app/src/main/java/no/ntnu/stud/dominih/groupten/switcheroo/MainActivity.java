package no.ntnu.stud.dominih.groupten.switcheroo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;

import no.ntnu.stud.dominih.groupten.switcheroo.fragments.MainMenuFragment;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 7734;
    public static boolean cameraPermissionsGranted = false;
    public static final String mockupUsername = UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
     *
     * TO IMPLEMENT THE DRAWING FRAGMENT PUT THIS IN THE ON CREATE METHOD OF THE MAIN ACTIVITY
     *
     FragmentManager myFragmentManager;
     FragmentTransaction myFragmentTransaction;
     DrawFragment myDrawFragment;
      \@Override
     protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main);

     myFragmentManager = getFragmentManager();
     myFragmentTransaction = myFragmentManager.beginTransaction();
     myDrawFragment = DrawFragment.getInstance();
     myFragmentTransaction.add(R.id.fragment_container,myDrawFragment,"draw");
     myFragmentTransaction.commit();
     }
     *
     *
     *
     *
     AND ADD THIS TO THE MAINACTIVITYXML

     **/

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
    /*
     <?xml version="1.0" encoding="utf-8"?>
     <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
     android:id="@+id/fragment_container"
     android:layout_width="match_parent"
     android:layout_height="match_parent"/>
     *
     *
     *
     *
     *
     *
     *
     * ORIGINAL ONE
     <?xml version="1.0" encoding="utf-8"?>
     <android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
     xmlns:app="http://schemas.android.com/apk/res-auto"
     xmlns:tools="http://schemas.android.com/tools"
     android:layout_width="match_parent"
     android:layout_height="match_parent"
     tools:context=".MainActivity">

     <TextView
     android:id="@+id/helloworld_textview"
     android:layout_width="wrap_content"
     android:layout_height="wrap_content"
     android:text="Hello World!"
     app:layout_constraintBottom_toBottomOf="parent"
     app:layout_constraintLeft_toLeftOf="parent"
     app:layout_constraintRight_toRightOf="parent"
     app:layout_constraintTop_toTopOf="parent" />

     </android.support.constraint.ConstraintLayout>
     */
}
