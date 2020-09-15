package ch.hevs.aislab.introfirebase.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ch.hevs.aislab.introfirebase.database.entity.ClientEntity;
import ch.hevs.aislab.introfirebase.database.repository.ClientRepository;
import ch.hevs.aislab.introfirebase.util.OnAsyncEventListener;

public class ClientViewModel extends AndroidViewModel {

    private ClientRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<ClientEntity> observableClient;

    public ClientViewModel(@NonNull Application application,
                           final String email, ClientRepository clientRepository) {
        super(application);

        repository = clientRepository;

        observableClient = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableClient.setValue(null);

        if (email != null) {
            LiveData<ClientEntity> client = repository.getClient(email);

            // observe the changes of the client entity from the database and forward them
            observableClient.addSource(client, observableClient::setValue);
        }
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String email;

        private final ClientRepository repository;

        public Factory(@NonNull Application application, String id) {
            this.application = application;
            email = id;
            repository = ClientRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ClientViewModel(application, email, repository);
        }
    }

    /**
     * Expose the LiveData ClientEntity query so the UI can observe it.
     */
    public LiveData<ClientEntity> getClient() {
        return observableClient;
    }

    public void createClient(ClientEntity client, OnAsyncEventListener callback) {
        ClientRepository.getInstance().insert(client, callback);
    }

    public void updateClient(ClientEntity client, OnAsyncEventListener callback) {
        repository.update(client, callback);
    }

    public void deleteClient(ClientEntity client, OnAsyncEventListener callback) {
        repository.delete(client, callback);
    }
}
