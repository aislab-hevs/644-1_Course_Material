package ch.hevs.aislab.demo.database.repository;

import java.util.List;

import androidx.lifecycle.LiveData;

import ch.hevs.aislab.demo.database.AppDatabase;
import ch.hevs.aislab.demo.database.async.client.CreateClient;
import ch.hevs.aislab.demo.database.async.client.DeleteClient;
import ch.hevs.aislab.demo.database.async.client.UpdateClient;
import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.database.pojo.ClientWithAccounts;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;
import ch.hevs.aislab.demo.util.TaskRunner;

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

    public LiveData<ClientEntity> getClient(final String id) {
        return database.clientDao().getById(id);
    }

    public LiveData<List<ClientWithAccounts>> getOtherClientsWithAccounts(final String owner) {
        return database.clientDao().getOtherClientsWithAccounts(owner);
    }

    public void insert(final ClientEntity client, OnAsyncEventListener callback) {
        TaskRunner.getInstance(ClientRepository.class).executeAsync(new CreateClient(database, client), callback);
    }

    public void update(final ClientEntity client, OnAsyncEventListener callback) {
        TaskRunner.getInstance(ClientRepository.class).executeAsync(new UpdateClient(database, client), callback);
    }

    public void delete(final ClientEntity client, OnAsyncEventListener callback) {
        TaskRunner.getInstance(ClientRepository.class).executeAsync(new DeleteClient(database, client), callback);
    }
}
