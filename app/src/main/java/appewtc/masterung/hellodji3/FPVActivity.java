package appewtc.masterung.hellodji3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import dji.sdk.api.DJIDrone;
import dji.sdk.api.DJIDroneTypeDef;
import dji.sdk.interfaces.DJIGerneralListener;
import dji.sdk.interfaces.DJIReceivedVideoDataCallBack;
import dji.sdk.widget.DjiGLSurfaceView;

public class FPVActivity extends AppCompatActivity {

    //Explicit
    private static final String TAG = "MyApp";
    private int DroneCode;
    private DJIReceivedVideoDataCallBack mReceivedVideoDataCallBack = null;
    private DjiGLSurfaceView mDjiGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fpv);

        //Create Thread
        new Thread(){
            public void run(){
                try{

                    DJIDrone.checkPermission(getApplicationContext(), new DJIGerneralListener() {
                        @Override
                        public void onGetPermissionResult(int result) {
                            Log.e(TAG, "Permission ==> " + result);
                        }
                    });

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

        //Choose Type Drone
        DJIDrone.initWithType(this.getApplicationContext(), DJIDroneTypeDef.DJIDroneType.DJIDrone_Phantom3_Advanced);


        DJIDrone.connectToDrone(); // Connect to the drone

        mDjiGLSurfaceView = (DjiGLSurfaceView)findViewById(R.id.DjiSurfaceView_02);
        mDjiGLSurfaceView.start();

        mReceivedVideoDataCallBack = new DJIReceivedVideoDataCallBack() {
            @Override
            public void onResult(byte[] videoBuffer, int size) {

                mDjiGLSurfaceView.setDataToDecoder(videoBuffer, size);

            }

        };
          DJIDrone.getDjiCamera().setReceivedVideoDataCallBack(mReceivedVideoDataCallBack);

    } // onCreate


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (DJIDrone.getDjiCamera() != null) {
            DJIDrone.getDjiCamera().setReceivedVideoDataCallBack(null);
        }
        mDjiGLSurfaceView.destroy();

    }   // onDestroy



}   // Main Class
