package com.androidApp.mozartinpocket.cloud;

import java.io.InputStream;

import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.androidApp.mozartinpocket.LoginActivity;
import com.androidApp.mozartinpocket.R;
import com.androidApp.mozartinpocket.cloud.RefreshableView.PullToRefreshListener;
import com.androidApp.mozartinpocket.util.ImageLoader;
import com.androidApp.mozartinpocket.util.Navigation;
import com.androidApp.mozartinpocket.util.ScreenShotUtils;
import com.androidApp.mozartinpocket.util.TabsControl;
import com.mozartinpocket.entities.Post;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

public class MusicInCloudActivity extends Activity {
	public static int curId = 0;
    
	private int postId = 0;
	private LinkedList<String> posts;
	private LinkedList<String> usernames;
	private ArrayList<Post> postList;
	private LinkedList<String> dates;
	private LinkedList<String> location;
	private LinkedList<String> imageURL;
	private LinkedList<String> musicSrc;
	RefreshableView refreshableView;
	ListView listview;
	public static ImageLoader imageLoader;
	private String URL=LoginActivity.GlobalURL+"ResponsePost";
	private DownloadPostListTask downloadPostListTask=null;
	private CustomAdapter myAdapter;
	private static final String USER_NAME_ARRAYLIST = "user_name_arraylist";
	private static final String POSTS_ARRAYLIST = "posts_arraylist";
	private static final String DATES_ARRAYLIST = "dates_arraylist";
	private static final String LOCATION_ARRAYLIST = "location_arraylist";
	private static final String IMAGE_URL_ARRAYLIST = "image_url_arraylist";
	private static final String MUSIC_SRC_ARRAYLIST = "music_src_arraylist";
	private static final String POST_ID = "post_id";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_in_cloud);
		TabsControl.controlTabs(this);
		RadioButton btn = (RadioButton) findViewById(R.id.navigation_bar).findViewById(R.id.main_tab_music_cloud);
        btn.setChecked(true);
        
        
        if(DataStorage.savedPara == null){
        	usernames = new LinkedList<String> ();
            usernames.add(LoginActivity.currentUser.getUsername());
            
    		posts = new LinkedList<String> ();
    		posts.add("I love music! \nI enjoy Mozart in Pocket!");
    		
    		dates = new LinkedList<String> ();
    		DateFormat format = DateFormat.getDateTimeInstance();
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
    		dates.add(format.format(new Date(0)));
    		
    		location = new LinkedList<String> ();
    		location.add("none");
    		
    		imageURL = new LinkedList<String> ();
    		imageURL.add("default_photo");
    		
    		musicSrc = new LinkedList<String> ();
    		musicSrc.add("default_music");
        }
        
		if(DataStorage.savedPara != null){
        	Log.w("onCreat","not null");
        	recoverFromBundle(DataStorage.savedPara);
        }
		//ImageLoader imageLoader=new ImageLoader(getApplicationContext());
		
		refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
		final CustomAdapter adapter = new CustomAdapter(this, R.layout.custom_adapter_item, usernames, posts, dates, location, imageURL, musicSrc);
		myAdapter = adapter;
        listview = (ListView) findViewById(R.id.list_view);
        listview.setAdapter(adapter);
        
        refreshableView.setOnRefreshListener(new PullToRefreshListener() { 
        	@Override
            public void onRefresh() {  
                new AsyncTask<Void, Void, Void>() {  
                    protected Void doInBackground(Void... params) {  
                        try {  
                            Thread.sleep(1000);  
                        } catch (Exception e) {  
                            e.printStackTrace();  
                        }  
                        
            			if(downloadPostListTask!=null){
            				downloadPostListTask.cancel(true);
            			}
            			downloadPostListTask = new DownloadPostListTask();
            			downloadPostListTask.execute(postId);         			
                        return null;  
                    }  
  
                    @Override  
                    protected void onPostExecute(Void result) { 
                //    	Log.v("postid", postId + "");
                    		adapter.notifyDataSetChanged(); 
                    		refreshableView.finishRefreshing();
                    }             
                }.execute();
            }  
        }, 0);  
	}
	@Override
	public void onResume(){
		super.onResume();
		if(DataStorage.savedPara!=null){
			Log.w("onResume","savedPara not nill");
			recoverFromBundle(DataStorage.savedPara);
			Log.w("onResume","savedPara not nill");
		}
		final CustomAdapter adapter = new CustomAdapter(this, R.layout.custom_adapter_item, usernames, posts, dates, location, imageURL, musicSrc);
		myAdapter = adapter;
        listview = (ListView) findViewById(R.id.list_view);
        listview.setAdapter(adapter);
		Log.w("onResume","onResume called");
	}
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState){
		super.onSaveInstanceState(savedInstanceState);
		ArrayList<String> postsArray = transferLinkedToArray(posts);
		ArrayList<String> usernamesArray = transferLinkedToArray(usernames);
		ArrayList<String> datesArray = transferLinkedToArray(dates);
		ArrayList<String> locationArray = transferLinkedToArray(location);
		ArrayList<String> imageURLArray = transferLinkedToArray(imageURL);
		ArrayList<String> musicSrcArray = transferLinkedToArray(musicSrc);
		savedInstanceState.putStringArrayList(USER_NAME_ARRAYLIST, usernamesArray);
		savedInstanceState.putStringArrayList(POSTS_ARRAYLIST, postsArray);
		savedInstanceState.putStringArrayList(DATES_ARRAYLIST, datesArray);
		savedInstanceState.putStringArrayList(LOCATION_ARRAYLIST, locationArray);
		savedInstanceState.putStringArrayList(IMAGE_URL_ARRAYLIST, imageURLArray);
		savedInstanceState.putStringArrayList(MUSIC_SRC_ARRAYLIST, musicSrcArray);
		//savedInstanceState.putInt(POST_ID, postId);
		Log.w("Really onSavedIntanceState","onSavedINstanceState Called haha");
		DataStorage.savedPara = new Bundle();
		DataStorage.savedPara.putStringArrayList(USER_NAME_ARRAYLIST, usernamesArray);
		DataStorage.savedPara.putStringArrayList(POSTS_ARRAYLIST, postsArray);
		DataStorage.savedPara.putStringArrayList(DATES_ARRAYLIST, datesArray);
		DataStorage.savedPara.putStringArrayList(LOCATION_ARRAYLIST, locationArray);
		DataStorage.savedPara.putStringArrayList(IMAGE_URL_ARRAYLIST, imageURLArray);
		DataStorage.savedPara.putStringArrayList(MUSIC_SRC_ARRAYLIST, musicSrcArray);
		DataStorage.savedPara.putInt(POST_ID, postId);
		Log.w("onSavedInstanceState", (DataStorage.savedPara.getStringArrayList(USER_NAME_ARRAYLIST)).get(0));
	}
	@Override
	public void onPause(){
		super.onPause();
		if(myAdapter!=null){
			myAdapter.stopMediaPlayer();
		}
	}
	
	@Override
    public void onStop() {
        super.onStop();
        if(myAdapter!=null){
        	myAdapter.stopMediaPlayer();
        }
    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.control, menu);
		return true;
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
	
	 private class DownloadPostListTask extends AsyncTask<Integer, Void, ArrayList<Post>>{ 
		 @Override
		 protected ArrayList<Post> doInBackground(Integer... params) {		 
			 ArrayList<Post> postList=downloadPostList( params[0]);
			 return postList;
		 }
		 @Override
		    protected void onPostExecute(ArrayList<Post> postList) {
			   if(postList != null && postList.size() != 0){
				   Post post=new Post();
				   for (int i = postList.size() - 1; i >= 0; i --) {
                		post = postList.get(i);
                		usernames.addFirst(post.getAuthorUsername());
               		    posts.addFirst(post.getComment());
               		    dates.addFirst(post.getDate());
               		    location.addFirst(post.getLatitude() + "a" + post.getLongitude());
               	        imageURL.addFirst(post.getUserPhotoUrl());
               		    musicSrc.addFirst(post.getMusicUrl());
                	}
            		    postId = post.getPostId();
            		    Log.i("post id",String.valueOf(postId) );
			   }
            	else if(postList == null) {
            		Toast.makeText(getApplicationContext(),"No available updates",Toast.LENGTH_LONG).show();
			   }	      
		    }
		
		@SuppressWarnings("unchecked")
		public ArrayList<Post> downloadPostList(int currentPostId){
			postList = new ArrayList<Post>();
			try{
			HttpPost post = new HttpPost(URL);  
			HttpClient client = new DefaultHttpClient();
			List <NameValuePair> params = new ArrayList <NameValuePair>(); 
			params.add(new BasicNameValuePair("currentPostId", String.valueOf(currentPostId)));
			HttpEntity requestHttpEntity = new UrlEncodedFormEntity(params);  
			post.setEntity(requestHttpEntity);
			HttpResponse response = client.execute(post); 		     
			InputStream is  = response.getEntity().getContent(); 
			ObjectInputStream ois = new ObjectInputStream(is);
	        postList = (ArrayList<Post>) ois.readObject();
	        ois.close();
	        ois = null;
			}catch(Exception e){
				e.printStackTrace();
			}			
			return postList;
		}
	 }

	private ArrayList<String> transferLinkedToArray(LinkedList<String> linked){
		ArrayList<String> array = new ArrayList<String>();
		Iterator<String> it  = linked.iterator();
		int index = 0;
		String regexChar = "\\\\";
		while(it.hasNext()){
			String temp = index + regexChar + it.next();
			array.add(temp);
		}
		return(array);
	}
	private void recoverFromBundle(Bundle savedInstanceState){
		if(savedInstanceState !=null){
			if(savedInstanceState.containsKey(USER_NAME_ARRAYLIST)){
				Log.w("recoverFromBundle","username arraylist");
				ArrayList<String> usernameArrayList = savedInstanceState.getStringArrayList(USER_NAME_ARRAYLIST);
				Log.w("usernameArraylist",usernameArrayList.get(0));
				usernames = transferArrayToLinked(usernameArrayList);
				ArrayList<String> postsArrayList = savedInstanceState.getStringArrayList(POSTS_ARRAYLIST);
				posts = transferArrayToLinked(postsArrayList); 
				ArrayList<String> datesArrayList = savedInstanceState.getStringArrayList(DATES_ARRAYLIST);
				dates = transferArrayToLinked(datesArrayList);
				ArrayList<String> locationArrayList = savedInstanceState.getStringArrayList(LOCATION_ARRAYLIST);
				location = transferArrayToLinked(locationArrayList);
				ArrayList<String> imageUrlArrayList = savedInstanceState.getStringArrayList(IMAGE_URL_ARRAYLIST);
				imageURL = transferArrayToLinked(imageUrlArrayList);
				ArrayList<String> musicSrcArrayList = savedInstanceState.getStringArrayList(MUSIC_SRC_ARRAYLIST);
				musicSrc = transferArrayToLinked(musicSrcArrayList);
				postId = savedInstanceState.getInt(POST_ID);
				Log.w("postId",Integer.toString(postId));
				Log.w("usernames",usernames.getFirst());
				
			}
		}
	}
	private LinkedList<String> transferArrayToLinked(ArrayList<String> array){
		LinkedList<String> linked = new LinkedList<String>();
		//HashMap<Integer, String> map = new HashMap<Integer, String>();
		String regex = "\\\\";
		//int length = array.size();
		//Iterator<String> it = array.iterator();
		for(int i = array.size()-1;i>=0;i--){
			String temp = array.get(i);
			
			int index = Integer.parseInt(temp.split(regex)[0]);
			Log.w("index",Integer.toString(index));
			String value = temp.split(regex,2)[1].substring(1);
			Log.w("arrayline",value);
			linked.add(index,value);
			Log.w("arrayToLink",value);
		}
		return(linked);
	}
	
}
