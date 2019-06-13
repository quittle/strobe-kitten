package strobe.kitten;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Warning extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warning);
        
        Button b = (Button) findViewById(R.id.button1);
        b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
		    	SharedPreferences sp = getSharedPreferences("settings", MODE_WORLD_READABLE);
				Editor e = sp.edit();
				e.putBoolean("warned", true);
				e.commit();
				startActivity(new Intent(getApplicationContext(), StrobeKittenActivity.class));
			}
        });
    }
}
