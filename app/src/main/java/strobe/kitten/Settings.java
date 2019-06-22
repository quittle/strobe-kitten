package strobe.kitten;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.*;

public class Settings extends Activity{
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        //Get the seekbar and radio group
        SeekBar sb = (SeekBar) findViewById(R.id.seekBar);
        RadioGroup rg = (RadioGroup) findViewById(R.id.kittyCat);
        
        //Restore values from settings
    	final SharedPreferences sp = getSharedPreferences("settings", MODE_WORLD_READABLE);
        sb.setProgress(sp.getInt("rate", 20));
        rg.check(sp.getBoolean("kitten", true)?R.id.kitty:R.id.cat);
		TextView text = (TextView) findViewById(R.id.rate);
		text.setText(String.valueOf(sp.getInt("rate", 20) ) + "%");
        
		//Force them to
        sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				TextView text = (TextView) findViewById(R.id.rate);
				text.setText(String.valueOf(progress) + "%");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Editor e = sp.edit();
				e.putInt("rate", seekBar.getProgress());
				e.commit();
				
			}
        	
        });
        
        rg.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				Editor e = sp.edit();
				e.putBoolean("kitten", checkedId==R.id.kitty);
				e.commit();
			}
        	
        });
    }
}
