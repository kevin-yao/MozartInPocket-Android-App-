package com.androidApp.mozartinpocket.compose;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import com.androidApp.mozartinpocket.LoginActivity;
import com.androidApp.mozartinpocket.MusicItemList;
import com.androidApp.mozartinpocket.MusicsDetailFragment;
import com.androidApp.mozartinpocket.R;
import com.androidApp.mozartinpocket.database.DataBaseManager;
import com.androidApp.mozartinpocket.exception.MusicContentEmptyException;
import com.androidApp.mozartinpocket.util.Navigation;
import com.androidApp.mozartinpocket.util.ScreenShotUtils;
import com.androidApp.mozartinpocket.util.TabsControl;
import com.mozartinpocket.entities.Music;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioButton;
import android.widget.Toast;


public class ComposeActivity extends FragmentActivity implements ComposeDialog.Callbacks{
	private MediaPlayer mediaPlayer;
	private WebView composeView;
	private JsInterface jsInterface;
	private DataBaseManager dbManager = new DataBaseManager(this);
	private Context context = this;
	private File sdCard = Environment.getExternalStorageDirectory();
	private static final String TEMP_ON_CONFIGURATION_CHANGE = "saved_temp_on_configuration_change";
	public static final String DIR = "/DCIM/Audio";
	public static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+DIR;
	public static final String RECOVER_CURRENT_FILE_NAME = "recovered_current_file_name";
	public static String fileNameFromDialog;

	public String tempOnConfigurationChange;
	private String currentFileName = null;
	private String textFromJS = null;
	private String uploadFileName;
	public String TAG = "Compose Activity";

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);

		TabsControl.controlTabs(this);
		RadioButton btn = (RadioButton) findViewById(R.id.navigation_bar).findViewById(R.id.main_tab_compose);
		btn.setChecked(true);
		Log.w(TAG,"Compose activity created");

		if(getIntent().hasExtra(MusicsDetailFragment.SELECTED_MUSIC_FILE_NAME)){
			uploadFileName = getIntent().getStringExtra(MusicsDetailFragment.SELECTED_MUSIC_FILE_NAME);
		}
		/*else{
			if(savedInstanceState!=null && savedInstanceState.containsKey(RECOVER_CURRENT_FILE_NAME)){
				String previous = null;
				if ((previous = savedInstanceState.getString(RECOVER_CURRENT_FILE_NAME))!=null){
					uploadFileName = previous;
				}
			}
		}*/
		if(uploadFileName!=null){
			Log.w("Upload File on Create",uploadFileName);
		}

		if(MusicItemList.musicItemList.isEmpty()){
			dbInitialize();
			Log.w(TAG,"dbInitialize");
		}

		composeView = (WebView) findViewById(R.id.webview_compose);
		WebSettings webSettings = composeView.getSettings();
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setJavaScriptEnabled(true);

		jsInterface = new JsInterface();
		composeView.loadUrl("file:///android_asset/css_piano.html");
		composeView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		composeView.setWebViewClient(new composeViewClient());
		composeView.addJavascriptInterface(jsInterface, "jsInterface");
		if(savedInstanceState!=null){
			restoreDataFromBundle(savedInstanceState);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.control, menu);
		return true;
	}

	private class composeViewClient extends WebViewClient{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url){
			//if(Uri.parse(url).getHost().equals("http:///.www.mlcalc.com/")){
			//	return(false);
			//}

			Intent intent = new Intent( Intent.ACTION_VIEW,Uri.parse(url));
			startActivity(intent);
			return(true);
		}
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
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_logout:
			Navigation.logout(this);
			return true;
		case R.id.screenShot:
			ScreenShotUtils.screenShot(this);
			return true;
		case R.id.userProfile:
			Navigation.editProfile(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		composeView.saveState(outState);
		outState.putString(TEMP_ON_CONFIGURATION_CHANGE,tempOnConfigurationChange); 
		outState.putString(RECOVER_CURRENT_FILE_NAME,currentFileName);

	}
	@Override
	public void onStop(){
		super.onStop();
		composeView.loadUrl("javascript:window.jsInterface.uploadFile()");
		Log.w("On Stop","Load URl");
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Do something here
	}

	public void restoreDataFromBundle(Bundle savedInstanceState){
		tempOnConfigurationChange = savedInstanceState.getString(TEMP_ON_CONFIGURATION_CHANGE);
	}
	//Javascript Interface
	class JsInterface{
		@JavascriptInterface
		public void getCharacter(String s){
			Log.w("Call from JsInterface",s);
			playSong("note/"+s+".mp3");
		}
		@JavascriptInterface
		//you should add this @ before any methods that you want to call from js
		public void getContentText(String s){
			textFromJS = s;
			
			try{
				if(textFromJS.isEmpty() || textFromJS.equals("null") || textFromJS.equals("")){
					Log.w("textFromJs",textFromJS + "haha");
					throw new MusicContentEmptyException( context);
				}
				ComposeDialog fileNameDialog = new ComposeDialog();
				fileNameDialog.show(getSupportFragmentManager(), "inputFileNameDialog");
			}
			catch(MusicContentEmptyException e){
				e.exceptionFix();
			}
			// it's not useful, the callback will never come back
			//saveMusicFile(textFromJS,fileNameFromDialog, description);
		}	
		@JavascriptInterface
		public String uploadFile(){
			if(uploadFileName!=null && !uploadFileName.equals("null"))
				makeToast();
			Log.w("JsInterface","uplodeFIle"+uploadFileName);
			String output = readFromFile(uploadFileName);
			output = output.substring(4);
			Log.w("readFromFile",output);
			currentFileName = uploadFileName;
			return(output);
		}
		@JavascriptInterface
		public void playMusicScore(String input){
			Log.w("JsInterface","PlayMusicScore");
			MusicScorePlayer msPlayer = new MusicScorePlayer("myfirstscore", input);
			msPlayer.playMusicScore();
			msPlayer.playMIDI();
		}
		@JavascriptInterface
		public void saveTemp(String s){
			tempOnConfigurationChange = s;
			Log.w("jsinterface", "saveTemp");
		}

	}
	//Callbacks to get fileName and Description from Dialog
	public void getFromDialog(String fileName, String description){
		fileNameFromDialog = fileName;
		//Log.w("textfromJS",textFromJS);
		saveMusicFile(textFromJS,fileNameFromDialog,description);


	}
	public void makeToast(){
		Toast.makeText(getApplicationContext(),"File "+ uploadFileName +" uploaded", Toast.LENGTH_LONG).show();
	}
	// populate Music Item and save to database
	// get fileName from Dialog
	// fileName.txt --> score
	// fileName.mid --> music file
	// user information populated
	@SuppressLint("SimpleDateFormat")
	public void saveMusicFile(String input, String fileName, String description){
		//populate new music item
		
		Music musicItem = new Music();
		musicItem.setAuthorUsername(LoginActivity.currentUser.getUsername());
		String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
		musicItem.setDate(formattedDate);
		musicItem.setDescription(description);
		musicItem.setName(fileName);
		musicItem.setSavePath(fileName);
		
		// write musicName.txt to SD card
		writeToSDCard(input, fileName);
		Log.w("write to SD card",input);
		// write musicName.mid to SD card
		MusicScorePlayer msPlayer = new MusicScorePlayer(fileName, input);
		msPlayer.playMusicScore();
		//msPlayer.playMIDI();
		
		currentFileName = fileName;
		makeToast("Music File Completely Saved");
		
		// add new Music item to database
		dbManager.open();
		dbManager.addMusic(musicItem);
		MusicItemList.musicItemList.add(fileName);
		MusicItemList.musicMap.put(fileName, musicItem);
		dbManager.close();
	}
	private void writeToSDCard(String input, String fileName){
		File directory = new File(sdCard.getAbsolutePath() + DIR);
		directory.mkdirs();
		File musicFile = new File(directory, fileName);
		Log.w(TAG,"MusicFile "+musicFile.getAbsolutePath() +" made");
		Log.w("write to sd card",input);
		try{
			PrintWriter writer = new PrintWriter(musicFile);
			writer.write(input);
			writer.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	// Read txt line from File
	private String readFromFile(String fileName){
		String readline = null;
		String thisline = null;
		File directory = new File(sdCard.getAbsolutePath() + DIR);
		fileName = directory.getAbsolutePath() +"/"+ fileName;
		Log.w("Chosen uploadFile",fileName);
		try{
			FileReader reader = new FileReader(fileName);

			BufferedReader bReader = new BufferedReader(reader);
			while((thisline = bReader.readLine())!=null){
				readline = readline+thisline;
			}
			bReader.close();
			reader.close();

			return(readline);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return(null);
	}
	public void playSong(String fileName){
		try{
			AssetFileDescriptor afd;
			afd = getAssets().openFd(fileName);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
			mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){

				@Override
				public void onPrepared(MediaPlayer mp) {
					mediaPlayer.start();

				}

			});
			mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.stop();
					mp.release();
					mp = null;
				}
			});
			mediaPlayer.prepareAsync();


		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void makeToast(String text){
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
	}
	//populate database 
	private void dbInitialize(){
		dbManager.open();
		ArrayList<Music> musicList =dbManager.getMusicList(LoginActivity.currentUser.getUsername());
		if(musicList == null){
			Log.w("From Database","currently you do not have any music files");
		}
		else{
			Iterator<Music> it = musicList.iterator();
			while(it.hasNext()){
				Music music  = it.next();
				String musicName = music.getName();
				Log.w("From Database",musicName);
				MusicItemList.musicItemList.add(musicName);
				MusicItemList.musicMap.put(musicName,music);
			}
		}
		dbManager.close();
	}
}

