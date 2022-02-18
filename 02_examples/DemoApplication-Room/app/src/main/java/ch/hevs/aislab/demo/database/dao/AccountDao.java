package ch.hevs.aislab.demo.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import ch.hevs.aislab.demo.database.entity.AccountEntity;

/**
 * https://developer.android.com/topic/libraries/architecture/room.html#no-object-references
 */
@Dao
public abstract class AccountDao {

    @Query("SELECT * FROM accounts WHERE id = :id")
    public abstract LiveData<AccountEntity> getById(Long id);

    @Query("SELECT * FROM accounts")
    public abstract LiveData<List<AccountEntity>> getAll();

    @Query("SELECT * FROM accounts WHERE owner=:owner")
    public abstract LiveData<List<AccountEntity>> getOwned(String owner);

    @Insert
    public abstract long insert(AccountEntity account);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<AccountEntity> accounts);

    @Update
    public abstract void update(AccountEntity account);

    @Delete
    public abstract void delete(AccountEntity account);

    @Query("DELETE FROM accounts")
    public abstract void deleteAll();

    /**
     * There's currently no way to add additional constraints (beside ForeignKey) to columns.
     *
     * This means we currently cannot check on a DB level if the balance of a client will be
     * updated with a negative value.
     * So we need to ensure that the sender has enough money on his client BEFORE we call this
     * method because we want to ensure that people cannot get into debt.
     *
     * @param sender AccountEntity that sends the money
     * @param recipient AccountEntity that receives the money
     */
    @Transaction
    public void transaction(AccountEntity sender, AccountEntity recipient) {
        update(sender);
        update(recipient);
    }
}
