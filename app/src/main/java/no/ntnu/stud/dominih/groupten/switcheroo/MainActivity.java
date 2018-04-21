package no.ntnu.stud.dominih.groupten.switcheroo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    FragmentManager myFragmentManager;
    FragmentTransaction myFragmentTransaction;
    DrawFragment myDrawFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myFragmentManager = getFragmentManager();
        myFragmentTransaction = myFragmentManager.beginTransaction();
        myDrawFragment = DrawFragment.getInstance();
        myFragmentTransaction.add(R.id.fragment_container,myDrawFragment,"draw");
        myFragmentTransaction.commit();
    }


    /**
     *
     * TO IMPLEMENT THE DRAWING FRAGMENT PUT THIS IN THE ON CREATE METHOD OF THE MAIN ACTIVITY
     *
     FragmentManager myFragmentManager;
     FragmentTransaction myFragmentTransaction;
     DrawFragment myDrawFragment;
     @Override
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
