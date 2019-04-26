/**
 * Tests can fail for other reasons than code, it´ because of the animations and espresso sync and
 * emulator state (screen off or locked)
 * <p/>
 * Before all the tests prepare the device to run tests and avoid these problems.
 * <p/>
 * - Disable animations
 * - Disable keyguard lock
 * - Set it to be awake all the time (dont let the processor sleep)
 *
 * @see <a href="u2020 open source app by Jake Wharton">https://github.com/JakeWharton/u2020</a>
 * @see <a href="Daj gist">https://gist.github.com/daj/7b48f1b8a92abf960e7b</a>
 **/

package com.example.EnglishDictionary;

import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

public final class TestRunner extends androidx.test.runner.AndroidJUnitRunner
{
    private PowerManager.WakeLock mWakeLock;

    @Override
    public void callApplicationOnCreate(Application app)
    {
        // Unlock the screen
        KeyguardManager keyguard = (KeyguardManager) app.getSystemService(Context.KEYGUARD_SERVICE);
        keyguard.newKeyguardLock(getClass().getSimpleName()).disableKeyguard();

        // Start a wake lock
        PowerManager power = (PowerManager) app.getSystemService(Context.POWER_SERVICE);
        mWakeLock = power.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, getClass().getSimpleName());
        mWakeLock.acquire();

        super.callApplicationOnCreate(app);
    }

    @Override
    public void onDestroy()
    {
        mWakeLock.release();

        super.onDestroy();
    }
}

