package ch.hevs.aislab.demo.database.async.client;

import android.util.Log;

import java.util.concurrent.Callable;

import ch.hevs.aislab.demo.database.AppDatabase;
import ch.hevs.aislab.demo.database.entity.ClientEntity;

public class CreateClient implements Callable<Void> {

    private final AppDatabase database;
    private final ClientEntity client;

    public CreateClient(AppDatabase database, ClientEntity client) {
        this.database = database;
        this.client = client;
    }

    @Override
    public Void call() {
        database.clientDao().insert(client);
        Log.d("CreateClient","call");
        return null;
    }
}
