package ch.hevs.aislab.demo.viewmodel.account;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import java.util.List;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.pojo.ClientWithAccounts;
import ch.hevs.aislab.demo.database.repository.AccountRepository;
import ch.hevs.aislab.demo.database.repository.ClientRepository;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class AccountListViewModel extends AndroidViewModel {

    private Application application;

    private AccountRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<ClientWithAccounts>> observableClientAccounts;
    private final MediatorLiveData<List<AccountEntity>> observableOwnAccounts;

    public AccountListViewModel(@NonNull Application application,
                                final String ownerId,
                                ClientRepository clientRepository,
                                AccountRepository accountRepository) {
        super(application);

        this.application = application;

        repository = accountRepository;

        observableClientAccounts = new MediatorLiveData<>();
        observableOwnAccounts = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableClientAccounts.setValue(null);
        observableOwnAccounts.setValue(null);

        LiveData<List<ClientWithAccounts>> clientAccounts =
                clientRepository.getOtherClientsWithAccounts(ownerId);
        LiveData<List<AccountEntity>> ownAccounts = repository.getByOwner(ownerId);

        // observe the changes of the entities from the database and forward them
        observableClientAccounts.addSource(clientAccounts, observableClientAccounts::setValue);
        observableOwnAccounts.addSource(ownAccounts, observableOwnAccounts::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String ownerId;

        private final ClientRepository clientRepository;

        private final AccountRepository accountRepository;

        public Factory(@NonNull Application application, String ownerId) {
            this.application = application;
            this.ownerId = ownerId;
            clientRepository = ((BaseApp) application).getClientRepository();
            accountRepository = ((BaseApp) application).getAccountRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new AccountListViewModel(application, ownerId, clientRepository, accountRepository);
        }
    }

    /**
     * Expose the LiveData ClientAccounts query so the UI can observe it.
     */
    public LiveData<List<ClientWithAccounts>> getClientAccounts() {
        return observableClientAccounts;
    }

    /**
     * Expose the LiveData AccountEntities query so the UI can observe it.
     */
    public LiveData<List<AccountEntity>> getOwnAccounts() {
        return observableOwnAccounts;
    }

    public void deleteAccount(AccountEntity account, OnAsyncEventListener callback) {
        repository.delete(account, callback);
    }

    public void executeTransaction(final AccountEntity sender, final AccountEntity recipient,
                                   OnAsyncEventListener callback) {
        repository.transaction(sender, recipient, callback);
    }
}
