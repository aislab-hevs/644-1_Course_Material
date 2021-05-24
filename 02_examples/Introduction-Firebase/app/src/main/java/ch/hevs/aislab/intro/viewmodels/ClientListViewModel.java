package ch.hevs.aislab.intro.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ch.hevs.aislab.intro.BasicApp;
import ch.hevs.aislab.intro.database.entity.ClientEntity;
import ch.hevs.aislab.intro.database.repository.ClientRepository;
import ch.hevs.aislab.intro.util.OnAsyncEventListener;

public class ClientListViewModel extends AndroidViewModel {

    private final ClientRepository clientRepository;
    private final LiveData<List<ClientEntity>> clients;

    public ClientListViewModel(Application application) {
        super(application);

        clientRepository = ((BasicApp) application).getClientRepository();
        clients = clientRepository.getAllClients();
    }

    /**
     * Expose the LiveData ClientEntities query so the UI can observe it.
     */
    public LiveData<List<ClientEntity>> getClients() {
        return clients;
    }

    public void deleteClient(ClientEntity client, OnAsyncEventListener callback) {
        clientRepository.delete(client, callback);
    }
}