package ch.hevs.storage;

import java.io.FileOutputStream;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	public void onClick(View v){
		Intent intent = new Intent(MainActivity.this, ShowData.class);

		switch (v.getId()) {
		case R.id.main_btn_cantons_save:  // 1.	Save it in a SharedPreference object
            String [] cantons = getResources().getStringArray(R.array.cantons_array);

            // to implement

			Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();;
			break;

		case R.id.main_btn_cantons_read:
			intent.putExtra("mode", 0);
			startActivity(intent);
			break;

		case R.id.main_btn_planets_save:  // 2.	Save it in an internal file
            String [] planets = getResources().getStringArray(R.array.planets_array);

            // to implement

			Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();;
			break;

		case R.id.main_btn_planets_read:
			intent.putExtra("mode", 1);
			startActivity(intent);
			break;

		case R.id.main_btn_fruits_save:  // 3.	Save it in a database
            String [] fruits = getResources().getStringArray(R.array.fruits_array);

            // to implement

			Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();;
			break;

		case R.id.main_btn_fruits_read:
			intent.putExtra("mode", 2);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
