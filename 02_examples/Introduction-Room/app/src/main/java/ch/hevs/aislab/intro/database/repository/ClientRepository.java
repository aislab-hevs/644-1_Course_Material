package ch.hevs.aislab.intro.database.repository;

import android.content.Context;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.hevs.aislab.intro.database.AppDatabase;
import ch.hevs.aislab.intro.database.async.CreateClient;
import ch.hevs.aislab.intro.database.async.DeleteClient;
import ch.hevs.aislab.intro.database.async.UpdateClient;
import ch.hevs.aislab.intro.database.entity.ClientEntity;
import ch.hevs.aislab.intro.util.OnAsyncEventListener;

public class ClientRepository {

    private static ClientRepository instance;

    private ClientRepository() {}

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

    public LiveData<ClientEntity> getClient(final String email, Context context) {
        return AppDatabase.getInstance(context).clientDao().getByEmail(email);
    }

    public LiveData<List<ClientEntity>> getAllClients(Context context) {
        return AppDatabase.getInstance(context).clientDao().getAll();
    }

    public void insert(final ClientEntity client, OnAsyncEventListener callback, Context context) {
        new CreateClient(context, callback).execute(client);
    }

    public void update(final ClientEntity client, OnAsyncEventListener callback, Context context) {
        new UpdateClient(context, callback).execute(client);
    }

    public void delete(final ClientEntity client, OnAsyncEventListener callback, Context context) {
        new DeleteClient(context, callback).execute(client);
    }
}
