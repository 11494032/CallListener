package com.example.moresmart_pc006.calllistener;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MoreSmart-PC006 on 2016/9/8.
 */
public class CallLIstenerService extends Service {
    @Nullable

    private boolean isCalling = false;
    private MediaRecorder mediaRecorder = null;
    private TelephonyManager telephonyManager = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        telephonyManager = (TelephonyManager)getSystemService( TELEPHONY_SERVICE );
        telephonyManager.listen( new PhoneStateListeners(),PhoneStateListener .LISTEN_CALL_STATE );

    }
    private class PhoneStateListeners extends PhoneStateListener
    {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            switch ( state )
            {
                case TelephonyManager.CALL_STATE_IDLE: {
                    System.out.println("CALL_STATE_IDLE");
                    if( isCalling )
                    {
                        isCalling = false;
                        mediaRecorder.stop();
                        mediaRecorder.reset();
                        mediaRecorder.release();
                        Log.d("tag", "录音结束");
                    }
                }
                    break;

                case TelephonyManager.CALL_STATE_RINGING:
                    System.out.println("CALL_STATE_RINGING");
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK: {
                    System.out.println("CALL_STATE_OFFHOOK");
                    Log.d("tag", "开始通话");

                    SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd_hh_mm_ss");

                    String date = format.format(new Date());

                    isCalling = true;
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+date+".3gp");
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                }
                    break;

                default:
                    break;

            }
        }
    }


}
