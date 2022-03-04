package ch.hevs.aislab.demo.database.repository;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.database.firebase.AccountListLiveData;
import ch.hevs.aislab.demo.database.firebase.AccountLiveData;
import ch.hevs.aislab.demo.database.pojo.ClientWithAccounts;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class AccountRepository {

    private static AccountRepository instance;

    public static AccountRepository getInstance() {
        if (instance == null) {
            synchronized (AccountRepository.class) {
                if (instance == null) {
                    instance = new AccountRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<AccountEntity> getAccount(final String accountId) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("accounts")
                .child(accountId);
        return new AccountLiveData(reference);
    }

    public LiveData<List<AccountEntity>> getByOwner(final String owner) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(owner)
                .child("accounts");
        return new AccountListLiveData(reference, owner);
    }

    public void insert(final AccountEntity account, final OnAsyncEventListener callback) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(account.getOwner())
                .child("accounts");
        String key = reference.push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(account.getOwner())
                .child("accounts")
                .child(key)
                .setValue(account, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final AccountEntity account, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(account.getOwner())
                .child("accounts")
                .child(account.getId())
                .updateChildren(account.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void atomicUpdate(final Double amount, final AccountEntity sender, final AccountEntity recipient, OnAsyncEventListener callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("clients/"+sender.getOwner()+"/accounts/"+sender.getId()+"/balance", ServerValue.increment(-amount));
        updates.put("clients/"+recipient.getOwner()+"/accounts/"+recipient.getId()+"/balance", ServerValue.increment(amount));
        FirebaseDatabase.getInstance().getReference()
                .updateChildren(updates, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void delete(final AccountEntity account, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(account.getOwner())
                .child("accounts")
                .child(account.getId())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}