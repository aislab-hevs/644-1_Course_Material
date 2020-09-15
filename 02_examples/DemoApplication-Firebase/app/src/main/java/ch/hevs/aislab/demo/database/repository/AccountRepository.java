package ch.hevs.aislab.demo.database.repository;

import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.List;

import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.firebase.AccountListLiveData;
import ch.hevs.aislab.demo.database.firebase.AccountLiveData;
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

    public void transaction(final AccountEntity sender, final AccountEntity recipient,
                            OnAsyncEventListener callback) {
        final DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        rootReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                rootReference
                        .child("clients")
                        .child(sender.getOwner())
                        .child("accounts")
                        .child(sender.getId())
                        .updateChildren(sender.toMap());

                rootReference
                        .child("clients")
                        .child(recipient.getOwner())
                        .child("accounts")
                        .child(recipient.getId())
                        .updateChildren(recipient.toMap());

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    callback.onFailure(databaseError.toException());
                } else {
                    callback.onSuccess();
                }
            }
        });
    }
}
