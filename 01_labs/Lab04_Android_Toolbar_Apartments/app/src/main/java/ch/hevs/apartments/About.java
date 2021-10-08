package ch.hevs.apartments;

import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;

import androidx.core.app.NavUtils;

public class About extends AppCompatActivity {
	private String address;
	private String postcode;
	private String place;
	private String nbr_of_rooms;
	private String price;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		// Show the Up button in the action bar.
		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(myToolbar);

		// Get a support ActionBar corresponding to this toolbar
		ActionBar ab = getSupportActionBar();

		// Enable the Up button
		ab.setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		address = intent.getStringExtra("address");
		postcode = intent.getStringExtra("postcode");
		place = intent.getStringExtra("place");
		nbr_of_rooms = intent.getStringExtra("nbr_of_rooms");
		price = intent.getStringExtra("price");
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//

			//add the infos about the apartment to the intent -> so we can show the last intruduced apartment

			Intent intent = new Intent(About.this, MainActivity.class);
			intent.putExtra("address", address);
			intent.putExtra("postcode", postcode);
			intent.putExtra("place", place);
			intent.putExtra("nbr_of_rooms", nbr_of_rooms);
			intent.putExtra("price", price);

			About.this.startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
