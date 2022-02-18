package ch.hevs.aislab.demo.viewmodel.account;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.repository.AccountRepository;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class AccountViewModel  extends AndroidViewModel {

    private Application application;

    private AccountRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<AccountEntity> observableAccount;

    public AccountViewModel(@NonNull Application application,
                                   final Long accountId, AccountRepository accountRepository) {
        super(application);

        this.application = application;

        repository = accountRepository;

        observableAccount = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableAccount.setValue(null);

        LiveData<AccountEntity> account = repository.getAccount(accountId);

        // observe the changes of the account entity from the database and forward them
        observableAccount.addSource(account, observableAccount::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final Long accountId;

        private final AccountRepository repository;

        public Factory(@NonNull Application application, Long accountId) {
            this.application = application;
            this.accountId = accountId;
            repository = ((BaseApp) application).getAccountRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new AccountViewModel(application, accountId, repository);
        }
    }

    /**
     * Expose the LiveData AccountEntity query so the UI can observe it.
     */
    public LiveData<AccountEntity> getAccount() {
        return observableAccount;
    }

    public void createAccount(AccountEntity account, OnAsyncEventListener callback) {
        repository.insert(account, callback);
    }

    public void updateAccount(AccountEntity account, OnAsyncEventListener callback) {
        repository.update(account, callback);
    }
}