package ch.hevs.aislab.intro.database.async;

import java.util.concurrent.Callable;

import ch.hevs.aislab.intro.database.AppDatabase;
import ch.hevs.aislab.intro.database.entity.ClientEntity;

public class UpdateClientTask implements Callable<Void> {

    private final AppDatabase database;
    private final ClientEntity client;

    public UpdateClientTask(AppDatabase database, ClientEntity client) {
        this.database = database;
        this.client = client;
    }

    @Override
    public Void call() {
        database.clientDao().update(client);
        return null;
    }
}