package ch.hevs.aislab.demo;

import android.app.Application;

import ch.hevs.aislab.demo.database.repository.AccountRepository;
import ch.hevs.aislab.demo.database.repository.ClientRepository;

/**
 * Android Application class. Used for accessing singletons.
 */
public class BaseApp extends Application {

    public AccountRepository getAccountRepository() {
        return AccountRepository.getInstance();
    }

    public ClientRepository getClientRepository() {
        return ClientRepository.getInstance();
    }
}