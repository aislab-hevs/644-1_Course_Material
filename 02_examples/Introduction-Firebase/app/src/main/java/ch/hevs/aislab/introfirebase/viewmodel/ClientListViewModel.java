package ch.hevs.aislab.introfirebase.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ch.hevs.aislab.introfirebase.database.entity.ClientEntity;
import ch.hevs.aislab.introfirebase.database.repository.ClientRepository;
import ch.hevs.aislab.introfirebase.util.OnAsyncEventListener;

public class ClientListViewModel extends AndroidViewModel {

    private ClientRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<ClientEntity>> observableClients;

    public ClientListViewModel(@NonNull Application application, ClientRepository clientRepository) {
        super(application);

        repository = clientRepository;

        observableClients = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableClients.setValue(null);

        LiveData<List<ClientEntity>> clients = repository.getAllClients();

        // observe the changes of the entities from the database and forward them
        observableClients.addSource(clients, observableClients::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final ClientRepository clientRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            clientRepository = ClientRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ClientListViewModel(application, clientRepository);
        }
    }

    /**
     * Expose the LiveData ClientEntities query so the UI can observe it.
     */
    public LiveData<List<ClientEntity>> getClients() {
        return observableClients;
    }

    public void deleteClient(ClientEntity client) {
        repository.delete(client, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {}

            @Override
            public void onFailure(Exception e) {}
        });
    }
}
