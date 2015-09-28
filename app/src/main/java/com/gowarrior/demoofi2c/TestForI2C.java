package com.gowarrior.demoofi2c;

import android.app.Service;
import android.content.Intent;
import android.gowarrior.SMBus;
import android.os.IBinder;

/**
 * Created by GoWarrior on 2015/9/6.
 */
public class TestForI2C extends Service {

    private SMBus bus;
    private int address = 0x68;
    private int power_mgmt_1 = 0x6b;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        testI2C();
    }

    private int read_word(int adr)
    {
        int high = bus.read_byte_data(address, adr);
        int low = bus.read_byte_data(address, adr+1);
        int val = (high << 8) + low;
        return val;
    }

    private int read_word_2c(int adr)
    {
        int val = read_word(adr);
        if(val >= 0x8000)
            return -((65535 - val) + 1);
        else
            return val;
    }

    private void testI2C()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bus = new SMBus();
                bus.open(1);
                bus.write_byte_data(address, power_mgmt_1, 0);
                double accel_xout, accel_yout, accel_zout;
                double accel_xout_scaled, accel_yout_scaled, accel_zout_scaled;
                while (true){
                    accel_xout = read_word_2c(0x3b);
                    accel_yout = read_word_2c(0x3d);
                    accel_zout = read_word_2c(0x3f);

                    accel_xout_scaled = accel_xout / 16384.0;
                    accel_yout_scaled = accel_yout / 16384.0;
                    accel_zout_scaled = accel_zout / 16384.0;

                    System.out.println("accel_xout: " + accel_xout + ",scaled: " + accel_xout_scaled);
                    System.out.println("accel_yout: " + accel_yout + ",scaled: " + accel_yout_scaled);
                    System.out.println("accel_zout: " + accel_zout + ",scaled: " + accel_zout_scaled);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
