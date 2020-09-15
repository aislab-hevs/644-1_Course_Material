package ch.hevs.storage;

import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ch.hevs.db.AppDatabase;
import ch.hevs.db.Fruit;

public class MainActivity extends Activity {

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, ShowData.class);

        switch (v.getId()) {
            case R.id.main_btn_cantons_save:  // 1.	Save it in a SharedPreference object
                String[] cantons = getResources().getStringArray(R.array.cantons_array);

                SharedPreferences sharedPref = getSharedPreferences("cantons", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                for (int i = 0; i < cantons.length; i++) {
                    editor.putString(i + "", cantons[i]);
                }
                editor.commit();
                Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
                break;

            case R.id.main_btn_cantons_read:
                intent.putExtra("mode", 0);
                startActivity(intent);
                break;

            case R.id.main_btn_planets_save:  // 2.	Save it in an internal file
                String[] planets = getResources().getStringArray(R.array.planets_array);

                String file = "planets.txt";
                String output = "";
                FileOutputStream outputStream;

                for (int i = 0; i < planets.length; i++) {
                    output = output + planets[i] + ";";
                }

                try {
                    outputStream = openFileOutput(file, Context.MODE_PRIVATE);
                    outputStream.write(output.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
                break;

            case R.id.main_btn_planets_read:
                intent.putExtra("mode", 1);
                startActivity(intent);
                break;

            case R.id.main_btn_fruits_save:  // 3.	Save it in a database
                String[] fruitStrings = getResources().getStringArray(R.array.fruits_array);
                /*
                DbHelper dbHelper = new DbHelper(this);
			    SQLiteDatabase db = dbHelper.getWritableDatabase();

			    ContentValues values = new ContentValues();

			    for (int i = 0; i < fruitStrings.length; i++) {
				    values.put(FruitEntry.KEY_FRUIT, fruitStrings[i]);
				    db.insert(FruitEntry.TABLE_FRUITS, FruitEntry.KEY_FRUIT, values);
			    }

                db.close();
			    */

                db = AppDatabase.getAppDatabase(this);

                boolean duplicates = false;

                for (String fruit : fruitStrings) {
                    try {
                        db.fruitDao().insertAll(new Fruit(fruit));
                    } catch (SQLiteConstraintException e) {
                        duplicates = true;
                    }
                }

                String toastMsg = "saved";
                if (duplicates) {
                    toastMsg = "duplicates not saved";
                }
                Toast.makeText(MainActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
                break;

            case R.id.main_btn_fruits_read:
                intent.putExtra("mode", 2);
                startActivity(intent);
                break;

            case R.id.main_btn_fruits_delete:
                db = AppDatabase.getAppDatabase(this);
                db.fruitDao().deleteAll();
                Toast.makeText(MainActivity.this, "Records deleted", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }
}
