package com.becerra.javier.enviarmensajes.trigger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

//Se desactiva para validacion
//import org.iilab.pb.alert.PanicAlert;
import com.becerra.javier.enviarmensajes.common.AppConstants;
import com.becerra.javier.enviarmensajes.common.ApplicationSettings;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.ACTION_BOOT_COMPLETED;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Map<String, String> eventLog = new HashMap<String, String>();
        eventLog.put("Restarted the app on booting", new Date(System.currentTimeMillis()).toString());
        if(intent.getAction().equals(ACTION_BOOT_COMPLETED)) {
//        	new PanicAlert(context).deActivate();
            if(ApplicationSettings.isAlertActive(context)){
                Log.e("BootReceiver", "Alarm is active");
                ApplicationSettings.setAlertActive(context, false);
                //TODO: Se desactiva para validacion
                //new PanicAlert(context).activate();
            }

            int wizardState = ApplicationSettings.getWizardState(context.getApplicationContext());
            Log.e("BootReceiver", "wizardState = " + wizardState);
            if (wizardState == AppConstants.WIZARD_FLAG_HOME_READY) {
                Log.e("BootReceiver", "BootReceiver in Panic Button");
                context.startService(new Intent(context, HardwareTriggerService.class));
            }
        }
    }
}

