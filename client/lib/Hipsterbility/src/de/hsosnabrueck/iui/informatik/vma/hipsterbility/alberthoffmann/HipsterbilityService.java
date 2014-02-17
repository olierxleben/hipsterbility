package de.hsosnabrueck.iui.informatik.vma.hipsterbility.alberthoffmann;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import de.hsosnabrueck.iui.informatik.R;

/**
 * Created by Albert Hoffmann on 17.02.14.
 */
public class HipsterbilityService extends Service {


    public HipsterbilityService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, CaptureService.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification n = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Hipsterbility")
                .setContentText("Usability testing enabled")
                .addAction(R.drawable.ic_launcher, "Start testing", pIntent)
                .build();
        startForeground(1234, n);
    }



    public IBinder onBind(Intent intent) {
        return null;
    }

}
