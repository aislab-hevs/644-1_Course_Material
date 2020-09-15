package ch.hevs.dialog;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements NoticeDialogFragment.NoticeDialogListener {
	private Button btn_change;
	private View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		btn_change = findViewById(R.id.main_btn_showAlert);

		btn_change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showNoticeDialog();
			}
		});
	}

	public void showNoticeDialog(){
		DialogFragment dialog = new NoticeDialogFragment();
		dialog.show(getFragmentManager(), "NoticeDialogFragment");
	}

	@Override
	public void onDialogClick(int idx) {
		// TODO Auto-generated method stub
		String [] colors = getResources().getStringArray(R.array.string_array_colors);
		Toast.makeText(MainActivity.this, "Color changed to " + colors[idx], Toast.LENGTH_SHORT).show();
		view = findViewById(android.R.id.content);

		switch(idx){
		case 0: 
			view.setBackgroundColor(Color.RED);
			break;

		case 1: 
			view.setBackgroundColor(Color.BLUE);
			break;

		case 2: 
			view.setBackgroundColor(Color.BLACK);
			break;

		case 3: 
			view.setBackgroundColor(Color.YELLOW);
			break;

		case 4: 
			view.setBackgroundColor(Color.GREEN);
			break;

		case 5:
			view.setBackgroundColor(Color.WHITE);
			break;

		default:
			view.setBackgroundColor(Color.WHITE);
			break;
		}
	}

	@Override
	public void onDialogNegativeClick() {
		Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
	}
}
