package com.androidApp.mozartinpocket.post;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidApp.mozartinpocket.GPSTracker;
import com.androidApp.mozartinpocket.LoginActivity;
import com.androidApp.mozartinpocket.MusicItemList;
import com.androidApp.mozartinpocket.MusicsDetailFragment;
import com.androidApp.mozartinpocket.R;
import com.androidApp.mozartinpocket.compose.ComposeActivity;
import com.androidApp.mozartinpocket.database.DataBaseManager;
import com.androidApp.mozartinpocket.sensor.ShakeListener;
import com.androidApp.mozartinpocket.sensor.ShakeListener.OnShakeListener;
import com.mozartinpocket.entities.Music;
import com.mozartinpocket.entities.Post;
public class PostDetailFragment extends MusicsDetailFragment {
	public static final String SELECTED_MUSIC_FILE_NAME = "selected_music_file_name";
	public static final String POST_COMMENT = "comment";
	public static final String POST_LOCATION = "post_location";
	private Music musicItem;
	private Context context;
	ShakeListener mShakeListener;
	private String URL=LoginActivity.GlobalURL+"RecieveMusic";
	private String URL1=LoginActivity.GlobalURL+"RecievePost";
	private ProgressBar progressBar;
	private UploadMusicTask uploadMusicTask=null;
	private static String nullFlag="*$#";
	private View fragmentRootView;
	private EditText post_comment;
	public PostDetailFragment(){		
	}
	//public PostDetailFragment(Context context) {
	//	this.context = context;
	//}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = getActivity().getApplicationContext();
		if (getArguments().containsKey(SELECTED_MUSIC_FILE_NAME)) {
			musicItem = MusicItemList.musicMap.get(getArguments().getString(SELECTED_MUSIC_FILE_NAME));
		}
		mShakeListener = new ShakeListener(getActivity());
		mShakeListener.setOnShakeListener(new Shake());
	}
	@Override  
	public void onPause()  
	{  
		super.onPause();  
		mShakeListener.stop();
	}  
	@Override 
	public void onResume()  
	{  
		super.onResume();  
		mShakeListener.start();
	} 

	int i=0;
	class Shake implements OnShakeListener
	{
		public void onShake()
		{
			if((i%100)==0){	
				/*Music music = new Music();
				music.setName("mozart2");
				music.setDescription("light music");
				music.setAuthorUsername("kangping");
				music.setSavePath("langlang");*/
				Post post= newPost();
				/*post.setMusicName("mozart");
				post.setAuthorUsername("wanger");
				post.setComment("dsfaf fdafdsa fdsa fadsa");
				post.setLatitude(123.12312);
				post.setLongitude(21.321212);*/
				//post.setMusicUrl("langlang");
				//post.setUserPhotoUrl("kangping");
				//DataBaseManager db= new DataBaseManager(getActivity());
				//db.open();
				//db.addMusic(musicItem);
				//Music music1= db.getMusic("mozart");
				//Log.i("music",music1.getDescription());
				if(uploadMusicTask!=null){
					uploadMusicTask.cancel(true);
				}
				uploadMusicTask= new UploadMusicTask();
				uploadMusicTask.execute(musicItem, post);
			}
			i++;
		}	
	} 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_post_detail,
				container, false);
		// Show the dummy content as text in a TextView.
		if (musicItem != null) {
			TextView text_authorName = (TextView) rootView.findViewById(R.id.txtView_post_detail_fragment_authorname);
				text_authorName.setTextSize(20);
				text_authorName.setText("Author: "+musicItem.getAuthorUsername());
				
				TextView text_musicName = (TextView) rootView.findViewById(R.id.txtView_post_detail_fragment_musicname);
				text_musicName.setText("Music Name: "+musicItem.getName());
				text_musicName.setTextSize(20);
				
				TextView text_date = (TextView) rootView.findViewById(R.id.txtView_post_detail_fragment_date);
				text_date.setText("Composed Date: "+musicItem.getDate());
				text_date.setTextSize(20);
				
				TextView text_description = (TextView) rootView.findViewById(R.id.txtView_post_detail_fragment_description);
				text_description.setText("Description: "+musicItem.getDescription());
				text_description.setTextSize(20);
				
				
				
				post_comment = (EditText) rootView.findViewById(R.id.editTXT_post_comment);
				
				Button postButton = (Button) rootView.findViewById(R.id.button_post_post);	
				postButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						//String postText = post_comment.getText().toString();
//						GPSTracker gpsTracker = new GPSTracker(getActivity());
//						String longitude = Double.toString(gpsTracker.getlongitude());
//						String latitude = Double.toString(gpsTracker.getlatitude());
						/*Music music = new Music();
						music.setName("mozart2");
						music.setDescription("light music");
						music.setAuthorUsername("kangping");*/
						//musicItem.setSavePath("langlang");
						Post post= newPost();
						/*post.setMusicName("mozart");
						post.setAuthorUsername("wanger");
						post.setComment("dsfaf fdafdsa fdsa fadsa");
						post.setLatitude(123.12312);
						post.setLongitude(21.321212);
						post.setMusicUrl("langlang");
						post.setUserPhotoUrl("kangping");*/
						DataBaseManager db= new DataBaseManager(getActivity());
						db.open();
						db.addMusic(musicItem);
						/*Music music1= db.getMusic("mozart");
						Log.i("music",music1.getDescription());*/
						if(uploadMusicTask!=null){
							uploadMusicTask.cancel(true);
						}
						uploadMusicTask= new UploadMusicTask();
						uploadMusicTask.execute(musicItem, post);
						//start the post activity
					//	Intent intent = new Intent(getActivity(),DoPostActivity.class);
					//	intent.putExtra(SELECTED_MUSIC_FILE_NAME, musicItem.getName());
					//	intent.putExtra(POST_COMMENT, postText);
					//	intent.putExtra(POST_LOCATION, longitude + " " + latitude);
					//	startActivity(intent);	
					}
				});
			}
		fragmentRootView = rootView;
		return rootView;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (uploadMusicTask != null
				&& uploadMusicTask.getStatus() != UploadMusicTask.Status.FINISHED) {
			uploadMusicTask.cancel(true);
			uploadMusicTask = null;
		}
	}
	
	 private class UploadMusicTask extends AsyncTask<Object, Integer, Integer>{ 
			@Override
			protected void onPreExecute() {
				progressBar = (ProgressBar) fragmentRootView.findViewById(R.id.progressBar3);
				progressBar.setVisibility(View.VISIBLE);
				Button btn=(Button) fragmentRootView.findViewById(R.id.button_post_post);
				btn.setClickable(false);
				super.onPreExecute();
			}
						
			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
			}
			
		 @Override
		 protected Integer doInBackground(Object... params) {		 
			 int status1=uploadMusic((Music) params[0]);
			 Log.i("sta1", String.valueOf(status1));			 
			 int status2=uploadPost((Post) params[1]);
			 Log.i("sta2", String.valueOf(status2));
			 int status=0;
			 if(status1==1&&status2==1){
				status=1; 
			 }else if(status1==2||status2==2){
				 status=2;
			 }else if(status1==3){
				 status=3;
			 }else{
				 status=0;
			 }
			 return status;
		 }
		 
		 @Override
		    protected void onPostExecute(Integer status) {
			 progressBar.setVisibility(View.INVISIBLE);
			 Button btn=(Button) fragmentRootView.findViewById(R.id.button_post_post);
			 btn.setClickable(true);
			 if(status==1){
				 Toast.makeText(context, "Upload success!", Toast.LENGTH_LONG).show();
			 }else if(status==2){
				 Toast.makeText(context, "Upload fail!", Toast.LENGTH_LONG).show();
			 }else if(status==0){
				 Toast.makeText(context, "Please check your network!", Toast.LENGTH_LONG).show();
			 }else if(status==3){
				 Toast.makeText(context, "This music file doesn't exists!", Toast.LENGTH_LONG).show();
				}	
			 else if(status==4){
				 Toast.makeText(context, "System error!", Toast.LENGTH_LONG).show();
				}
		    }
	 
		public int uploadMusic(Music music){
			int status = 0 ;
			try{
				HttpPost post = new HttpPost(URL);  
				HttpClient client = new DefaultHttpClient();		
				File musicFile = new File(Environment.getExternalStorageDirectory()
						+ ComposeActivity.DIR+"/"+music.getSavePath()+".mid");
				if (!musicFile.exists()) {
					musicFile.mkdirs();
				}
				MultipartEntity mulentity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				mulentity.addPart("name", new StringBody(music.getName(), Charset.forName("UTF-8")));
				mulentity.addPart("date", new StringBody((music.getDate()==null)?nullFlag:music.getDate(),Charset.forName(HTTP.UTF_8)));
				mulentity.addPart("description", new StringBody((music.getDescription()==null)?nullFlag:music.getDescription(),Charset.forName(HTTP.UTF_8)));
				mulentity.addPart("savePath", new StringBody((music.getSavePath()==null)?nullFlag:music.getSavePath(),Charset.forName(HTTP.UTF_8)));
				mulentity.addPart("authorUsername", new StringBody((music.getAuthorUsername()==null)?nullFlag:music.getAuthorUsername(),Charset.forName(HTTP.UTF_8)));		  
				FileBody musicBody = new FileBody(musicFile); 
				mulentity.addPart("music", musicBody);
				post.setEntity(mulentity);
				HttpResponse response = client.execute(post);	        
				InputStream is  = response.getEntity().getContent();           
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String index = null;
				index=new String(reader.readLine().getBytes(), "UTF-8");
				is.close();
				if(response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK)
				{
					status=0;
				}
				if(index.equals("SUCCESS")){
					status=1;
				}else if(index.equals("FAIL")){
					status=2;
				}			
			}catch(FileNotFoundException e){
				status=3;
				e.printStackTrace();
			}catch(Exception e){
				status=4;
				e.printStackTrace();
			}
			return status;
		}
		
		public int uploadPost(Post post){
			int status = 0 ;
			try{
				HttpPost httppost = new HttpPost(URL1);  
				HttpClient client = new DefaultHttpClient();
				List <NameValuePair> params = new ArrayList <NameValuePair>(); 
				params.add(new BasicNameValuePair("music_name", post.getMusicName()));
				Log.i("Tag",post.getAuthorUsername());
				params.add(new BasicNameValuePair("comment", post.getComment()));
				params.add(new BasicNameValuePair("authorUsername", post.getAuthorUsername()));
				params.add(new BasicNameValuePair("date", post.getDate()));
				params.add(new BasicNameValuePair("musicUrl", post.getMusicUrl()));
				params.add(new BasicNameValuePair("userPhotoUrl", post.getUserPhotoUrl()));
				params.add(new BasicNameValuePair("latitude", String.valueOf(post.getLatitude())));
				params.add(new BasicNameValuePair("longitude", String.valueOf(post.getLongitude())));
				HttpEntity requestHttpEntity = new UrlEncodedFormEntity(params);  
				httppost.setEntity(requestHttpEntity);
				HttpResponse response = client.execute(httppost);	        
				InputStream is  = response.getEntity().getContent();           
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String index = null;
				index=new String(reader.readLine().getBytes(), "UTF-8");
				is.close();
				if(index.equals("SUCCESS")){
					status=1;
				}else if(index.equals("FAIL")){
					status=2;
				}			
			}catch(Exception e){
				status=0;
				e.printStackTrace();
			}
			return status;
		}
	 }
	 private Post newPost(){
		 Post newPost =  new Post();
		 String postText = post_comment.getText().toString();
		 GPSTracker gpsTracker = new GPSTracker(getActivity());
		 newPost.setAuthorUsername(musicItem.getAuthorUsername());
		 newPost.setMusicName(musicItem.getName());
		 newPost.setComment(postText);
		 newPost.setLatitude(gpsTracker.getlatitude());
		 newPost.setLongitude(gpsTracker.getlongitude());
		 newPost.setMusicUrl(musicItem.getName());
		 newPost.setUserPhotoUrl(LoginActivity.currentUser.getPhotoFilename());
		 return(newPost);
		 
	 }
}
