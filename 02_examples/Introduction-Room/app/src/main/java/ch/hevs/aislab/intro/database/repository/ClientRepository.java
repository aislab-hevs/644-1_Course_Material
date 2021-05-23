package ch.hevs.aislab.intro.database.repository;

import java.util.List;

import androidx.lifecycle.LiveData;

import ch.hevs.aislab.intro.database.AppDatabase;
import ch.hevs.aislab.intro.database.async.CreateClientTask;
import ch.hevs.aislab.intro.database.async.DeleteClientTask;
import ch.hevs.aislab.intro.database.async.UpdateClientTask;
import ch.hevs.aislab.intro.database.entity.ClientEntity;
import ch.hevs.aislab.intro.util.OnAsyncEventListener;
import ch.hevs.aislab.intro.util.TaskRunner;

public class ClientRepository {

    private static ClientRepository instance;
    private final AppDatabase database;

    private ClientRepository(final AppDatabase database) {
        this.database = database;
    }

    public static ClientRepository getInstance(final AppDatabase database) {
        if (instance == null) {
            synchronized (ClientRepository.class) {
                if (instance == null) {
                    instance = new ClientRepository(database);
                }
            }
        }
        return instance;
    }

    public LiveData<ClientEntity> getClient(final long id) {
        return database.clientDao().getById(id);
    }

    public LiveData<List<ClientEntity>> getAllClients() {
        return database.clientDao().getAll();
    }

    public void insert(final ClientEntity client, OnAsyncEventListener callback) {
        TaskRunner.getInstance().executeAsync(new CreateClientTask(database, client), callback);
    }

    public void update(final ClientEntity client, OnAsyncEventListener callback) {
        TaskRunner.getInstance().executeAsync(new UpdateClientTask(database, client), callback);
    }

    public void delete(final ClientEntity client, OnAsyncEventListener callback) {
        TaskRunner.getInstance().executeAsync(new DeleteClientTask(database, client), callback);
    }
}
