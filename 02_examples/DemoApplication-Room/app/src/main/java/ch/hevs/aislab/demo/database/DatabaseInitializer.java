package ch.hevs.aislab.demo.database;

import android.os.AsyncTask;
import android.util.Log;

import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.entity.ClientEntity;

/**
 * Generates dummy data
 */
public class DatabaseInitializer {

    public static final String TAG = "DatabaseInitializer";

    public static void populateDatabase(final AppDatabase db) {
        Log.i(TAG, "Inserting demo data.");
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    private static void addClient(final AppDatabase db, final String email, final String firstName,
                                   final String lastName, final String password) {
        ClientEntity client = new ClientEntity(email, firstName, lastName, password);
        db.clientDao().insert(client);
    }

    private static void addAccount(final AppDatabase db, final String name, final Double balance,
                                   final String owner) {
        AccountEntity account = new AccountEntity(name, balance, owner);
        db.accountDao().insert(account);
    }

    private static void populateWithTestData(AppDatabase db) {
        db.clientDao().deleteAll();

        addClient(db,
                "m.p@fifa.com", "Michel", "Platini", "michel1"
        );
        addClient(db,
                "s.b@fifa.com", "Sepp", "Blatter", "sepp1"
        );
        addClient(db,
                "e.s@fifa.com", "Ebbe", "Schwartz", "ebbe1"
        );
        addClient(db,
                "a.c@fifa.com", "Aleksander", "Ceferin", "aleksander1"
        );

        try {
            // Let's ensure that the clients are already stored in the database before we continue.
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        addAccount(db,
                "Savings", 20000d, "m.p@fifa.com"
        );
        addAccount(db,
                "Savings", 20000d, "s.b@fifa.com"
        );
        addAccount(db,
                "Savings", 20000d, "e.s@fifa.com"
        );
        addAccount(db,
                "Savings", 20000d, "a.c@fifa.com"
        );

        addAccount(db,
                "Secret", 1820000d, "m.p@fifa.com"
        );
        addAccount(db,
                "Secret", 1820000d, "s.b@fifa.com"
        );
        addAccount(db,
                "Secret", 1820000d, "e.s@fifa.com"
        );
        addAccount(db,
                "Secret", 1820000d, "a.c@fifa.com"
        );
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase database;

        PopulateDbAsync(AppDatabase db) {
            database = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(database);
            return null;
        }

    }
}
