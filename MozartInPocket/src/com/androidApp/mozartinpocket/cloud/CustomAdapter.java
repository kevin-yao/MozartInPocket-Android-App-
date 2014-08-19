package com.androidApp.mozartinpocket.cloud;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.androidApp.mozartinpocket.R;
import com.androidApp.mozartinpocket.util.ImageLoader;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
	public static final String DIR = "/DCIM/Audio";
	private String path = Environment.getExternalStorageDirectory().getAbsolutePath()+DIR;
	private final Activity context;
	private final List<String> usernames;
	private final List<String> posts;
	private final List<String> dates;
	private final List<String> location;
	private final List<String> images;
	LinkedList<String> musicSrc;
	private final int resource;
	public ImageLoader imageLoader; 
	//private LinkedList<MediaPlayer> mediaPlayer;
	private MediaPlayer mediaPlayer;
	private int beingPlayed = -1;
	private ArrayList<View> itemViews;
	
	public CustomAdapter(Activity context, int resource, List<String> usernames, List<String> posts, List<String> dates, List<String> location, List<String> images, LinkedList<String> musicSrc) {
		super();
		this.context = context;
		this.usernames = usernames;
		this.posts = posts;
		this.dates = dates;
		this.location = location;
		this.images = images;
		this.resource = resource;
		this.musicSrc = musicSrc;
	    imageLoader = new ImageLoader(context.getApplicationContext());	
	    //mediaPlayer = new LinkedList<MediaPlayer>();
	    mediaPlayer = new MediaPlayer();
	    itemViews = new ArrayList<View> ();
	}
	
	static class ViewHolder {
	    public ImageView image;
	    public TextView username;
	    public TextView post;
	    public TextView time_location;
	    public ImageView arrow;
	    public int beingPlayed;
	    public boolean beingPause;
	  }
	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View itemView = convertView;
	    if (itemView == null) {
	      LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());
	      itemView = inflater.inflate(resource, null);
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.image = (ImageView) itemView.findViewById(R.id.list_image);
	      viewHolder.username = (TextView) itemView.findViewById(R.id.txtView_username);
	      viewHolder.post = (TextView) itemView.findViewById(R.id.txtView_post);
	      viewHolder.time_location = (TextView) itemView.findViewById(R.id.txtView_time_location);
	      viewHolder.arrow = (ImageView) itemView.findViewById(R.id.arrow);
	      viewHolder.arrow.setSelected(false);
	      viewHolder.beingPause = false;
	      itemView.setTag(viewHolder);
	      itemViews.add(itemView);
	      notifyDataSetChanged();
	    }
	    final ViewHolder holder = (ViewHolder) itemView.getTag();
	    String str[] = location.get(position).split("a");
	    String url = images.get(position);
	    holder.username.setText(usernames.get(position));
	    holder.post.setText(posts.get(position));
	    if(! location.get(position).equals("none")) 
	    	holder.time_location.setText(dates.get(position) + "\n latitude:" + str[0] + " Longitude" + str[1]);
	    else
	    	holder.time_location.setText(dates.get(position));
	    if(url.equals("default_photo"))
	    	holder.image.setImageResource(R.drawable.default_photo);
	    else
	    	imageLoader.DisplayImage(url, holder.image);
	    
	    final String src = musicSrc.get(position);
	    final int pos = position;
	    holder.arrow.setClickable(true);
	    notifyDataSetChanged();
	    holder.arrow.setOnClickListener(new View.OnClickListener() { 
	    	private int playbackPosition=0;
		    boolean isFirstTime = true;
		    public boolean isPlaying = false;
		    public boolean isPause = false;
	          @Override
	          public void onClick(View v) {
	        	  if(mediaPlayer == null || !mediaPlayer.isPlaying() && !isPause) {
	        		    holder.arrow.setImageResource(R.drawable.arrow);
	        		    isFirstTime = true;
		        		isPlaying = false;
		      		    isPause = false;
	        	  }
	        	  
	        	  if(beingPlayed == -1) {
	        		  //holder.arrow.setImageResource(R.drawable.pause_icon);
        			  beingPlayed = pos;
        		  }else if(beingPlayed != pos) {
        			  //holder.arrow.setImageResource(R.drawable.arrow);
        			  mediaPlayer.reset();
        			  ViewHolder h = (ViewHolder) itemViews.get(beingPlayed).getTag();
        			  h.arrow.setImageResource(R.drawable.arrow);
        			  holder.arrow.setImageResource(R.drawable.pause_icon);
        			  notifyDataSetChanged();
        			  beingPlayed = pos;
        		  } 
//        		  else if(beingPlayed == pos)
//        			  holder.arrow.setImageResource(R.drawable.arrow);
	        	  
		          mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
		        	    public void onCompletion(MediaPlayer mp) {
		        	        mp.reset();
		        	        holder.arrow.setImageResource(R.drawable.arrow);
		        	        beingPlayed = -1;
		        	        isFirstTime = true;
			        		isPlaying = false;
			      		    isPause = false;
		        	    }
		        	});
	        	  
	        	  if (isFirstTime && beingPlayed == pos) {
	        		  try {
		  			    //holder.arrow.setImageResource(R.drawable.pause_icon);
	  					playLocalAudio_UsingURL(src);
	  					isFirstTime = false;
	  					isPlaying = true;
	  				  } catch (Exception e) {
	  					e.printStackTrace();
	  				  }
	        	  }else if (isPlaying && beingPlayed == pos) {
	        		  if(mediaPlayer!=null)
			        	{
			        		playbackPosition = mediaPlayer.getCurrentPosition();
			        		mediaPlayer.pause();
			        		holder.arrow.setImageResource(R.drawable.arrow);
			        		isPause = true;
			        		holder.beingPause = true;
			        		isPlaying = false;
			        	}
	        	  }else if (isPause && beingPlayed == pos) {
	        		   if(mediaPlayer!=null && !mediaPlayer.isPlaying())
			        	{
	        			    mediaPlayer.seekTo(playbackPosition);
	        			    mediaPlayer.start();
	        			    holder.arrow.setImageResource(R.drawable.pause_icon);
			        		isPlaying = true;
			        		isPause = false;
			        		holder.beingPause = false;
			        	}
	        	  }
	          }
	          
	          private void playLocalAudio_UsingURL(final String src) throws Exception {
	        	  new AsyncTask<Void, Void, Void>() {  
	        		    String filename;
	                    protected Void doInBackground(Void... params) {  
	                        try {  
	                            Thread.sleep(1000);  
	                        } catch (Exception e) {  
	                            e.printStackTrace();  
	                        }   
	                        holder.arrow.setClickable(false);
	                        if(src.equals("default_music"))
	                        	filename = path + "/test.mid";
	                        else {
	                        	InputStream is = null;
		                    	String strbuf [] = src.split("/");
		                    	filename = path + strbuf[strbuf.length - 1];
		                    	File f = new File(filename);
		                		try {
		                			is = new URL(src).openConnection().getInputStream();
		                			copy(is,f);
		                		} catch (MalformedURLException e) {
		                			holder.arrow.setClickable(false);
		                			e.printStackTrace();
		                			
		                		} catch (IOException e) {
		                			e.printStackTrace();
		                		}  
	                        }	      	        	 
						    return null;
	                    }  
	  
	                    @Override  
	                    protected void onPostExecute(Void result) {  
	                    	try {
	                    		mediaPlayer.setDataSource(filename);
	                    		mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
									
									@Override
									public void onPrepared(MediaPlayer mp) {
										holder.arrow.setImageResource(R.drawable.pause_icon);
										holder.arrow.setClickable(true);
										mp.start();
									}
								});
	                    		mediaPlayer.prepareAsync();
								
							} catch (IllegalStateException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
	                    }  
	                }.execute();
	      	  }	          
	        });  
	    
	    return itemView;
	  }

	private void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf)) > 0) {
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@Override
	public int getCount() {
		return posts.size(); 
	}

	@Override
	public Object getItem(int position) {
		return posts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void stopMediaPlayer(){
		mediaPlayer.stop();
	}
}
