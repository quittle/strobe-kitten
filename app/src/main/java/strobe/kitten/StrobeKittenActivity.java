package strobe.kitten;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class StrobeKittenActivity extends Activity {
    private static final String BUNDLE_KEY_PAUSED = "paused";
    private static final String BUNDLE_KEY_FRAME = "frame";

    CountDownTimer mCountDownTimer;
    boolean mPaused = false;
    int mFrame = 1;
    boolean mShowKitten;
    SharedPreferences mSharedPreferences;
    ImageView mKittenView;
    LinearLayout mLayoutRoot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mLayoutRoot = findViewById(R.id.layout_root);
        mKittenView = findViewById(R.id.kitten);

        if (savedInstanceState != null) {
            mPaused = savedInstanceState.getBoolean(BUNDLE_KEY_PAUSED, mPaused);
            mFrame = savedInstanceState.getInt(BUNDLE_KEY_FRAME, mFrame);
        }

        renderFrame(mFrame);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePaused();
            }
        };

        mLayoutRoot.setOnClickListener(onClickListener);
        mKittenView.setOnClickListener(onClickListener);

        final View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(view.getContext(), Settings.class));
                return true;
            }
        };

        mLayoutRoot.setOnLongClickListener(onLongClickListener);
        mKittenView.setOnLongClickListener(onLongClickListener);
    }

    @Override
    public void onSaveInstanceState(final Bundle savedInstanceState) {
        savedInstanceState.putBoolean(BUNDLE_KEY_PAUSED, mPaused);
        savedInstanceState.putInt(BUNDLE_KEY_FRAME, mFrame);
    }

    @Override
    public void onResume(){
        super.onResume();

        final boolean warningShown = mSharedPreferences
                .getBoolean(Settings.PREF_WARNING_SHOWED, Settings.PREF_WARNING_SHOWED_DEFAULT);

        if (!warningShown) {
            startActivity(new Intent(this, Warning.class));
        }

        mShowKitten = mSharedPreferences.getBoolean(
                Settings.PREF_SHOW_KITTEN, Settings.PREF_SHOW_KITTEN_DEFAULT);
        renderFrame(mFrame);

        if (!mPaused) {
            resumeStrobe();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        pauseStrobe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, Settings.class));
        return true;
    }

    private void togglePaused() {
        if (mPaused) {
            resumeStrobe();
        } else {
            pauseStrobe();
        }
        mPaused = !mPaused;
    }

    private void pauseStrobe() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    public void resumeStrobe() {
        final int rate = mSharedPreferences.getInt(Settings.PREF_RATE, Settings.PREF_RATE_DEFAULT);
        final int millisInFuture = 1000 / Math.max(rate, 1);
        // This is recreated ever time to handle rate updates
        mCountDownTimer = new CountDownTimer(millisInFuture, 10 * 1000 * 1000){
            @Override
            public void onFinish() {
                switchImage();
                start();
            }

            @Override
            public void onTick(long millisUntilFinished) {}
        };
        mCountDownTimer.start();
    }

    private void switchImage() {
        if (mFrame == 1) {
            mFrame = 2;
            renderFrame(2);
        } else {
            mFrame = 1;
            renderFrame(1);
        }
    }

    private void renderFrame(int frame) {
        if (frame == 1) {
            mLayoutRoot.setBackgroundColor(Color.BLACK);
            mKittenView.setBackgroundColor(Color.BLACK);
            mKittenView.setImageResource(mShowKitten ? R.drawable.kitten1 : R.drawable.cat1);
        } else if (frame == 2) {
            mLayoutRoot.setBackgroundColor(Color.WHITE);
            mKittenView.setBackgroundColor(Color.WHITE);
            mKittenView.setImageResource(mShowKitten ? R.drawable.kitten2 : R.drawable.cat2);
        }
        mKittenView.invalidate();
        mKittenView.refreshDrawableState();
    }
}
