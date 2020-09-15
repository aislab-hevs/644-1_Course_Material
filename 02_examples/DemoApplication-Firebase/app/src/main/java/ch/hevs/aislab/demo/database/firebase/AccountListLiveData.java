package ch.hevs.aislab.demo.database.firebase;

import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ch.hevs.aislab.demo.database.entity.AccountEntity;

public class AccountListLiveData extends LiveData<List<AccountEntity>> {

    private static final String TAG = "AccountListLiveData";

    private final DatabaseReference reference;
    private final String owner;
    private final MyValueEventListener listener = new MyValueEventListener();

    public AccountListLiveData(DatabaseReference ref, String owner) {
        reference = ref;
        this.owner = owner;
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
            setValue(toAccounts(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<AccountEntity> toAccounts(DataSnapshot snapshot) {
        List<AccountEntity> accounts = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            AccountEntity entity = childSnapshot.getValue(AccountEntity.class);
            entity.setId(childSnapshot.getKey());
            entity.setOwner(owner);
            accounts.add(entity);
        }
        return accounts;
    }
}