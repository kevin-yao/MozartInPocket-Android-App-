package com.androidApp.mozartinpocket.share;

import java.io.File;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidApp.mozartinpocket.MusicItemList;
import com.androidApp.mozartinpocket.MusicsDetailFragment;
import com.androidApp.mozartinpocket.R;
import com.mozartinpocket.entities.Music;

public class ShareDetailFragment extends MusicsDetailFragment {
	public static final String SELECTED_MUSIC_FILE_NAME = "selected_music_file_name";
	
	private String path = Environment.getExternalStorageDirectory().getAbsolutePath()+DIR;
	public static final String DIR = "/DCIM/Audio";

	protected static final int REQUEST_ENABLE_BT = 1;
	
	private Music musicItem;
	public ShareDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(MusicsDetailFragment.SELECTED_MUSIC_FILE_NAME)) {
			musicItem = MusicItemList.musicMap.get(getArguments().getString(MusicsDetailFragment.SELECTED_MUSIC_FILE_NAME));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_share_detail,
				container, false);
		// Show the dummy content as text in a TextView.
		if (musicItem != null) {
			TextView text_authorName = (TextView) rootView.findViewById(R.id.txtView_share_detail_fragment_authorname);
			text_authorName.setTextSize(20);
			text_authorName.setText("Author: "+musicItem.getAuthorUsername());

			TextView text_musicName = (TextView) rootView.findViewById(R.id.txtView_share_detail_fragment_musicname);
			text_musicName.setText("Music Name: "+musicItem.getName());
			text_musicName.setTextSize(20);

			TextView text_date = (TextView) rootView.findViewById(R.id.txtView_share_detail_fragment_date);
			text_date.setText("Composed Date: "+musicItem.getDate());
			text_date.setTextSize(20);

			TextView text_description = (TextView) rootView.findViewById(R.id.txtView_share_detail_fragment_description);
			text_description.setText("Description: "+musicItem.getDescription());
			text_description.setTextSize(20);

			Button shareButton = (Button) rootView.findViewById(R.id.button_share_share);	
			shareButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String filename = path + "/" + musicItem.getName() + ".mid";
					File f = new File(filename);
					Intent transfer = new Intent();
					transfer.setAction(Intent.ACTION_SEND);
					transfer.setType("audio/mid");
					transfer.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
					startActivity(transfer);
					BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
					boolean hasBluetooth = true;
					if (mBluetoothAdapter == null) {
					    // Device does not support Bluetooth
						hasBluetooth = false;
						Toast.makeText(getActivity().getApplicationContext(), "Your device doesn't support Bluetooth", Toast.LENGTH_LONG).show();
					}
					
					if(hasBluetooth && !mBluetoothAdapter.isEnabled()) {
						
						Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
					    
					}
				}
			});
		}



		return rootView;
	}

}
