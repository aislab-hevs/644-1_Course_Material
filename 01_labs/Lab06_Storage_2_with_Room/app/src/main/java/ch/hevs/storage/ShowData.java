package ch.hevs.storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import ch.hevs.db.AppDatabase;
import ch.hevs.db.Fruit;


public class ShowData extends Activity {

	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_data);
		TextView title = findViewById(R.id.show_data_txt_title);
		Intent intent = getIntent();
		ArrayList<String> array;
		int mode = intent.getIntExtra("mode", -1);		

		switch (mode) {
		case 0:
			title.setText("Shared Preferences");
			readSharedPreferences();
			break;

		case 1:
			title.setText("File");
			readFile();
			break;
			
		case 2:
			title.setText("SQLite/Room");
			readSQL();
			break;

		default:
			break;
		}
	}

	private void readSharedPreferences(){
		SharedPreferences sharedPref = getSharedPreferences("cantons", Context.MODE_PRIVATE);
		Map<String, ?> keys = sharedPref.getAll();
		generateListView(new ArrayList<>((Collection<? extends String>) keys.values()));
	}
	
	private void readFile(){
		String fileName = "planets.txt";
		String [] temp;
		int ch;

		StringBuffer fileContent = new StringBuffer("");
		FileInputStream fis;

		try {
			fis = openFileInput(fileName);
			try {
				while( (ch = fis.read()) != -1)
					fileContent.append((char)ch);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		temp = fileContent.toString().split(";");
		generateListView(new ArrayList<>(Arrays.asList(temp)));
	}
	
	private void readSQL(){
		AppDatabase db = AppDatabase.getAppDatabase(this);

		List<Fruit> fruits = db.fruitDao().getAll();
		List<String> array = new ArrayList<>();
		for (Fruit fruit : fruits)
			array.add(fruit.getFruitName());
		generateListView(array);
	}
	
	private void generateListView(List<String> array){
		//sort arraylist
		Collections.sort(array);

		// Get ListView object from xml
		listView = findViewById(R.id.listview);

		ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, array);

		// Assign adapter to ListView
		listView.setAdapter(adapter); 
	}
} 


