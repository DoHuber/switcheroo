package no.ntnu.stud.dominih.groupten.switcheroo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDatabaseService<T> {

    private String databaseRefName;
    private DatabaseReference databaseReference;
    private List<AsyncCallback<T>> callbacks = new ArrayList<>();
    private Class<T> typeClass;

    private AbstractDatabaseService(){}

    public AbstractDatabaseService(String databaseRefName, Class<T> typeClass) {

        this.databaseRefName = databaseRefName;
        this.typeClass = typeClass;

    }

    public void init() {

        databaseReference = FirebaseDatabase.getInstance().getReference(databaseRefName);
        startListening();

    }

    private void startListening() {
        databaseReference.addChildEventListener(new AbstractChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                notifyListeners(true, snapshotToList(dataSnapshot), null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                notifyListeners(false, null, databaseError.toException());
            }

        });
    }

    private void notifyListeners(boolean success, List<T> result, Exception e) {

            for (AsyncCallback<T> element : callbacks) {

                if (success) {
                    element.onSuccess(result);
                } else {
                    element.onFailure(e);
                }
            }

    }

    public void addChild(T newChild) {

        DatabaseReference childReference = databaseReference.push();
        childReference.setValue(newChild);

    }

    public void subscribeToUpdates(AsyncCallback<T> callback) {

        callbacks.add(callback);

    }

    public void getAll(final AsyncCallback<T> callback) {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                callback.onSuccess(snapshotToList(dataSnapshot));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                callback.onFailure(databaseError.toException());

            }
        });

    }

    private List<T> snapshotToList(DataSnapshot snapshot) {

        List<T> result = new ArrayList<>();

        for (DataSnapshot element : snapshot.getChildren()) {

            result.add(snapshotToObject(element));

        }

        return result;

    }

    private T snapshotToObject(DataSnapshot snapshot) {

        return snapshot.getValue(typeClass);

    }

}
