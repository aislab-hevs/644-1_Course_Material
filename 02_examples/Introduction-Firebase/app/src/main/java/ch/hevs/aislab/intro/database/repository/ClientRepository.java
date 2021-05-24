package ch.hevs.aislab.intro.database.repository;

import android.text.TextUtils;

import java.util.List;
import java.util.Objects;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import ch.hevs.aislab.intro.database.entity.ClientEntity;
import ch.hevs.aislab.intro.database.firebase.ClientListLiveData;
import ch.hevs.aislab.intro.database.firebase.ClientLiveData;
import ch.hevs.aislab.intro.util.OnAsyncEventListener;

public class ClientRepository {

    private static ClientRepository instance;

    public static ClientRepository getInstance() {
        if (instance == null) {
            synchronized (ClientRepository.class) {
                if (instance == null) {
                    instance = new ClientRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<ClientEntity> getClient(final String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(id);
        return new ClientLiveData(reference);
    }

    public LiveData<List<ClientEntity>> getAllClients() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("clients");
        return new ClientListLiveData(reference);
    }

    public void insert(final ClientEntity client, OnAsyncEventListener callback) {
        String id = FirebaseDatabase.getInstance().getReference("clients").push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(Objects.requireNonNull(id))
                .setValue(client, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final ClientEntity client, OnAsyncEventListener callback) {
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
