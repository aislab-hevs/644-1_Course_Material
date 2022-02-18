package ch.hevs.aislab.demo.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import java.util.List;

import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.database.pojo.ClientWithAccounts;

/**
 * https://developer.android.com/topic/libraries/architecture/room.html#no-object-references
 */
@Dao
public interface ClientDao {
    @Query("SELECT * FROM clients WHERE email = :id")
    LiveData<ClientEntity> getById(String id);

    @Query("SELECT * FROM clients")
    LiveData<List<ClientEntity>> getAll();

    /**
     * This method is used to populate the transaction activity.
     * It returns all OTHER users and their accounts.
     * @param id Id of the client who should be excluded from the list.
     * @return A live data object containing a list of ClientAccounts with
     * containing all clients but the @id.
     */
    @Transaction
    @Query("SELECT * FROM clients WHERE email != :id")
    LiveData<List<ClientWithAccounts>> getOtherClientsWithAccounts(String id);

    @Insert
    long insert(ClientEntity client) throws SQLiteConstraintException;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ClientEntity> clients);

    @Update
    void update(ClientEntity client);

    @Delete
    void delete(ClientEntity client);

    @Query("DELETE FROM clients")
    void deleteAll();
}
