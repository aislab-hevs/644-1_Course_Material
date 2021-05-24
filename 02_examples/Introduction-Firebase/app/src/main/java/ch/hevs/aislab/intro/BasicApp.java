package ch.hevs.aislab.intro;

import android.app.Application;

import ch.hevs.aislab.intro.database.repository.ClientRepository;

public class BasicApp extends Application {

    public static final String KEY_CLIENT_ID = "client_id";

    public ClientRepository getClientRepository() {
        return ClientRepository.getInstance();
    }
}
