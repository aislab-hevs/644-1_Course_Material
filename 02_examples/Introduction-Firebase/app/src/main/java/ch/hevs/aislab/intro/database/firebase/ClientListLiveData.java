package ch.hevs.aislab.intro.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.hevs.aislab.intro.database.entity.ClientEntity;

public class ClientListLiveData extends LiveData<List<ClientEntity>> {

    private static final String TAG = "ClientAccountsLiveData";

    private final DatabaseReference reference;
    private final MyValueEventListener listener = new MyValueEventListener();

    public ClientListLiveData(DatabaseReference ref) {
        reference = ref;
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        if (!hasActiveObservers()) {
            Log.d(TAG, "onInactive: Remove Event Listener");
            reference.removeEventListener(listener);
        }
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toClientList(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<ClientEntity> toClientList(DataSnapshot snapshot) {
        List<ClientEntity> clients = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            ClientEntity entity = childSnapshot.getValue(ClientEntity.class);
            Objects.requireNonNull(entity).setId(childSnapshot.getKey());
            clients.add(entity);
        }
        return clients;
    }
}
