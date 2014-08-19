package com.androidApp.mozartinpocket.compose;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ComposeDialog extends DialogFragment{

	private String fileName = null;
	private String descript = null;
	private Callbacks myCallbacks = dialogCallbacks;

	public interface Callbacks{
		public void getFromDialog(String s, String d);
	}
	public static Callbacks dialogCallbacks = new Callbacks(){
		@Override
		public void getFromDialog(String fileName, String description){
		}
	};
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){

		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Please enter file name and description");
		final EditText inputfileName = new EditText(getActivity());
		final EditText description = new EditText(getActivity());
		final TextView txt1 = new TextView(getActivity());
		txt1.setText("Please input file name:");
		txt1.setTextSize(20);
		final TextView txt2 = new TextView(getActivity());
		txt2.setText("Description:");
		txt1.setTextSize(20);
		final TextView txt3 = new TextView(getActivity());
		txt3.setTextSize(16);
		txt3.setText("Music Name can not be empty");
		txt3.setTextColor(Color.parseColor("#F62217"));
		LinearLayout ll =new LinearLayout(getActivity());
		ll.setOrientation(LinearLayout.VERTICAL);

		ll.addView(txt1);
		ll.addView(inputfileName);
		ll.addView(txt3);

		ll.addView(txt2);
		ll.addView(description);


		builder.setView(ll);
		inputfileName.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().length()>0){
					txt3.setText("");
				}
				else{
					txt3.setText("Music Name can not be empty");
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				if(s.toString().length()==0){
					txt3.setText("Music Name can not be empty");
				}
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

		builder.setPositiveButton("ok",new DialogInterface.OnClickListener(){
			@Override 
			public void onClick(DialogInterface dialog, int whichButton){
				{

					fileName = inputfileName.getText().toString();
					if(!fileName.isEmpty()){
						descript = description.getText().toString();
						myCallbacks.getFromDialog(fileName,descript);
						Log.w("dialog",fileName + " // " + descript);
					}
					else{
						dialog.dismiss();
						ComposeDialog fileNameDialog = new ComposeDialog();
						fileNameDialog.show(getActivity().getSupportFragmentManager(), "inputFileNameDialog");
					}
				}

			}
		});
		builder.setNegativeButton("cancel", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int whichButton){
				dialog.dismiss();
			}
		});

		return builder.create();
	}

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		myCallbacks = (Callbacks) activity;
	}
	@Override 
	public void onDetach(){
		super.onDetach();
		myCallbacks = dialogCallbacks;
	}
}
