package ch.hevs.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import android.database.sqlite.SQLiteConstraintException;

import java.util.List;

@Dao
public interface FruitDao {

    @Query("SELECT * FROM fruits")
    List<Fruit> getAll();

    @Query("SELECT * FROM fruits WHERE id IN (:fruitIds)")
    List<Fruit> loadAllByIds(int[] fruitIds);

    @Query("SELECT * FROM fruits WHERE fruit LIKE :fruitName LIMIT 1")
    Fruit findByName(String fruitName);

    @Insert
    void insertAll(Fruit... fruits) throws SQLiteConstraintException;

    @Update
    void updateFruits(Fruit... fruits);

    @Delete
    void delete(Fruit fruits);

    @Query("DELETE FROM fruits")
    void deleteAll();
}
