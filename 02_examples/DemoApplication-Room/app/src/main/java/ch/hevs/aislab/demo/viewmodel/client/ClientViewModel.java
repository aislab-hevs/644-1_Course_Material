package ch.hevs.aislab.demo.viewmodel.client;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.database.repository.ClientRepository;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class ClientViewModel extends AndroidViewModel {

    private ClientRepository repository;

    private Application application;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<ClientEntity> observableClient;

    public ClientViewModel(@NonNull Application application,
                            final String clientId, ClientRepository clientRepository) {
        super(application);

        this.application = application;

        repository = clientRepository;

        observableClient = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableClient.setValue(null);

        LiveData<ClientEntity> client = repository.getClient(clientId, application);

        // observe the changes of the client entity from the database and forward them
        observableClient.addSource(client, observableClient::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String clientId;

        private final ClientRepository repository;

        public Factory(@NonNull Application application, String clientId) {
            this.application = application;
            this.clientId = clientId;
            repository = ((BaseApp) application).getClientRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ClientViewModel(application, clientId, repository);
        }
    }

    /**
     * Expose the LiveData ClientEntity query so the UI can observe it.
     */
    public LiveData<ClientEntity> getClient() {
        return observableClient;
    }

    public void createClient(ClientEntity client, OnAsyncEventListener callback) {
        repository.insert(client, callback, application);
    }

    public void updateClient(ClientEntity client, OnAsyncEventListener callback) {
        repository.update(client, callback, application);
    }

    public void deleteClient(ClientEntity client, OnAsyncEventListener callback) {
        repository.delete(client, callback, application);

    }
}
