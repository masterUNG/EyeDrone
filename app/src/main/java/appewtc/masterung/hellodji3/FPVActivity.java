package appewtc.masterung.hellodji3;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import dji.midware.data.manager.P3.ServiceManager;
import dji.sdk.api.DJIDrone;
import dji.sdk.api.DJIDroneTypeDef;
import dji.sdk.interfaces.DJIGerneralListener;
import dji.sdk.interfaces.DJIReceivedVideoDataCallBack;
import dji.sdk.widget.DjiGLSurfaceView;

public class FPVActivity extends Activity{

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
                            Log.d(TAG, "Permission ==> " + result);
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

        // Connect to the drone
        DJIDrone.connectToDrone();

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
    protected void onResume() {
        super.onResume();

        DJIDrone.getDjiMC().startUpdateTimer(1000); // Start the update timer for MC to update info
        ServiceManager.getInstance().pauseService(false);

    }

    @Override
    protected void onPause() {
        super.onPause();

        DJIDrone.getDjiMC().stopUpdateTimer(); // Stop the update timer for MC to update info
        ServiceManager.getInstance().pauseService(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (DJIDrone.getDjiCamera() != null) {
            DJIDrone.getDjiCamera().setReceivedVideoDataCallBack(null);
        }
        mDjiGLSurfaceView.destroy();

    }   // onDestroy



}   // Main Class
