package strobe.kitten;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;

public class Warning extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warning);

        findViewById(R.id.continue_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
			    final Context context = getApplicationContext();
                PreferenceManager.getDefaultSharedPreferences(context).edit()
				    .putBoolean(Settings.PREF_WARNING_SHOWED, true)
				    .commit();
				startActivity(new Intent(context, StrobeKittenActivity.class));
			}
        });
    }
}
