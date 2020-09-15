package ch.hevs.aislab.demo.database.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.async.client.CreateClient;
import ch.hevs.aislab.demo.database.async.client.DeleteClient;
import ch.hevs.aislab.demo.database.async.client.UpdateClient;
import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.database.pojo.ClientWithAccounts;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class ClientRepository {

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

    public LiveData<ClientEntity> getClient(final String clientId, Application application) {
        return ((BaseApp) application).getDatabase().clientDao().getById(clientId);
    }

    public LiveData<List<ClientWithAccounts>> getOtherClientsWithAccounts(final String owner,
                                                                          Application application) {
        return ((BaseApp) application).getDatabase().clientDao().getOtherClientsWithAccounts(owner);
    }

    public void insert(final ClientEntity client, OnAsyncEventListener callback,
                       Application application) {
        new CreateClient(application, callback).execute(client);
    }

    public void update(final ClientEntity client, OnAsyncEventListener callback,
                       Application application) {
        new UpdateClient(application, callback).execute(client);
    }

    public void delete(final ClientEntity client, OnAsyncEventListener callback,
                       Application application) {
        new DeleteClient(application, callback).execute(client);
    }
}
