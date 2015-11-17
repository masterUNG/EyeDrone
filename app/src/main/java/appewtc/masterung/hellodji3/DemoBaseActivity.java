package appewtc.masterung.hellodji3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import dji.midware.data.manager.P3.ServiceManager;

public class DemoBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_base);
    }   // onCreate

    @Override
    protected void onResume() {
        super.onResume();
        ServiceManager.getInstance().pauseService(false); // Resume the service
    }   // onResume

    @Override
    protected void onPause() {
        super.onPause();
        ServiceManager.getInstance().pauseService(true); // Pause the service
    }   // onPause

}   // Main Class
