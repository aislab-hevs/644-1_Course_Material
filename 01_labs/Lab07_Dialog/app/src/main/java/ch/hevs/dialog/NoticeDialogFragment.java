package ch.hevs.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class NoticeDialogFragment extends DialogFragment{
	private NoticeDialogListener mListener;


	public interface NoticeDialogListener{
		public void onDialogClick(int idx);
		public void onDialogNegativeClick();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.pick_color)
		.setItems(R.array.string_array_colors, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// The 'which' argument contains the index position
				// of the selected item
				mListener.onDialogClick(which);
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// Send the negative button event back to the host activity
				mListener.onDialogNegativeClick();
			}
		});
		return builder.create();
	}

	@Override
	public void onAttach(Context context){
		super.onAttach(context);

		try{
			mListener = (NoticeDialogListener) context;
		}
		catch(ClassCastException e){
			throw new ClassCastException(context.toString() + " must implement NoticeDialogListener");
		}
	}
}
