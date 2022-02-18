package ch.hevs.aislab.demo.database.async.client;

import java.util.concurrent.Callable;

import ch.hevs.aislab.demo.database.AppDatabase;
import ch.hevs.aislab.demo.database.entity.ClientEntity;

public class UpdateClient implements Callable<Void>{

    private final AppDatabase database;
    private final ClientEntity client;

    public UpdateClient(AppDatabase database, ClientEntity client) {
        this.database = database;
        this.client = client;
    }

    @Override
    public Void call(){
        database.clientDao().update(client);
        return null;
    }
}