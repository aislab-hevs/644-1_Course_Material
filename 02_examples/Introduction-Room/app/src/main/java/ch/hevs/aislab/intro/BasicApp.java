package ch.hevs.aislab.intro;

import android.app.Application;

import ch.hevs.aislab.intro.database.AppDatabase;
import ch.hevs.aislab.intro.database.repository.ClientRepository;

public class BasicApp extends Application {

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public ClientRepository getClientRepository() {
        return ClientRepository.getInstance(getDatabase());
    }
}
