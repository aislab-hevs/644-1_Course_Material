package ch.hevs.db.old;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ch.hevs.db.old.FruitsContract;

public class DbHelper extends SQLiteOpenHelper{
    //Info about database
	private static final String DATABASE_NAME = "fruits.db";
	private static final int DATABASE_VERSION = 1;

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(FruitsContract.FruitEntry.CREATE_TABLE_FRUITS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
