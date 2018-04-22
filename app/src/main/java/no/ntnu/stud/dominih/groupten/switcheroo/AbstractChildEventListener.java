package no.ntnu.stud.dominih.groupten.switcheroo;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

abstract class AbstractChildEventListener implements ChildEventListener {

    @Override
    public abstract void onChildAdded(DataSnapshot dataSnapshot, String s);

    @Override
    public abstract void onCancelled(DatabaseError databaseError);

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        // Not possible, ignored
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        // Not possible, ignored
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        // Not possible, ignored
    }

}
