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

    private static final String TAG = "AccountListViewModel";

    private AccountRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<ClientWithAccounts>> mObservableClientAccounts;
    private final MediatorLiveData<List<AccountEntity>> mObservableOwnAccounts;

    public AccountListViewModel(@NonNull Application application,
                                final String ownerId, ClientRepository clientRepository, AccountRepository accountRepository) {
        super(application);

        mRepository = accountRepository;

        mObservableClientAccounts = new MediatorLiveData<>();
        mObservableOwnAccounts = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableClientAccounts.setValue(null);
        mObservableOwnAccounts.setValue(null);

        LiveData<List<ClientWithAccounts>> clientAccounts = clientRepository.getOtherClientsWithAccounts(ownerId);
        LiveData<List<AccountEntity>> ownAccounts = mRepository.getByOwner(ownerId);

        // observe the changes of the entities from the database and forward them
        mObservableClientAccounts.addSource(clientAccounts, mObservableClientAccounts::setValue);
        mObservableOwnAccounts.addSource(ownAccounts, mObservableOwnAccounts::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final String mOwnerId;

        private final ClientRepository mClientRepository;

        private final AccountRepository mAccountRepository;

        public Factory(@NonNull Application application, String ownerId) {
            mApplication = application;
            mOwnerId = ownerId;
            mClientRepository = ((BaseApp) application).getClientRepository();
            mAccountRepository = ((BaseApp) application).getAccountRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new AccountListViewModel(mApplication, mOwnerId, mClientRepository, mAccountRepository);
        }
    }

    /**
     * Expose the LiveData ClientWithAccounts query so the UI can observe it.
     */
    public LiveData<List<ClientWithAccounts>> getClientAccounts() {
        return mObservableClientAccounts;
    }

    /**
     * Expose the LiveData AccountEntities query so the UI can observe it.
     */
    public LiveData<List<AccountEntity>> getOwnAccounts() {
        return mObservableOwnAccounts;
    }

    public void deleteAccount(AccountEntity account, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getAccountRepository()
                .delete(account, callback);
    }

    public void executeTransaction(final AccountEntity sender, final AccountEntity recipient,
                                   OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getAccountRepository().transaction(sender, recipient, callback);
    }
}
