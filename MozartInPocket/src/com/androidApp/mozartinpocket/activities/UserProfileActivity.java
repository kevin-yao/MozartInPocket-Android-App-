package com.androidApp.mozartinpocket.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import com.androidApp.mozartinpocket.LoginActivity;
import com.androidApp.mozartinpocket.R;
import com.androidApp.mozartinpocket.database.DataBaseManager;
import com.androidApp.mozartinpocket.ChangePhotoDialog;
import com.androidApp.mozartinpocket.util.ImageLoader;
import com.androidApp.mozartinpocket.util.Navigation;
import com.androidApp.mozartinpocket.util.ScreenShotUtils;
import com.mozartinpocket.entities.User;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.widget.RelativeLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class UserProfileActivity extends FragmentActivity implements OnClickListener{
	private ArrayList<Boolean> btnState = new ArrayList<Boolean>();
	public static int buttonNum = 12;
	public static final int PADDING = 8;
	public static final int NUM_OF_COLUMNS = 3;
	private String URL=LoginActivity.GlobalURL+"SaveUserProfile";
	private ProgressBar progressBar;
	private UploadUserTask uploadUserTask = null;
	private static String nullFlag="*$#";
	ImageView photo;
	int imageWidth;
	EditText real_name;
	EditText age;
	EditText email;
	TextView interest_tag;
	ArrayList<Button> btns; 
	ArrayList<Boolean> music_styles_arr;
	StringBuffer music_styles = new StringBuffer("");
	String gender = new String();
	String username = new String("");
	String password = new String("");
	String photoName = null;
	boolean local_image = false;
	String updateFlag = "false";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);

		if ( savedInstanceState == null ) // the app just started running
		{
			for (int i = 0; i < buttonNum; i ++) {
				btnState.add(false);
			}
		} // end if

		InitilizeImageLayout();
		
		Intent intent = getIntent();
		username = intent.getStringExtra(LoginActivity.USER_NAME);
		password = intent.getStringExtra(LoginActivity.PASSWORD);
		
		TextView user_name = (TextView) findViewById(R.id.txtView_user_name);
		user_name.setText(username);
		
		photo = (ImageView) findViewById(R.id.portrait);
		ImageLoader imageLoader = new ImageLoader(getBaseContext());
		imageLoader.DisplayImage("http://photos.tuchong.com/351862/f/6212531.jpg", photo);
		photo.setLayoutParams(new RelativeLayout.LayoutParams(imageWidth, imageWidth));
		photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogFragment dialog = new ChangePhotoDialog();
				dialog.show(getSupportFragmentManager(), "ChangePhotoDialogFragment");
			}});
        
		real_name = (EditText) findViewById(R.id.edtxt_editName);
		age = (EditText) findViewById(R.id.edtxt_editAge);
		email = (EditText) findViewById(R.id.edtxt_email);
		interest_tag = (TextView) findViewById(R.id.txtView_show_interest);
		
		btns = new ArrayList<Button>();
		
		music_styles_arr = new ArrayList<Boolean>(12);
		for(int i = 0; i < 12; i ++)
			music_styles_arr.add(false);
		
		Button add = (Button) findViewById(R.id.btn_add_interest);
		add.setOnClickListener(this);

		Button style1btn = (Button) findViewById(R.id.btn_style1);
		style1btn.setOnClickListener(this);
		btns.add(style1btn);

		Button style2btn = (Button) findViewById(R.id.btn_style2);
		style2btn.setOnClickListener(this);
		btns.add(style2btn);

		Button style3btn = (Button) findViewById(R.id.btn_style3);
		style3btn.setOnClickListener(this);
		btns.add(style3btn);

		Button style4btn = (Button) findViewById(R.id.btn_style4);
		style4btn.setOnClickListener(this);
		btns.add(style4btn);

		Button style5btn = (Button) findViewById(R.id.btn_style5);
		style5btn.setOnClickListener(this);
		btns.add(style5btn);

		Button style6btn = (Button) findViewById(R.id.btn_style6);
		style6btn.setOnClickListener(this);
		btns.add(style6btn);

		Button style7btn = (Button) findViewById(R.id.btn_style7);
		style7btn.setOnClickListener(this);
		btns.add(style7btn);

		Button style8btn = (Button) findViewById(R.id.btn_style8);
		style8btn.setOnClickListener(this);
		btns.add(style8btn);

		Button style9btn = (Button) findViewById(R.id.btn_style9);
		style9btn.setOnClickListener(this);
		btns.add(style9btn);

		Button style10btn = (Button) findViewById(R.id.btn_style10);
		style10btn.setOnClickListener(this);
		btns.add(style10btn);

		Button style11btn = (Button) findViewById(R.id.btn_style11);
		style11btn.setOnClickListener(this);
		btns.add(style11btn);

		Button style12btn = (Button) findViewById(R.id.btn_style12);
		style12btn.setOnClickListener(this);
		btns.add(style12btn);

		Button registerBtn = (Button) findViewById(R.id.btn_register_profile);
		registerBtn.setOnClickListener(this);

		Button updateBtn = (Button) findViewById(R.id.btn_update);
		updateBtn.setOnClickListener(this);
		
		final Spinner editGenderSpinner = (Spinner) findViewById(R.id.spin_editGender);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		editGenderSpinner.setAdapter(adapter);

		editGenderSpinner.setOnItemSelectedListener (new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				gender = editGenderSpinner.getSelectedItem().toString();
			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		
        if(LoginActivity.currentUser.getState() == User.LOGIN) {
        	updateFlag = "true";
        	updateBtn.setClickable(true);
        	registerBtn.setClickable(false);
        	if(LoginActivity.currentUser.getName() != null)
        		real_name.setText(LoginActivity.currentUser.getName());
        	age.setText(LoginActivity.currentUser.getAge() + "");
        	if(LoginActivity.currentUser.getGender() != null) {
        		int pos = 0;
        		if (LoginActivity.currentUser.getGender().equals("Boy"))
        			pos = 1;
        		editGenderSpinner.setSelection(pos);
        	}
        	
        	if(LoginActivity.currentUser.getEmail() != null)
        		email.setText(LoginActivity.currentUser.getEmail());
        	if(LoginActivity.currentUser.getInterestsTag() != null)
        		email.setText(LoginActivity.currentUser.getInterestsTag());
        	if(LoginActivity.currentUser.getMyMusicStyle() != null) {
        		String styles [] = LoginActivity.currentUser.getMyMusicStyle().split(" ");
        		int index = 0;
        		for(int i = 0; i < styles.length;i ++) {
        			if(styles[i].equals("Classical"))
        				index = 0;
        			else if(styles[i].equals("Jazz"))
        				index = 1;
        			else if(styles[i].equals("Rock"))
        				index = 2;
        			else if(styles[i].equals("Baroque"))
        				index = 3;
        			else if(styles[i].equals("Electronic"))
        				index = 4;
        			else if(styles[i].equals("Pop"))
        				index = 5;
        			else if(styles[i].equals("Instrumental"))
        				index = 6;
        			else if(styles[i].equals("Indie"))
        				index = 7;
        			else if(styles[i].equals("Folk"))
        				index = 8;
        			else if(styles[i].equals("Funk"))
        				index = 9;
        			else if(styles[i].equals("Metal"))
        				index = 10;
        			else if(styles[i].equals("Punk"))
        				index = 11;
            		btns.get(index).setSelected(true);
    				btnState.set(index, true);
    				music_styles_arr.set(index, true);
        		}
        	}
        	
        }else if(LoginActivity.currentUser.getState() == User.REGISTER) {
        	updateBtn.setClickable(false);
        	registerBtn.setClickable(true);
        }
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_profile, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_add_interest) {
			TextView showInterest = (TextView) findViewById(R.id.txtView_show_interest);
			EditText interestText = (EditText) findViewById(R.id.edtxt_editInterest);
			showInterest.append(" " + interestText.getText().toString());
			interestText.setText("");
		}
		int index = 0;
		switch (v.getId()) {
		case R.id.btn_style1: 
			index = 0; 
			break;
		case R.id.btn_style2: 
			index = 1;
			break;
		case R.id.btn_style3:
			index = 2;
			break;
		case R.id.btn_style4: 
			index = 3;
			break;
		case R.id.btn_style5:
			index = 4;
			break;
		case R.id.btn_style6: 
			index = 5;
			break;
		case R.id.btn_style7: 
			index = 6; 
			break;
		case R.id.btn_style8: 
			index = 6; 
			break;
		case R.id.btn_style9: 
			index = 6; 
			break;
		case R.id.btn_style10: 
			index = 9;
			break;
		case R.id.btn_style11:
			index = 10;
			break;
		case R.id.btn_style12: 
			index = 11;
			break;
		}
		if (! btnState.get(index)) {
			v.setSelected(true);
			btnState.set(index, true);
			music_styles_arr.set(index, true);
			
		}else { 
			v.setSelected(false);
			v.setEnabled(true);
			btnState.set(index, false);
			music_styles_arr.set(index, false);
		}
		if(v.getId()==R.id.btn_register_profile || v.getId()==R.id.btn_update){
			for(int i = 0; i < 12; i ++) {
				if(music_styles_arr.get(i))
					music_styles.append(btns.get(i).getText().toString() + " ");
			}
			
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			user.setName(real_name.getText().toString());
			if(age.getText().toString().length() == 0) {
				user.setAge(0);
			}else {
				user.setAge(Integer.parseInt(age.getText().toString()));
			}
			user.setEmail(email.getText().toString());
			user.setGender(gender);
			user.setInterestsTag(interest_tag.getText().toString());
			user.setMyMusicStyle(music_styles.toString());
			if(photoName != null) {
				String name = photoName;
				if(photoName.contains("/")) {
					String str [] = photoName.split("/");
					String s = str [str.length - 1];
					name = s.substring(0, s.length() - 4);
				}			
				user.setPhotoFilename(name);
			}else {
				user.setPhotoFilename("empty");
				photoName = "empty";
			}
			try{
			DataBaseManager db= new DataBaseManager(this);
			db.open();
			db.addUser(user);
			db.close();
			//User user2=db.getUser("Joy");
			//Log.i("user", user2.getInterestsTag());
			}catch(Exception e){
				System.err.println(e.toString());
			}			
			if(uploadUserTask!=null){
				uploadUserTask.cancel(true);
			}
			uploadUserTask= new UploadUserTask();
			uploadUserTask.execute(user);
		}
	}
    
	@Override
    public void onStop() {
        super.onStop();
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//ContentResolver resolver = getContentResolver();
		if(requestCode == ChangePhotoDialog.LOCAL_IMAGE && resultCode == Activity.RESULT_OK) 
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				final Intent intent = data;
				new Thread(new Runnable() {
					@Override
					public void run() {
						Uri imageUri = intent.getData();
						photo.setImageURI(imageUri);
						photoName = getRealPathFromURI(imageUri);
						local_image = true;
					}
				}).start();
			}
		if(requestCode == ChangePhotoDialog.REQUEST_IMAGE && resultCode == Activity.RESULT_OK) 
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						Uri imageUri = Uri.parse(ChangePhotoDialog.fileName);
						photo.setImageURI(imageUri);
						photoName = ChangePhotoDialog.photoName;
					}
				}).start();
			}
	}

	public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
		case R.id.action_compose:
			Navigation.compose(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void InitilizeImageLayout() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				PADDING, r.getDisplayMetrics());

		int screenWidth = getResources().getDisplayMetrics().widthPixels;

		imageWidth = (int) ((screenWidth - ((NUM_OF_COLUMNS + 1) * padding)) / NUM_OF_COLUMNS);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (uploadUserTask != null
				&& uploadUserTask.getStatus() != UploadUserTask.Status.FINISHED) {
			uploadUserTask.cancel(true);
			uploadUserTask = null;
		}
	}
	private class UploadUserTask extends AsyncTask<Object, Integer, Integer>{ 
		@Override
		protected void onPreExecute() {
			progressBar = (ProgressBar) findViewById(R.id.progressBar1);
			progressBar.setVisibility(View.VISIBLE);
			if(updateFlag.equals("true")) {
				Button btn = (Button) findViewById(R.id.btn_update);
				btn.setClickable(false);
			}else {
				Button btn=(Button) findViewById(R.id.btn_register_profile);
				btn.setClickable(false);
			}
			
			super.onPreExecute();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected Integer doInBackground(Object... params) {		 
			int status=uploadUser((User) params[0]);
			return status;
		}
		@Override
		protected void onPostExecute(Integer status) {
		 progressBar.setVisibility(View.INVISIBLE);
		 if(updateFlag.equals("true")) {
				Button btn = (Button) findViewById(R.id.btn_update);
				btn.setClickable(true);
		 }else {
				Button btn=(Button) findViewById(R.id.btn_register_profile);
				btn.setClickable(true);
		 }
			if(status==1){
				Toast.makeText(getApplicationContext(), "Upload success!", Toast.LENGTH_LONG).show();
			}else if(status==2){
				Toast.makeText(getApplicationContext(), "Upload fail!", Toast.LENGTH_LONG).show();
			}else if(status==0){
				Toast.makeText(getApplicationContext(), "Please check your network!", Toast.LENGTH_LONG).show();
			}else if(status==3){
				Toast.makeText(getApplicationContext(), "This username has existed, please change another one!", Toast.LENGTH_LONG).show();
			}else if(status==4){
				Toast.makeText(getApplicationContext(), "This image doesn't exists!", Toast.LENGTH_LONG).show();
			}else if(status==5){
				Toast.makeText(getApplicationContext(), "System error!", Toast.LENGTH_LONG).show();
			}	 	      
		}
		public int uploadUser(User user){
			int status = 0 ;
			try{
				HttpPost post = new HttpPost(URL);  
				HttpClient client = new DefaultHttpClient();
				File photo = null;
				if(! photoName.equals("empty")) {
					if (local_image) {
						photo = new File(photoName);
					}else {
						photo = new File(Environment.getExternalStorageDirectory()
							+ "/photo/"+user.getPhotoFilename()+".jpg");
					}
					
					if (!photo.exists()) {
						photo.mkdirs();
					}
				}
				Log.i("name",user.getPhotoFilename());
				MultipartEntity mulentity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				mulentity.addPart("updateFlag", new StringBody(updateFlag, Charset.forName("UTF-8")));
				mulentity.addPart("username", new StringBody(user.getUsername(), Charset.forName("UTF-8")));
				mulentity.addPart("password", new StringBody(user.getPassword(),Charset.forName(HTTP.UTF_8)));
				mulentity.addPart("name", new StringBody((user.getName()==null)?nullFlag:user.getName(), Charset.forName(HTTP.UTF_8)));
				mulentity.addPart("gender", new StringBody((user.getGender()==null)? nullFlag:user.getGender(),Charset.forName(HTTP.UTF_8)));
				mulentity.addPart("email", new StringBody((user.getEmail()==null)?nullFlag:user.getEmail(),Charset.forName(HTTP.UTF_8)));
				mulentity.addPart("age", new StringBody(String.valueOf(user.getAge()),Charset.forName(HTTP.UTF_8)));
				mulentity.addPart("interestsTag", new StringBody((user.getInterestsTag()==null)?nullFlag:user.getInterestsTag(),Charset.forName(HTTP.UTF_8)));
				mulentity.addPart("myMusicStyle", new StringBody((user.getMyMusicStyle()==null)?nullFlag:user.getMyMusicStyle(),Charset.forName(HTTP.UTF_8)));
				mulentity.addPart("photoFilename", new StringBody((user.getPhotoFilename()==null)?nullFlag:user.getPhotoFilename(),Charset.forName(HTTP.UTF_8)));
				
				if(user.getPhotoFilename().equals("empty")){
					mulentity.addPart("empty", new StringBody("true",Charset.forName(HTTP.UTF_8)));	
				}else{
					mulentity.addPart("empty", new StringBody("false",Charset.forName(HTTP.UTF_8)));
				    FileBody photoBody = new FileBody(photo); 
				    mulentity.addPart("photo", photoBody);
				}
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
				}else if(index.equals("USER EXISTS")){
					status=3;
				}
						
			}catch(FileNotFoundException e){
				status=4;
				e.printStackTrace();
			}
			catch(Exception e){
				status=5;
				e.printStackTrace();
			}
			return status;
		}
	}

}
