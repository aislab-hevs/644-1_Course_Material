package ch.hevs.apartments;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity {
	private Button btn_add;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(myToolbar);

		// Get a support ActionBar corresponding to this toolbar
		ActionBar ab = getSupportActionBar();

		// Enable the Up button
		ab.setDisplayHomeAsUpEnabled(true);


		btn_add = (Button) findViewById(R.id.main_btn_add);
		btn_add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "Apartment added", Toast.LENGTH_LONG).show();
				EditText address = (EditText)findViewById(R.id.main_address);
				EditText postcode = (EditText)findViewById(R.id.main_postcode);
				EditText place = (EditText)findViewById(R.id.main_place);
				EditText nbr_of_rooms = (EditText)findViewById(R.id.main_nbr_of_rooms);
				EditText price = (EditText)findViewById(R.id.main_price);

				Intent intent = new Intent(MainActivity.this, ShowApartment.class);
				intent.putExtra("address", address.getText().toString());
				intent.putExtra("postcode", postcode.getText().toString());
				intent.putExtra("place", place.getText().toString());
				intent.putExtra("nbr_of_rooms", nbr_of_rooms.getText().toString());
				intent.putExtra("price", price.getText().toString());
				MainActivity.this.startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		Intent intent = getIntent();
		String address = intent.getStringExtra("address");
		String postcode = intent.getStringExtra("postcode");
		String place = intent.getStringExtra("place");
		String nbr_of_rooms = intent.getStringExtra("nbr_of_rooms");
		String price = intent.getStringExtra("price");

		switch(item.getItemId()){
		case R.id.action_about:
			intent = new Intent(MainActivity.this, About.class);
			intent.putExtra("address", address);
			intent.putExtra("postcode", postcode);
			intent.putExtra("place", place);
			intent.putExtra("nbr_of_rooms", nbr_of_rooms);
			intent.putExtra("price", price);
			MainActivity.this.startActivity(intent);
			break;

		case R.id.action_show:

			intent = new Intent(MainActivity.this, ShowApartment.class);
			intent.putExtra("address", address);
			intent.putExtra("postcode", postcode);
			intent.putExtra("place", place);
			intent.putExtra("nbr_of_rooms", nbr_of_rooms);
			intent.putExtra("price", price);
			MainActivity.this.startActivity(intent);
			break;
		}

		return true;
	}

}
