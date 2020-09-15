package ch.hevs.aislab.demo.database.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.util.Pair;

import java.util.List;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.async.account.CreateAccount;
import ch.hevs.aislab.demo.database.async.account.DeleteAccount;
import ch.hevs.aislab.demo.database.async.account.Transaction;
import ch.hevs.aislab.demo.database.async.account.UpdateAccount;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class AccountRepository {

    private static AccountRepository instance;

    private AccountRepository() {

    }

    public static AccountRepository getInstance() {
        if (instance == null) {
            synchronized (AccountRepository.class) {
                if (instance == null) {
                    instance = new AccountRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<AccountEntity> getAccount(final Long accountId, Application application) {
        return ((BaseApp) application).getDatabase().accountDao().getById(accountId);
    }

    public LiveData<List<AccountEntity>> getAccounts(Application application) {
        return ((BaseApp) application).getDatabase().accountDao().getAll();
    }

    public LiveData<List<AccountEntity>> getByOwner(final String owner, Application application) {
        return ((BaseApp) application).getDatabase().accountDao().getOwned(owner);
    }

    public void insert(final AccountEntity account, OnAsyncEventListener callback,
                       Application application) {
        new CreateAccount(application, callback).execute(account);
    }

    public void update(final AccountEntity account, OnAsyncEventListener callback,
                       Application application) {
        new UpdateAccount(application, callback).execute(account);
    }

    public void delete(final AccountEntity account, OnAsyncEventListener callback,
                       Application application) {
        new DeleteAccount(application, callback).execute(account);
    }

    @SuppressWarnings("unchecked")
    public void transaction(final AccountEntity sender, final AccountEntity recipient,
                            OnAsyncEventListener callback, Application application) {
        new Transaction(application, callback).execute(new Pair<>(sender, recipient));
    }
}
