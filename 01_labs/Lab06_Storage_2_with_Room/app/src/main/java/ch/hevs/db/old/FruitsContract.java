package ch.hevs.db.old;

import android.provider.BaseColumns;

public final class FruitsContract {

    private FruitsContract(){
        //empty constructor should never be instantiated
    }

    public static abstract class FruitEntry implements BaseColumns{
        //Table name
        public static final String TABLE_FRUITS = "fruits";

        //Fruit Column names
        public static final String KEY_ID = "id";
        public static final String KEY_FRUIT = "fruit";

        // Database creation sql statement
        public static final String CREATE_TABLE_FRUITS = "CREATE TABLE "
                + TABLE_FRUITS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_FRUIT
                + " TEXT NOT NULL);";
    }

}
