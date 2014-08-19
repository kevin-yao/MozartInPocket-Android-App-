package com.androidApp.mozartinpocket.cloud;

import com.androidApp.mozartinpocket.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.RotateAnimation;

public class RefreshableView extends LinearLayout implements OnTouchListener {
	
    public static final int STATUS_PULL_TO_REFRESH = 0;  
    public static final int STATUS_RELEASE_TO_REFRESH = 1;  
    public static final int STATUS_REFRESHING = 2;  
    public static final int STATUS_REFRESH_FINISHED = 3;
    
    /** 
     * Header's speed of scrolling back
     */  
    public static final int SCROLL_SPEED = -20;  
  
    /** 
     * How many milliseconds in one minute, used to decide the last updated time
     */  
    public static final long ONE_MINUTE = 60 * 1000;  
  
    /** 
     * How many milliseconds in one hour, used to decide the last updated time
     */  
    public static final long ONE_HOUR = 60 * ONE_MINUTE;  
  
    /** 
     * How many milliseconds in one day, used to decide the last updated time
     */  
    public static final long ONE_DAY = 24 * ONE_HOUR;  
  
    /** 
     * How many milliseconds in one month, used to decide the last updated time 
     */  
    public static final long ONE_MONTH = 30 * ONE_DAY;  
  
    /** 
     * How many milliseconds in one year, used to decide the last updated time 
     */  
    public static final long ONE_YEAR = 12 * ONE_MONTH;  
  
    /** 
     * Last updated time
     */  
    private static final String UPDATED_AT = "updated_at";  
  
    
    private PullToRefreshListener mListener;  
  
    /** 
     * Used to store last updated time 
     */  
    private SharedPreferences preferences;  
  
    private View headView; 
    private ListView listView;  
    private ProgressBar progressBar;  
    private ImageView arrow;  
    private TextView description;  
    private TextView updateAt;  
  
    /** 
     * Layout params for header
     */  
    private MarginLayoutParams headerLayoutParams; 
  
    /** 
     * Last updated time in milliseconds
     */  
    private long lastUpdateTime;
  
    /** 
     * Differentiate different UI
     */  
    private int mId = -1; 
   
    private int hideHeaderHeight;  

    /** 
     * Current state, possible values include STATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH,
     * STATUS_REFRESHING and STATUS_REFRESH_FINISHED 
     */  
    private int currentStatus = STATUS_REFRESH_FINISHED;
  
    /** 
     * Record last state
     */  
    private int lastStatus = currentStatus;  
  
    /** 
     * y axis when touch the screen 
     */  
    private float yDown;  

    private int touchSlop;  
  
    /** 
     * Whether the layout has been loaded
     */  
    private boolean loadOnce;  
  
    /** 
     * Only when ListView reaches the top can the user pull to refresh
     */  
    private boolean ableToPull; 
    
	
	public RefreshableView(Context context) {
		super(context);
	}
	
	public RefreshableView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        preferences = PreferenceManager.getDefaultSharedPreferences(context);  
        headView = LayoutInflater.from(context).inflate(R.layout.listview_head, null, true);  
        progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);  
        arrow = (ImageView) headView.findViewById(R.id.head_arrowImageView);  
        description = (TextView) headView.findViewById(R.id.head_tipsTextView);  
        updateAt = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);  
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();  
        refreshUpdatedAtValue();  
        setOrientation(VERTICAL);  
        addView(headView, 0);  
    }  

	@Override  
    protected void onLayout(boolean changed, int l, int t, int r, int b) {  
        super.onLayout(changed, l, t, r, b);  
        if (changed && !loadOnce) {  
            hideHeaderHeight = - headView.getHeight();  
            headerLayoutParams = (MarginLayoutParams) headView.getLayoutParams();  
            headerLayoutParams.topMargin = hideHeaderHeight;  
            listView = (ListView) getChildAt(1);  
            listView.setOnTouchListener(this);  
            loadOnce = true;  
        }  
    }  
	
	@Override  
    public boolean onTouch(View v, MotionEvent event) {  
        setIsAbleToPull(event);  
        if (ableToPull) {  
            switch (event.getAction()) {  
            case MotionEvent.ACTION_DOWN:  
                yDown = event.getRawY();  
                break;  
            case MotionEvent.ACTION_MOVE:  
                float yMove = event.getRawY();  
                int distance = (int) (yMove - yDown);  
                
                if (distance <= 0 && headerLayoutParams.topMargin <= hideHeaderHeight) {  
                    return false;  
                }  
                if (distance < touchSlop) {  
                    return false;  
                }  
                if (currentStatus != STATUS_REFRESHING) {  
                    if (headerLayoutParams.topMargin > 0) {  
                        currentStatus = STATUS_RELEASE_TO_REFRESH;  
                    } else {  
                        currentStatus = STATUS_PULL_TO_REFRESH;  
                    }  
                    
                    headerLayoutParams.topMargin = (distance / 2) + hideHeaderHeight;  
                    headView.setLayoutParams(headerLayoutParams);  
                }  
                break;  
            case MotionEvent.ACTION_UP:  
            default:  
                if (currentStatus == STATUS_RELEASE_TO_REFRESH) {  
                    // Refresh if status is release to refresh 
                    new RefreshingTask().execute();  
                } else if (currentStatus == STATUS_PULL_TO_REFRESH) {   
                	// Hide header if status is pull to refresh
                    new HideHeaderTask().execute();  
                }  
                break;  
            }  
            // Update header
            if (currentStatus == STATUS_PULL_TO_REFRESH  
                    || currentStatus == STATUS_RELEASE_TO_REFRESH) {  
                updateHeaderView();  
                // ListView loses focus 
                listView.setPressed(false);  
                listView.setFocusable(false);  
                listView.setFocusableInTouchMode(false);  
                lastStatus = currentStatus;  
                // Return true to block out ListView's scrolling events
                return true;  
            }  
        }  
        return false;  
    }  
  
    public void setOnRefreshListener(PullToRefreshListener listener, int id) {  
        mListener = listener;  
        mId = id;  
    }  
  
    public void finishRefreshing() {  
        currentStatus = STATUS_REFRESH_FINISHED;  
        preferences.edit().putLong(UPDATED_AT + mId, System.currentTimeMillis()).commit();  
        new HideHeaderTask().execute();  
    }  
  
    private void setIsAbleToPull(MotionEvent event) {  
        View firstChild = listView.getChildAt(0);  
        if (firstChild != null) {  
            int firstVisiblePos = listView.getFirstVisiblePosition();  
            if (firstVisiblePos == 0 && firstChild.getTop() == 0) {  
                if (!ableToPull) {  
                    yDown = event.getRawY();  
                }  
                 
                ableToPull = true;  
            } else {  
                if (headerLayoutParams.topMargin != hideHeaderHeight) {  
                    headerLayoutParams.topMargin = hideHeaderHeight;  
                    headView.setLayoutParams(headerLayoutParams);  
                }  
                ableToPull = false;  
            }  
        } else {  
            // Allow pull to refresh if ListView has no items
            ableToPull = true;  
        }  
    }
    
    private void updateHeaderView() {  
        if (lastStatus != currentStatus) {  
            if (currentStatus == STATUS_PULL_TO_REFRESH) {  
                description.setText(getResources().getString(R.string.head_update_tips));  
                arrow.setVisibility(View.VISIBLE);  
                progressBar.setVisibility(View.GONE);  
                rotateArrow();  
            } else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {  
                description.setText(getResources().getString(R.string.release_to_refresh));  
                arrow.setVisibility(View.VISIBLE);  
                progressBar.setVisibility(View.GONE);  
                rotateArrow();  
            } else if (currentStatus == STATUS_REFRESHING) {  
                description.setText(getResources().getString(R.string.refreshing));  
                progressBar.setVisibility(View.VISIBLE);  
                arrow.clearAnimation();  
                arrow.setVisibility(View.GONE);  
            }  
            refreshUpdatedAtValue();  
        }  
    }
    
    private void rotateArrow() {  
        float pivotX = arrow.getWidth() / 2f;  
        float pivotY = arrow.getHeight() / 2f;  
        float fromDegrees = 0f;  
        float toDegrees = 0f;  
        if (currentStatus == STATUS_PULL_TO_REFRESH) {  
            fromDegrees = 180f;  
            toDegrees = 360f;  
        } else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {  
            fromDegrees = 0f;  
            toDegrees = 180f;  
        }  
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);  
        animation.setDuration(100);  
        animation.setFillAfter(true);  
        arrow.startAnimation(animation);  
    }  
    
    private void refreshUpdatedAtValue() {  
        lastUpdateTime = preferences.getLong(UPDATED_AT + mId, -1);  
        long currentTime = System.currentTimeMillis();  
        long timePassed = currentTime - lastUpdateTime;  
        long timeIntoFormat;  
        String updateAtValue;  
        if (lastUpdateTime == -1) {  
            updateAtValue = getResources().getString(R.string.not_updated_yet);  
        } else if (timePassed < 0) {  
            updateAtValue = getResources().getString(R.string.time_error);  
        } else if (timePassed < ONE_MINUTE) {  
            updateAtValue = getResources().getString(R.string.updated_just_now);  
        } else if (timePassed < ONE_HOUR) {  
            timeIntoFormat = timePassed / ONE_MINUTE;  
            String value = timeIntoFormat + "min";  
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);  
        } else if (timePassed < ONE_DAY) {  
            timeIntoFormat = timePassed / ONE_HOUR;  
            String value = timeIntoFormat + "hr";  
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);  
        } else if (timePassed < ONE_MONTH) {  
            timeIntoFormat = timePassed / ONE_DAY;  
            String value = timeIntoFormat + "day";  
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);  
        } else if (timePassed < ONE_YEAR) {  
            timeIntoFormat = timePassed / ONE_MONTH;  
            String value = timeIntoFormat + "month";  
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);  
        } else {  
            timeIntoFormat = timePassed / ONE_YEAR;  
            String value = timeIntoFormat + "year";  
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);  
        }  
        updateAt.setText(updateAtValue);  
    } 
    
    class RefreshingTask extends AsyncTask<Void, Integer, Void> {  
    	  
        @Override  
        protected Void doInBackground(Void... params) {  
            int topMargin = headerLayoutParams.topMargin;  
            while (true) {  
                topMargin = topMargin + SCROLL_SPEED;  
                if (topMargin <= 0) {  
                    topMargin = 0;  
                    break;  
                }  
                publishProgress(topMargin);  
                sleep(10);  
            }  
            currentStatus = STATUS_REFRESHING;  
            publishProgress(0);  
            if (mListener != null) {  
                mListener.onRefresh();  
            }  
            return null;  
        }  
  
        @Override  
        protected void onProgressUpdate(Integer... topMargin) {  
            updateHeaderView();  
            headerLayoutParams.topMargin = topMargin[0];  
            headView.setLayoutParams(headerLayoutParams);  
        }  
    } 
    
    class HideHeaderTask extends AsyncTask<Void, Integer, Integer> {  
    	  
        @Override  
        protected Integer doInBackground(Void... params) {  
            int topMargin = headerLayoutParams.topMargin;  
            while (true) {  
                topMargin = topMargin + SCROLL_SPEED;  
                if (topMargin <= hideHeaderHeight) {  
                    topMargin = hideHeaderHeight;  
                    break;  
                }  
                publishProgress(topMargin);  
                sleep(10);  
            }  
            return topMargin;  
        }  
  
        @Override  
        protected void onProgressUpdate(Integer... topMargin) {  
            headerLayoutParams.topMargin = topMargin[0];  
            headView.setLayoutParams(headerLayoutParams);  
        }  
  
        @Override  
        protected void onPostExecute(Integer topMargin) {  
            headerLayoutParams.topMargin = topMargin;  
            headView.setLayoutParams(headerLayoutParams);  
            currentStatus = STATUS_REFRESH_FINISHED;  
        }  
    } 
    
    private void sleep(int time) {  
        try {  
            Thread.sleep(time);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
    }  
    
    public interface PullToRefreshListener {     	  
        public void onRefresh();  
    }  
    
}
