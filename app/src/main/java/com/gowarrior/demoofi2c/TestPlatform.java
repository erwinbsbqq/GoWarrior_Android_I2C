package com.gowarrior.demoofi2c;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by GoWarrior on 2015/9/6.
 */
public class TestPlatform extends Activity {

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        Intent intent = new Intent(TestPlatform.this, TestForI2C.class);
        startService(intent);
    }

}
