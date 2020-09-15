package ch.hevs.aislab.demo.database.async.client;

import android.app.Application;
import android.os.AsyncTask;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class DeleteClient extends AsyncTask<ClientEntity, Void, Void> {

    private Application application;
    private OnAsyncEventListener callback;
    private Exception exception;

    public DeleteClient(Application application, OnAsyncEventListener callback) {
        this.application = application;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(ClientEntity... params) {
        try {
            for (ClientEntity client : params)
                ((BaseApp) application).getDatabase().clientDao()
                        .delete(client);
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (callback != null) {
            if (exception == null) {
                callback.onSuccess();
            } else {
                callback.onFailure(exception);
            }
        }
    }
}