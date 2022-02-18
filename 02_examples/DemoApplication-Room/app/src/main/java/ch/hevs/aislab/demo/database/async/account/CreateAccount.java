package ch.hevs.aislab.demo.database.async.account;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.Callable;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.AppDatabase;
import ch.hevs.aislab.demo.database.entity.AccountEntity;

public class CreateAccount implements Callable<Void> {

    private final AppDatabase database;
    private final AccountEntity account;

    public CreateAccount(AppDatabase database, AccountEntity account) {
        this.database = database;
        this.account = account;
    }

    @Override
    public Void call(){
        database.accountDao().insert(account);
        Log.d("", "Create account");
        return null;
    }
}