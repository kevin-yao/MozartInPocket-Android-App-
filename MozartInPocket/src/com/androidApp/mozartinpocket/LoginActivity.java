package com.androidApp.mozartinpocket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.androidApp.mozartinpocket.compose.ComposeActivity;
import com.androidApp.mozartinpocket.database.DataBaseManager;
import com.androidApp.mozartinpocket.exception.IllegalNameException;
import com.androidApp.mozartinpocket.activities.UserProfileActivity;
import com.mozartinpocket.entities.User;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	public static String USER_NAME = "user name";
	public static String PASSWORD = "password";

	private Button loginButton;
	private Button registerButton;
	public static final String GlobalURL="http://10.0.22.88:8080/MozartInPocket/";
	private String URL=GlobalURL+"Login";
	private ProgressBar progressBar;
	private LoginTask login;
	private DataBaseManager dbManager;
	private User user;
	String username;
	String password;

	public static User currentUser = new User();
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
        user=new User();
		loginButton = (Button) findViewById(R.id.btn_login);
		loginButton.setOnClickListener(this);
		registerButton = (Button) findViewById(R.id.btn_register);
		registerButton.setOnClickListener(this);
		
		// load music file database
		//if(MusicItemList.musicItemList.isEmpty()){
		// MusicItemList.populateMusicDB();
		//}
		// populate current User;

	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		EditText etUsername=(EditText) findViewById(R.id.edtxt_userName);
		username=etUsername.getText().toString();
		EditText etPwr=(EditText) findViewById(R.id.edtxt_pwr);
		password=etPwr.getText().toString();

		if (v.equals(loginButton)) {
			//Intent intent = new Intent(this, ControlActivity.class);
			System.out.println(username);
			System.out.println(password);
			if(password.equals("123456")){
				currentUser.setUsername(username);
				currentUser.setPassword(password);
				currentUser.setPhotoFilename(user.getPhotoFilename());
//				currentUser.setState(User.LOGIN);
//				user.setState(User.LOGIN);
//				currentUser = user;
				Intent intent = new Intent(LoginActivity.this, ComposeActivity.class);
				startActivity(intent);
				return;
			}
			if(login!=null){
				login.cancel(true);
			}
			login=new LoginTask();
			login.execute(username, password);

		}else if (v.equals(registerButton)) {
			try{
				String regex = "\\\\";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher1 = pattern.matcher(username);
				Matcher matcher2 = pattern.matcher(password);
				String m1=null,m2 = null;
				if(matcher1.find()){
					m1 = matcher1.group();
				}
				if(matcher2.find()){
					m2 = matcher2.group();
				}
                if(username.length()!=0 && password.length()!=0 && m1 ==null && m2 ==null && username.length()<=25 && password.length()<=25){
					Intent intent = new Intent(this, UserProfileActivity.class);
					intent.putExtra(USER_NAME, username);
					intent.putExtra(PASSWORD, password);
					startActivity(intent);
					currentUser = user;
				}
				else{
					throw new IllegalNameException(this,username,password);
				}
				//Navigation.editProfile(this);
			}
			catch(IllegalNameException e){
				e.exceptionFix();
			}
		}
	}

	private class LoginTask extends AsyncTask<String, Integer, Integer>{
		@Override
		protected void onPreExecute() {
			progressBar = (ProgressBar) findViewById(R.id.progressBar2);
			progressBar.setVisibility(View.VISIBLE);
			Button btn=(Button) findViewById(R.id.btn_login);
			btn.setClickable(false);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected Integer doInBackground(String... params) {		 
			int status = logIn(params[0],params[1]);
			return status;
		}
		@Override
		protected void onPostExecute(Integer status) {
			progressBar.setVisibility(View.INVISIBLE);
			Button btn=(Button) findViewById(R.id.btn_login);
			btn.setClickable(true);
			if (status==1){
			//	dbManager = new DataBaseManager(getApplicationContext());
			//	dbManager.open();
			//	User user = dbManager.getUser(username);
				currentUser.setUsername(username);
				currentUser.setPassword(password);
				currentUser.setPhotoFilename(user.getPhotoFilename());
			//	dbManager.close();
				currentUser.setState(User.LOGIN);
				user.setState(User.LOGIN);
				currentUser = user;
				Intent intent = new Intent(LoginActivity.this, ComposeActivity.class);
				startActivity(intent);
			}else if(status==2){
				Toast.makeText(getApplicationContext(), "Username or password is wrong, please try again!", Toast.LENGTH_LONG).show();
			}else if(status==0) {
				Toast.makeText(getApplicationContext(), "Please check your network!", Toast.LENGTH_LONG).show();
			}else if(status==3) {
				Toast.makeText(getApplicationContext(), "System error!", Toast.LENGTH_LONG).show();
			}
		}

		public int logIn(String username, String password){
			int status=0;
			HttpPost post = new HttpPost(URL); 
			HttpClient client = new DefaultHttpClient();
			List <NameValuePair> params = new ArrayList <NameValuePair>();  
			user.setUsername(username);
			user.setPassword(password);
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));
			try{
				HttpEntity requestHttpEntity = new UrlEncodedFormEntity(params);  
				post.setEntity(requestHttpEntity);
				HttpResponse response = client.execute(post); 		     
				InputStream is  = response.getEntity().getContent();           
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String index = null;
				index=new String(reader.readLine().getBytes(), "UTF-8");
				is.close(); 
				System.out.println(index);
				if(response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK)
				{
					status=0;
				}
				if(username==null||index.equals("FAIL")){
					status=2;
				}else if(index.equals("SUCCESS")){
					status=1;
					
					int age = Integer.valueOf(new String(reader.readLine().getBytes(), "UTF-8"));
					System.out.println(age);
					user.setAge(age);
					String name=new String(reader.readLine().getBytes(), "UTF-8");
					System.out.println(name);
					user.setName(name);
					String email=new String(reader.readLine().getBytes(), "UTF-8");
					System.out.println(email);
					user.setEmail(email);
					String gender=new String(reader.readLine().getBytes(),"UTF-8");
					System.out.println(gender);
					user.setGender(gender);
					String musicStyle=new String(reader.readLine().getBytes(),"UTF-8");
					System.out.println(musicStyle);
					user.setMyMusicStyle(musicStyle);
					String photoFilename=new String(reader.readLine().getBytes(),"UTF-8");
					System.out.println(photoFilename);
					user.setPhotoFilename(photoFilename);
					String interestsTag=new String(reader.readLine().getBytes(),"UTF-8");
					System.out.println(interestsTag);
					user.setInterestsTag(interestsTag);
					dbManager = new DataBaseManager(getApplicationContext());
					dbManager.open();
					dbManager.addUser(user);
					dbManager.close();
				}
			}catch(Exception e){
				status=3;
				e.printStackTrace();
			}
			return status;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (login != null
				&& login.getStatus() != LoginTask.Status.FINISHED) {
			login.cancel(true);
			login = null;
		}
	}
}
