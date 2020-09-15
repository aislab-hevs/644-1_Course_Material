package ch.hevs.storage;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
			title.setText("SQLite");
			readSQL();
			break;

		default:
			break;
		}
	}

	private void readSharedPreferences(){
        // to implement
	}
	
	private void readFile(){
        // to implement
	}
	
	private void readSQL(){
        // to implement
	}
	
	private void generateListView(ArrayList<String> array){
		//sort arraylist
		Collections.sort(array);

		// Get ListView object from xml
		listView = findViewById(R.id.listview);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, array);

		// Assign adapter to ListView
		listView.setAdapter(adapter); 
	}
} 


