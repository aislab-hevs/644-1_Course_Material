package ch.hevs.aislab.intro.database;

import android.os.AsyncTask;
import android.util.Log;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

import ch.hevs.aislab.intro.database.entity.ClientEntity;
import ch.hevs.aislab.intro.util.TaskRunner;

public class DatabaseInitializer {

    public static final String TAG = "DatabaseInitializer";

    public static void populateDatabase(final AppDatabase db) {
        Log.i(TAG, "Inserting demo data.");
        new PopulateDbTask(db).call();
    }

    private static void addClient(final AppDatabase db, final String email, final String firstName,
                                final String lastName) {
        ClientEntity client = new ClientEntity(
                email, firstName, lastName,
                LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(
                                ThreadLocalRandom.current().nextInt() * 1000L
                        ), OffsetDateTime.now().getOffset())
        );
        db.clientDao().insert(client);
    }

    private static void populateWithTestData(AppDatabase db) {
        db.clientDao().deleteAll();

        addClient(db, "michel.platini@fifa.com", "Michel", "Platini");
        addClient(db, "sepp.blatter@fifa.com", "Sepp", "Blatter");
        addClient(db, "ebbe.schwartz@fifa.com", "Ebbe", "Schwartz");
        addClient(db, "aleksander.ceferin@fifa.com", "Aleksander", "Ceferin");
    }

    private static class PopulateDbTask implements Callable<Void> {

        private final AppDatabase database;

        PopulateDbTask(AppDatabase db) {
            database = db;
        }

        @Override
        public Void call() {
            populateWithTestData(database);
            return null;
        }
    }
}
