package ch.hevs.relativelayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Spinner spinner = findViewById(R.id.spinner1);
		Button submit = findViewById(R.id.button1);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.planets_array, android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(adapter);

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				StringBuffer result = new StringBuffer();
				EditText login = findViewById(R.id.editText1);
				EditText password = findViewById(R.id.editText2);
				CheckBox smartphone = findViewById(R.id.checkBox1);
				CheckBox tablet = findViewById(R.id.checkBox2);

				RadioGroup radioGroup = findViewById(R.id.radioGroup1);
				int radioButtonID = radioGroup.getCheckedRadioButtonId();
				View radioButton = radioGroup.findViewById(radioButtonID);
				int idx = radioGroup.indexOfChild(radioButton);

				ToggleButton toggle = findViewById(R.id.toggleButton1);
				Spinner spinner = findViewById(R.id.spinner1);
				SeekBar seekBar = findViewById(R.id.seekBar1);

				result.append("Login: " + login.getText().toString() + "\n");
				result.append("Password:  " + password.getText().toString() + "\n");
				result.append(toggle.isChecked() ? "Toggle: on\n" : "Toggle: off\n");
				result.append(smartphone.isChecked() ? "Smartphone: checked\n" : "");
				result.append(tablet.isChecked() ? "Tablet: checked\n" : "");
				result.append(idx == 0 ? "Gender: Female\n" : "Gender: Male\n");
				result.append("Spinner: " + spinner.getSelectedItem().toString() + "\n");
				result.append("Seek bar: " + seekBar.getProgress() + "\n");

				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
			}
		});
	}
}
