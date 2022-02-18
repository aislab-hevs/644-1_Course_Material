package ch.hevs.aislab.demo.database.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.util.Pair;

import java.util.List;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.AppDatabase;
import ch.hevs.aislab.demo.database.async.account.CreateAccount;
import ch.hevs.aislab.demo.database.async.account.DeleteAccount;
import ch.hevs.aislab.demo.database.async.account.Transaction;
import ch.hevs.aislab.demo.database.async.account.UpdateAccount;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;
import ch.hevs.aislab.demo.util.TaskRunner;

public class AccountRepository {

    private static AccountRepository instance;
    private final AppDatabase database;

    private AccountRepository(final AppDatabase database) {this.database = database;}

    public static AccountRepository getInstance(final AppDatabase database) {
        if (instance == null) {
            synchronized (AccountRepository.class) {
                if (instance == null) {
                    instance = new AccountRepository(database);
                }
            }
        }
        return instance;
    }

    public LiveData<AccountEntity> getAccount(final Long accountId) {
        return database.accountDao().getById(accountId);
    }

    public LiveData<List<AccountEntity>> getAccounts() {
        return database.accountDao().getAll();
    }

    public LiveData<List<AccountEntity>> getByOwner(final String owner) {
        return database.accountDao().getOwned(owner);
    }

    public void insert(final AccountEntity account, OnAsyncEventListener callback) {
        TaskRunner.getInstance(AccountRepository.class).executeAsync(new CreateAccount(database, account), callback);
    }

    public void update(final AccountEntity account, OnAsyncEventListener callback) {
        TaskRunner.getInstance(AccountRepository.class).executeAsync(new UpdateAccount(database, account), callback);
    }

    public void delete(final AccountEntity account, OnAsyncEventListener callback) {
        TaskRunner.getInstance(AccountRepository.class).executeAsync(new DeleteAccount(database, account), callback);
    }

    @SuppressWarnings("unchecked")
    public void transaction(final AccountEntity sender, final AccountEntity recipient, OnAsyncEventListener callback) {
        TaskRunner.getInstance(AccountRepository.class).executeAsync(new Transaction(database, sender, recipient), callback);
    }
}
