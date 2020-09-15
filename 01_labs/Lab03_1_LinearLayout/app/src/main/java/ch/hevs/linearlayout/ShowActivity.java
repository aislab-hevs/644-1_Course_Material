package ch.hevs.linearlayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ShowActivity extends Activity {
	private TextView firstname;
	private TextView lastname;
	private TextView address;
	private TextView city;
	private TextView country;
	private TextView birthdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show);
		
		Intent intent = getIntent();
		
		firstname = findViewById(R.id.tv_firstName);
		lastname = findViewById(R.id.tv_lastName);
		address = findViewById(R.id.tv_address);
		city = findViewById(R.id.tv_city);
		country = findViewById(R.id.tv_country);
		birthdate = findViewById(R.id.tv_birthdate);

		firstname.setText(intent.getStringExtra("firstname"));
		lastname.setText(intent.getStringExtra("lastname"));
		address.setText(intent.getStringExtra("address"));
		city.setText(intent.getStringExtra("city"));
		country.setText(intent.getStringExtra("country"));
		birthdate.setText(intent.getStringExtra("birthdate"));
	}
}
