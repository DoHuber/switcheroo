package no.ntnu.stud.dominih.groupten.switcheroo;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Helper class, makes the code more concise and clear wherever it is used.
 *
 * From all the possible ChildEvents in Firebase, only onChildAdded and any error conditions
 * are relevant for this application. In order to not have to implement a full ChildEventListener
 * whenever one is needed, this abstract class was created to cover all the ignored events,
 * which are implemented but kept empty on purpose.
 *
 * @author Dominik Huber
 *
 */
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
