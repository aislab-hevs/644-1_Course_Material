package ch.hevs.aislab.intro.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

import ch.hevs.aislab.intro.BasicApp;
import ch.hevs.aislab.intro.database.entity.ClientEntity;
import ch.hevs.aislab.intro.database.repository.ClientRepository;
import ch.hevs.aislab.intro.util.OnAsyncEventListener;

public class ClientViewModel extends AndroidViewModel {

    private final LiveData<ClientEntity> observableClient;
    private final ClientRepository clientRepository;
    private final DateTimeFormatter dateFormatter;

    public ClientViewModel(@NonNull Application application, ClientRepository repository,
                            final String clientId) {
        super(application);
        clientRepository = repository;
        observableClient = repository.getClient(clientId);
        dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy hh:mm:ss a")
                .withLocale(Locale.getDefault());
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */

    public LiveData<ClientEntity> getClient() {
        return observableClient;
    }

    public long getTimestamp(String dateString) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(
                LocalDateTime.parse(dateString, dateFormatter),
                TimeZone.getDefault().toZoneId()
        );
        return zonedDateTime.toInstant().toEpochMilli();
    }

    public String displayTimestamp(Long timestamp) {
        if (timestamp != 0) {
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(
                    Instant.ofEpochMilli(timestamp),
                    TimeZone.getDefault().toZoneId()
            );
            return zonedDateTime.format(dateFormatter);
        } else {
            return "";
        }
    }

    public void createClient(ClientEntity client, OnAsyncEventListener callback) {
        clientRepository.insert(client, callback);
    }

    public void updateClient(ClientEntity client, OnAsyncEventListener callback) {
        clientRepository.update(client, callback);
    }

    /**
     * A creator is used to inject the client ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the client ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String clientId;

        private final ClientRepository clientRepository;

        public Factory(@NonNull Application application, String clientId) {
            this.application = application;
            this.clientId = clientId;
            clientRepository = ((BasicApp) application).getClientRepository();
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ClientViewModel(application, clientRepository, clientId);
        }
    }
}