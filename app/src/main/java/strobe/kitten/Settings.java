package strobe.kitten;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.*;

public class Settings extends Activity {
	public static final String PREF_RATE = "rate";
	public static final int PREF_RATE_DEFAULT = 20;
	public static final String PREF_SHOW_KITTEN = "mShowKitten";
	public static final boolean PREF_SHOW_KITTEN_DEFAULT = true;
	public static final String PREF_WARNING_SHOWED = "warned";
	public static final boolean PREF_WARNING_SHOWED_DEFAULT = false;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        //Get the seekbar and radio group
        SeekBar sb = findViewById(R.id.seekBar);
        RadioGroup rg = findViewById(R.id.kittyCat);

        //Restore values from settings
    	final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    	final int rate = sp.getInt(PREF_RATE, PREF_RATE_DEFAULT);
        sb.setProgress(rate);
        rg.check(sp.getBoolean(PREF_SHOW_KITTEN, PREF_SHOW_KITTEN_DEFAULT) ? R.id.kitty : R.id.cat);
		TextView text = findViewById(R.id.rate);
		text.setText(getString(R.string.number_percent, rate));

        sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				TextView text = findViewById(R.id.rate);
				text.setText(getString(R.string.number_percent, progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				sp.edit()
					.putInt(PREF_RATE, seekBar.getProgress())
					.commit();
			}

        });

        rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				sp.edit()
					.putBoolean(PREF_SHOW_KITTEN, checkedId == R.id.kitty)
					.commit();
			}
        });
    }
}
