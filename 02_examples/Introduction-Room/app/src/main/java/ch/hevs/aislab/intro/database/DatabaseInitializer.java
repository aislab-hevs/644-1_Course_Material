package ch.hevs.aislab.intro.database;

import android.util.Log;

import java.time.Instant;

import ch.hevs.aislab.intro.database.entity.ClientEntity;

public class DatabaseInitializer {

    public static final String TAG = "DatabaseInitializer";

    public static void populateDatabase(final AppDatabase db) {
        Log.i(TAG, "Inserting demo data.");
        populateWithTestData(db);
    }

    private static void addClient(final AppDatabase db, final String email, final String firstName,
                                final String lastName) {
        ClientEntity client = new ClientEntity(
                email, firstName, lastName,
                Instant.parse("2021-01-01T10:00:00.00Z")
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
}
