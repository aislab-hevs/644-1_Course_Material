package ch.hevs.aislab.demo.database.firebase;

import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ch.hevs.aislab.demo.database.entity.ClientEntity;

public class ClientLiveData extends LiveData<ClientEntity> {
    private static final String TAG = "ClientLiveData";

    private final DatabaseReference reference;
    private final ClientLiveData.MyValueEventListener listener = new ClientLiveData.MyValueEventListener();

    public ClientLiveData(DatabaseReference ref) {
        this.reference = ref;
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ClientEntity entity = dataSnapshot.getValue(ClientEntity.class);
            entity.setId(dataSnapshot.getKey());
            setValue(entity);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }
}
