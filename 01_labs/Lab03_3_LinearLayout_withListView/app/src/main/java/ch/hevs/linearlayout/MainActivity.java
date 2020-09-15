package ch.hevs.linearlayout;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final String [] cantons = getResources().getStringArray(R.array.cantons_array);
		ListView list;
	
		ArrayAdapter<String>adapter = new ArrayAdapter<String>(this, R.layout.listview_layout, cantons){

			// Call for every entry in the ArrayAdapter
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view;
				//If View doesn't exist create a new view
				if (convertView == null) {
					// Create the Layout
					LayoutInflater inflater = getLayoutInflater();
					view = inflater.inflate(R.layout.listview_layout, parent, false);
				} else {
					view = convertView;
				}
				//Add Text to the layout
				TextView textView1 = (TextView) view.findViewById(R.id.listview_canton);
				textView1.setText(cantons[position]);
                return view;
			}
		};

		//ListView 
		list = findViewById(R.id.main_listview);
		list.setAdapter(adapter);

		//ListeView handler
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "You have selected: " + cantons[position], Toast.LENGTH_LONG).show();
			}
		});
	}
}
