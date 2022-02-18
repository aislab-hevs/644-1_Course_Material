package ch.hevs.aislab.demo.database.async.account;

        import android.app.Application;
        import android.os.AsyncTask;
        import android.util.Pair;

        import java.util.concurrent.Callable;

        import ch.hevs.aislab.demo.BaseApp;
        import ch.hevs.aislab.demo.database.AppDatabase;
        import ch.hevs.aislab.demo.database.entity.AccountEntity;
        import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class Transaction implements Callable<Void> {

    private final AppDatabase database;
    private final AccountEntity sender;
    private final AccountEntity recipient;

    public Transaction(AppDatabase database, AccountEntity sender, AccountEntity recipient) {
        this.database = database;
        this.sender = sender;
        this.recipient = recipient;
    }

    @Override
    public Void call(){
        database.accountDao().transaction(sender, recipient);
        return null;
    }
}