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

    private AccountRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<AccountEntity> mObservableAccount;

    public AccountViewModel(@NonNull Application application,
                                   final String accountId, AccountRepository accountRepository) {
        super(application);

        mRepository = accountRepository;

        mObservableAccount = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableAccount.setValue(null);

        if (accountId != null) {
            LiveData<AccountEntity> account = mRepository.getAccount(accountId);

            // observe the changes of the account entity from the database and forward them
            mObservableAccount.addSource(account, mObservableAccount::setValue);
        }
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final String mAccountId;

        private final AccountRepository mRepository;

        public Factory(@NonNull Application application, String accountId) {
            mApplication = application;
            mAccountId = accountId;
            mRepository = ((BaseApp) application).getAccountRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new AccountViewModel(mApplication, mAccountId, mRepository);
        }
    }

    /**
     * Expose the LiveData AccountEntity query so the UI can observe it.
     */
    public LiveData<AccountEntity> getAccount() {
        return mObservableAccount;
    }

    public void createAccount(AccountEntity account, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getAccountRepository()
                .insert(account, callback);
    }

    public void updateAccount(AccountEntity account, OnAsyncEventListener callback) {
        ((BaseApp) getApplication()).getAccountRepository()
                .update(account, callback);
    }
}