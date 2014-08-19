package com.androidApp.mozartinpocket;

import java.io.IOException;


import com.androidApp.mozartinpocket.R;
import com.androidApp.mozartinpocket.compose.ComposeActivity;
import com.androidApp.mozartinpocket.post.PostActivity;
import com.mozartinpocket.entities.Music;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A fragment representing a single musics detail screen. This fragment is
 * either contained in a {@link MusicsListActivity} in two-pane mode (on
 * tablets) or a {@link MusicsDetailActivity} on handsets.
 */
public class MusicsDetailFragment extends Fragment implements SeekBar.OnSeekBarChangeListener{
	public static String ARG_ITEM_ID = "id";
	public static final String SELECTED_MUSIC_FILE_NAME = "selected_music_file_name";
	public static final String SELECTED_MUSIC_POSITION = "selected_position";
	private Button playButton;
	private Button editButton;
	private Button deleteButton;
	private SeekBar musicBar;
	private Handler mHandler = new Handler();
	private int status;
	private String absoluteName;
	private MediaPlayer mediaPlayer;
	private Music musicItem;
	private String musicName;
	private int selectedPosition;
	//private DataBaseManager dbManager = new DataBaseManager(getActivity());
	private Callbacks myCallbacks = musicsDetailCallback;
	private Callbacks2 callback2 = detailCallback2;
	public interface Callbacks{
		public void deleteMusicFile(String musicName, int position);
	}
	private static Callbacks musicsDetailCallback = new Callbacks(){ 
		public void deleteMusicFile(String musicName, int position){}
	};
	public interface Callbacks2{
		public void changeSelectedPosition(String musicName, int position);
	}
	private static Callbacks2 detailCallback2 = new Callbacks2(){
		public void changeSelectedPosition(String musicName, int position){}
	};
	public MusicsDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(SELECTED_MUSIC_FILE_NAME)) {
			musicItem = MusicItemList.musicMap.get(getArguments().getString(SELECTED_MUSIC_FILE_NAME));
			selectedPosition = getArguments().getInt(SELECTED_MUSIC_POSITION);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_musics_detail,
				container, false);

		if (musicItem != null) {
			TextView text_authorName = (TextView) rootView.findViewById(R.id.txtView_music_file_detail_fragment_authorname);
			text_authorName.setTextSize(20);
			text_authorName.setText("Author: "+musicItem.getAuthorUsername());

			TextView text_musicName = (TextView) rootView.findViewById(R.id.txtView_music_file_detail_fragment_musicname);
			text_musicName.setText("Music Name: "+musicItem.getName());
			text_musicName.setTextSize(20);

			TextView text_date = (TextView) rootView.findViewById(R.id.txtView_music_file_detail_fragment_date);
			text_date.setText("Composed Date: "+musicItem.getDate());
			text_date.setTextSize(20);

			TextView text_description = (TextView) rootView.findViewById(R.id.txtView_music_file_detail_fragment_description);
			text_description.setText("Description: "+musicItem.getDescription());
			text_description.setTextSize(20);

			editButton = (Button) rootView.findViewById(R.id.button_edit_music_detail_fragment);	
			editButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),ComposeActivity.class);
					intent.putExtra(SELECTED_MUSIC_FILE_NAME, musicItem.getName());
					startActivity(intent);	
				}
			});
		}
		deleteButton = (Button) rootView.findViewById(R.id.button_delete_music_detail_fragment);
		deleteButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				myCallbacks.deleteMusicFile(musicName, selectedPosition);
			}
		});

		playButton = (Button) rootView.findViewById(R.id.button_play_music_detail_fragment);
		status = 0;
		musicName = musicItem.getName();
		absoluteName = ComposeActivity.PATH + "/"+musicName + ".mid";
		setPlayButtonFunction();

		musicBar = (SeekBar) rootView.findViewById(R.id.seek_bar_progress);
		musicBar.setOnSeekBarChangeListener(this);


		ScrollView ll = (ScrollView) rootView.findViewById(R.id.musics_detail_container);
		ll.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				int pointerCount = event.getPointerCount();
				int action = event.getActionMasked();
				String actionString = "";
				int pointerIndex = ((event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT);
				if(pointerIndex >=pointerCount){
					return false;
				}
				if(pointerCount>1){
					for(int i = 0;i<pointerCount;i++){
						int activePosition = event.getPointerId(i);
						if(activePosition == -1 ){
							return false;
						}
					}
					switch (action)
					{
					case MotionEvent.ACTION_DOWN:
						actionString = "DOWN";
						Log.w("Multitouch","down");
						break;
					case MotionEvent.ACTION_UP:
						actionString = "UP";
						Log.w("Multitouch","up");
						break;	
					case MotionEvent.ACTION_POINTER_DOWN:
						actionString = "DOWN";
						Log.w("Multitouch","pointer down");
						break;
					case MotionEvent.ACTION_POINTER_UP:
						actionString = "UP";
						Log.w("Multitouch","pointer up");
						break;
					case MotionEvent.ACTION_MOVE:
						//actionString = "MOVE";
						break;
					default:
						actionString = "";
					}

					if(pointerCount>1 && pointerCount<=2){
						Log.w("actionString",actionString);
						if(actionString.equals("DOWN")){

							callback2.changeSelectedPosition(musicItem.getName(), selectedPosition);
							return true;
						}
					}
					else{
						if(pointerCount>2){
							Log.w("Pointer >2","toPost");
							Intent intent = new Intent(getActivity(),PostActivity.class);
							intent.putExtra(SELECTED_MUSIC_FILE_NAME, musicItem.getName());
							intent.putExtra(SELECTED_MUSIC_POSITION, selectedPosition);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							Toast.makeText(getActivity(), "multitouch detected"+actionString, Toast.LENGTH_SHORT).show();
							startActivity(intent);
							return true;
						}

					}
				}
				return false;
			}
		});
		return rootView;
	}
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		if ((activity instanceof Callbacks)) {
			myCallbacks = (Callbacks) activity;
			callback2 = (Callbacks2) activity;
		}
	}
	@Override 
	public void onDetach(){
		super.onDetach();
		myCallbacks = musicsDetailCallback;
		callback2 = detailCallback2;
		stopMediaPlayer();
	}

	/*	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME ){
			if(mediaPlayer!=null && mediaPlayer.isPlaying()){
				mHandler.removeCallbacks(mUpdateTimeTask,mediaPlayer);
				mHandler.removeCallbacks(mUpdateTimeTask);
				mediaPlayer.stop();
				return true;
			}
		}
		return super.getActivity().onKeyDown(keyCode, event);
	}*/
	private void setPlayButtonFunction(){
		playButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view){

				Log.w("PlayMIDI",absoluteName);
				switch(status){
				case 0:{
					playMIDI(absoluteName);
					status = 2; //playing
					((MusicsListActivity) getActivity()).status = 2;
					//playButton.setImageResource(R.drawable.icon_pause);
					break;
				}
				case 1:{
					if(mediaPlayer!=null && !mediaPlayer.isPlaying())
						mediaPlayer.start();
					status = 2;
					((MusicsListActivity) getActivity()).status = 2;
					//playButton.setImageResource(R.drawable.icon_pause);
					break;
				}
				case 2:{
					if(mediaPlayer!=null && mediaPlayer.isPlaying())
						mediaPlayer.pause();
					status = 1;
					//playButton.setImageResource(R.drawable.icon_play);
					break;
				}
				}
			}
		});
	}
	public void playMIDI(String absoluteFileName){
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(absoluteFileName);
			mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
					status = 2;
					((MusicsListActivity) getActivity()).status = 2;
					musicBar.setProgress(0);
					musicBar.setMax(100);
					updateProgressBar();

				}
			});
			mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					musicBar.setProgress(100);
					//mHandler.removeCallbacks(mUpdateTimeTask, mp);
					mHandler.removeCallbacks(mUpdateTimeTask);
					mp.stop();
					//mp.release();
					//mp = null;
					status = 0;
				}
			});
			mediaPlayer.prepareAsync();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long totalDuration = mediaPlayer.getDuration();
			long currentDuration = mediaPlayer.getCurrentPosition();

			// Displaying Total Duration time
			//songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
			// Displaying time completed playing
			//songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));

			// Updating progress bar
			int progress = getProcessPercentage(currentDuration, totalDuration);
			//Log.d("Progress", ""+progress);
			musicBar.setProgress(progress);

			// Running this thread after 100 milliseconds
			mHandler.postDelayed(this, 100);
		}
	};

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		// int totalDuration = mediaPlayer.getDuration();
		//updateProgressBar();


	}
	private int getProcessPercentage(long currentDuration, long totalDuration){
		return Math.round(((float)currentDuration)/((float)totalDuration)*100);
	}
	public void updateProgressBar() {
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}
	public void stopMediaPlayer(){
		if(mediaPlayer!=null){
			if(mediaPlayer.isPlaying()){
				mHandler.removeCallbacks(mUpdateTimeTask, mediaPlayer);
				mHandler.removeCallbacks(mUpdateTimeTask);
				mediaPlayer.stop();
				status = 0;
				((MusicsListActivity) getActivity()).status = 0;
			}	

		}

	}

}
