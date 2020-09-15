package ch.hevs.aislab.demo.database.firebase;

import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ch.hevs.aislab.demo.database.entity.AccountEntity;

public class AccountLiveData extends LiveData<AccountEntity> {

    private static final String TAG = "AccountLiveData";

    private final DatabaseReference reference;
    private final String owner;
    private final AccountLiveData.MyValueEventListener listener = new AccountLiveData.MyValueEventListener();

    public AccountLiveData(DatabaseReference ref) {
        reference = ref;
        owner = ref.getParent().getParent().getKey();
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
            AccountEntity entity = dataSnapshot.getValue(AccountEntity.class);
            entity.setId(dataSnapshot.getKey());
            entity.setOwner(owner);
            setValue(entity);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }
}
