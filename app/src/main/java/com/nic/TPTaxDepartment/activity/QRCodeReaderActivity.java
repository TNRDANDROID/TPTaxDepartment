package com.nic.TPTaxDepartment.activity;

import android.app.Activity;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.utils.Utils;


public class QRCodeReaderActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {
    private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;
    RelativeLayout whole_ll,swipe_rl,document_rl;
    View bar;
    Animation animation;
    public static MediaPlayer mp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_reader_view);

         bar = findViewById(R.id.bar);
         whole_ll=findViewById(R.id.whole_ll);
         swipe_rl=findViewById(R.id.scanner_rl);
         document_rl=findViewById(R.id.document_layout);

         animation = AnimationUtils.loadAnimation(this, R.anim.scanning_animation_view);
        bar.setVisibility(View.VISIBLE);
        bar.startAnimation(animation);
        //animation.start();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                bar.setVisibility(View.VISIBLE);
                bar.startAnimation(animation);
                //animation.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
               /*bar.setVisibility(View.VISIBLE);
                bar.startAnimation(animation);*/
            //animation.start();
            }
        });

        mp = MediaPlayer.create(this,R.raw.beep);

        resultTextView=findViewById(R.id.qr_code_text);
        qrCodeReaderView=findViewById(R.id.qrdecoderview);

        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);




        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        //qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
        qrCodeReaderView.setSoundEffectsEnabled(true);

        qrCodeReaderView.startCamera();
        //whole_ll.setOnTouchListener(new RelativeLayoutTouchListener(this));



    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        mp.start();
        resultTextView.setText(text);
        bar.setVisibility(View.GONE);
        bar.animate().cancel();
        //animation.cancel();
        Utils.showAlert(this,"QrCode Successfully Read QRCode : "+ text);
        qrCodeReaderView.stopCamera();

        //finish();

    }
    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

    public class RelativeLayoutTouchListener implements View.OnTouchListener{



            static final String logTag = "ActivitySwipeDetector";
            private Activity activity;
            static final int MIN_DISTANCE = 100;// TODO change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance
            private float downX, downY, upX, upY;

            // private MainActivity mMainActivity;

        public RelativeLayoutTouchListener(Activity mainActivity) {
            activity = mainActivity;
        }

            public void onRightToLeftSwipe() {
            Log.i(logTag, "RightToLeftSwipe!");
            Toast.makeText(activity, "RightToLeftSwipe", Toast.LENGTH_SHORT).show();


            // activity.doSomething();
        }

            public void onLeftToRightSwipe() {
            Log.i(logTag, "LeftToRightSwipe!");
            Toast.makeText(activity, "LeftToRightSwipe", Toast.LENGTH_SHORT).show();



            // activity.doSomething();
        }

            public void onTopToBottomSwipe() {
            Log.i(logTag, "onTopToBottomSwipe!");
            Toast.makeText(activity, "onTopToBottomSwipe", Toast.LENGTH_SHORT).show();
            // activity.doSomething();
        }

            public void onBottomToTopSwipe() {
            Log.i(logTag, "onBottomToTopSwipe!");
            Toast.makeText(activity, "onBottomToTopSwipe", Toast.LENGTH_SHORT).show();
            // activity.doSomething();
        }

            public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    downY = event.getY();
                    return true;
                }
                case MotionEvent.ACTION_UP: {
                    upX = event.getX();
                    upY = event.getY();

                    float deltaX = downX - upX;
                    float deltaY = downY - upY;

                    // swipe horizontal?
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        // left or right
                        if (deltaX < 0) {
                            this.onLeftToRightSwipe();
                            return true;
                        }
                        if (deltaX > 0) {
                            this.onRightToLeftSwipe();
                            return true;
                        }
                    } else {
                        Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long horizontally, need at least " + MIN_DISTANCE);
                        // return false; // We don't consume the event
                    }

                    // swipe vertical?
                    if (Math.abs(deltaY) > MIN_DISTANCE) {
                        // top or down
                        if (deltaY < 0) {
                            this.onTopToBottomSwipe();
                            return true;
                        }
                        if (deltaY > 0) {
                            this.onBottomToTopSwipe();
                            return true;
                        }
                    } else {
                        Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long vertically, need at least " + MIN_DISTANCE);
                        // return false; // We don't consume the event
                    }

                    return false; // no swipe horizontally and no swipe vertically
                }// case MotionEvent.ACTION_UP:
            }
            return false;
        }

        }




}
