package ch.hevs.aislab.demo.database.repository;

import androidx.lifecycle.LiveData;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.database.firebase.ClientAccountsListLiveData;
import ch.hevs.aislab.demo.database.firebase.ClientLiveData;
import ch.hevs.aislab.demo.database.pojo.ClientWithAccounts;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class ClientRepository {

    private static final String TAG = "ClientRepository";

    private static ClientRepository instance;

    private ClientRepository() {
    }

    public static ClientRepository getInstance() {
        if (instance == null) {
            synchronized (AccountRepository.class) {
                if (instance == null) {
                    instance = new ClientRepository();
                }
            }
        }
        return instance;
    }

    public void signIn(final String email, final String password,
                       final OnCompleteListener<AuthResult> listener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public LiveData<ClientEntity> getClient(final String clientId) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(clientId);
        return new ClientLiveData(reference);
    }

    public LiveData<List<ClientWithAccounts>> getOtherClientsWithAccounts(final String owner) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("clients");
        return new ClientAccountsListLiveData(reference, owner);
    }

    public void register(final ClientEntity client, final OnAsyncEventListener callback) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                client.getEmail(),
                client.getPassword()
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                client.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                insert(client, callback);
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    private void insert(final ClientEntity client, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(client, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                        FirebaseAuth.getInstance().getCurrentUser().delete()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        callback.onFailure(null);
                                        Log.d(TAG, "Rollback successful: User account deleted");
                                    } else {
                                        callback.onFailure(task.getException());
                                        Log.d(TAG, "Rollback failed: signInWithEmail:failure",
                                                task.getException());
                                    }
                                });
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final ClientEntity client, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(client.getId())
                .updateChildren(client.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
        FirebaseAuth.getInstance().getCurrentUser().updatePassword(client.getPassword())
                .addOnFailureListener(
                        e -> Log.d(TAG, "updatePassword failure!", e)
        );
    }

    public void delete(final ClientEntity client, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(client.getId())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
