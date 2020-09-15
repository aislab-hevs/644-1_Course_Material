package ch.hevs.linearlayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterInfoActivity extends Activity {
	private Button submit;
	private EditText firstName;
	private EditText lastName;
	private EditText address;
	private EditText city;
	private EditText country;
	private EditText birthdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_info);

		submit = findViewById(R.id.btn_submit);
		firstName = findViewById(R.id.et_firstname);
		lastName = findViewById(R.id.et_lastname);
		address = findViewById(R.id.et_address);
		city = findViewById(R.id.et_city);
		country = findViewById(R.id.et_country);
		birthdate = findViewById(R.id.et_birthdate);

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EnterInfoActivity.this, ShowActivity.class);	
				intent.putExtra("firstname", firstName.getText().toString());
				intent.putExtra("lastname", lastName.getText().toString());
				intent.putExtra("address", address.getText().toString());
				intent.putExtra("city", city.getText().toString());
				intent.putExtra("country", country.getText().toString());
				intent.putExtra("birthdate", birthdate.getText().toString());

				EnterInfoActivity.this.startActivity(intent);
			}
		});
	}
}
