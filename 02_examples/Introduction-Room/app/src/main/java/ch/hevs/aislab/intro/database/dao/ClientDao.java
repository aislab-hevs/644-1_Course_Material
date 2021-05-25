package ch.hevs.aislab.intro.database.dao;

import android.database.sqlite.SQLiteConstraintException;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import ch.hevs.aislab.intro.database.entity.ClientEntity;

@Dao
public interface ClientDao {

    @Query("SELECT * FROM clients WHERE id = :id")
    LiveData<ClientEntity> getById(long id);

    @Query("SELECT id, first_name, last_name FROM clients")
    LiveData<List<ClientEntity>> getAllPartial();

    @Insert
    void insert(ClientEntity client) throws SQLiteConstraintException;

    @Update
    void update(ClientEntity client);

    @Delete
    void delete(ClientEntity client);

    @Query("DELETE FROM clients")
    void deleteAll();
}
