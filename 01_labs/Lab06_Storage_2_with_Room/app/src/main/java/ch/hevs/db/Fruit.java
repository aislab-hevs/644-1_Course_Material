package ch.hevs.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "fruits", indices = {@Index(value = {"fruit"}, unique = true)})
public class Fruit {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "fruit")
    private String fruitName;

    public Fruit(String fruitName) {
        this.fruitName = fruitName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFruitName() {
        return fruitName;
    }

    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }
}
