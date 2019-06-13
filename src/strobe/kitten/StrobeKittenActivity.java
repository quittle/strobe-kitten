package strobe.kitten;

import strobe.kitten.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class StrobeKittenActivity extends Activity {
	final int SYSTEM_UI_FLAG_HIDE_NAVIGATION = 2;
	CountDownTimer ct;
	boolean paused = true;
	boolean kitten;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	
    	SharedPreferences sp = getSharedPreferences("settings", MODE_WORLD_READABLE);
    	if(!sp.getBoolean("warned", false)){
    		startActivity(new Intent(getApplicationContext(), Warning.class));
    	}
    	kitten = sp.getBoolean("kitten", true);
    	pause(null);
    	ct.start();
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	paused = true;
    	ct.cancel();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		startActivity(new Intent(getApplicationContext(), Settings.class));
		return true;
    }
    
    public void pause(View v){
    	if(paused){
    	   	SharedPreferences sp = getSharedPreferences("settings", MODE_WORLD_READABLE);
    		ct = new CountDownTimer(1000/Math.max(sp.getInt("rate", 10), 1),10000000){

    			@Override
    			public void onFinish() {
    				imageSwitch();
    				start();
    			}

    			@Override
    			public void onTick(long millisUntilFinished) {
    			}
        		
        	};
    	   	ct.start();
    	   	Toast.makeText(getApplicationContext(), "Strobing!", Toast.LENGTH_SHORT).show();
        	paused = false;
    	}
    	else{
    		ct.cancel();
    	   	Toast.makeText(getApplicationContext(), "Paused!", Toast.LENGTH_SHORT).show();
    		paused = true;
    	}
    }
    
    private void imageSwitch(){
    	ImageView iv = (ImageView) findViewById(R.id.imageView1);
    	LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
		Resources res = getResources();
        if(((String)iv.getTag()).equals("1")){
        	ll.setBackgroundColor(Color.BLACK);
        	iv.setBackgroundColor(Color.BLACK);
        	iv.setImageDrawable(res.getDrawable(kitten?R.drawable.kitten1:R.drawable.cat1));
        	iv.invalidate();
        	iv.refreshDrawableState();
        	iv.setTag(String.valueOf("2"));
        }
        else{
        	ll.setBackgroundColor(Color.WHITE);
        	iv.setBackgroundColor(Color.WHITE);
        	iv.setImageDrawable(res.getDrawable(kitten?R.drawable.kitten2:R.drawable.cat2));
        	iv.invalidate();
        	iv.refreshDrawableState();
        	iv.setTag(String.valueOf("1"));
        }
    }
}