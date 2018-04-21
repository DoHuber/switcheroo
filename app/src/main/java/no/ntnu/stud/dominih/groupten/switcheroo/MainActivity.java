package no.ntnu.stud.dominih.groupten.switcheroo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     *
     * TO IMPLEMENT THE DRAWING FRAGMENT PUT THIS IN THE ON CREATE METHOD OF THE MAIN ACTIVITY
     *
     myFragmentManager = getFragmentManager();
     myFragmentTransaction = myFragmentManager.beginTransaction();
     myDrawFragment = DrawFragment.getInstance();
     myFragmentTransaction.add(R.id.fragment_container,myDrawFragment,"draw");
     myFragmentTransaction.commit();
     *
     *
     *
     *
     AND ADD THIS TO THE MAINACTIVITYXML
     
     <?xml version="1.0" encoding="utf-8"?>
     <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
     android:id="@+id/fragment_container"
     android:layout_width="match_parent"
     android:layout_height="match_parent"/>

     */
}
