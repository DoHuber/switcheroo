package no.ntnu.stud.dominih.groupten.switcheroo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Simple database hello world
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference switcherooRef = database.getReference("switcheroo");

        String random = UUID.randomUUID().toString();

        DatabaseReference newChild = switcherooRef.push();
        newChild.setValue(random);

    }
}
