package ch.hevs.aislab.demo.database.async.client;

import android.app.Application;
import android.os.AsyncTask;

import java.util.concurrent.Callable;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.AppDatabase;
import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class DeleteClient implements Callable<Void> {

    private final AppDatabase database;
    private final ClientEntity client;

    public DeleteClient(AppDatabase database, ClientEntity client) {
        this.database = database;
        this.client = client;
    }

    @Override
    public Void call(){
        database.clientDao().delete(client);
        return null;
    }
}