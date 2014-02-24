package de.hsosnabrueck.iui.informatik.vma.hipsterbility.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import de.hsosnabrueck.iui.informatik.R;
import de.hsosnabrueck.iui.informatik.vma.hipsterbility.Hipsterbility;
import de.hsosnabrueck.iui.informatik.vma.hipsterbility.modules.AbstractModule;
import de.hsosnabrueck.iui.informatik.vma.hipsterbility.sessions.Session;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Albert Hoffmann on 13.02.14.
 */
public class CaptureService extends Service implements SurfaceHolder.Callback{

    // TODO: Implementation

    //================================================================================
    // Properties
    //================================================================================

    public static final String VIDEOS_DIR = "videos";
    private Session session;

    private ArrayList<AbstractModule> modules;

    private WindowManager windowManager;
    private SurfaceView surfaceView;
    private Camera camera = null;
    private MediaRecorder mediaRecorder = null;

    //================================================================================
    // Constructors
    //================================================================================

    public CaptureService(){
        this.modules = new ArrayList<AbstractModule>();
    }

    //================================================================================
    // Public Methods
    //================================================================================

    @Override
    public void onCreate() {
        super.onCreate();
        // Start foreground service to avoid unexpected kill
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Background Video Recorder")
                .setContentText("")
                .setSmallIcon(R.drawable.ic_launcher)
                .build();
        startForeground(1234, notification);

        // Create new SurfaceView, set its size to 1x1, move it to the top left corner and set this service as a callback
        windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        surfaceView = new SurfaceView(this);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                1, 1,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
        );
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        windowManager.addView(surfaceView, layoutParams);
        surfaceView.getHolder().addCallback(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {


        boolean found = false;
        int i;
        for (i=0; i< Camera.getNumberOfCameras(); i++) {
            Camera.CameraInfo newInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, newInfo);
            if (newInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                found = true;
                break;
            }
        }
        camera = Camera.open(i);
        mediaRecorder = new MediaRecorder();
        camera.unlock();

        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        // Set quality to low, because high does not work with front facing camera.
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));

        mediaRecorder.setOutputFile(
                getOuputFileName()
        );

        try { mediaRecorder.prepare(); } catch (Exception e) {}
        mediaRecorder.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.session = intent.getParcelableExtra("Session");
//        AudioCaptureModule audioCap = new AudioCaptureModule(session);
//        this.modules.add(audioCap);
        //CameraCapture camCap = new CameraCapture(this, session);
        //this.modules.add(camCap);
        for(AbstractModule module:modules){
            module.startCapture();
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        for(AbstractModule module:modules){
            module.stopCapture();
        }
        super.onDestroy();

        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();

        camera.lock();
        camera.release();

        windowManager.removeView(surfaceView);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {}

    //================================================================================
    // Private Methods
    //================================================================================

    private String getOuputFileName(){
        return Environment.getExternalStorageDirectory()
                + File.separator
                + Hipsterbility.BASE_DIR
                + File.separator
                + VIDEOS_DIR
                + File.separator
                + session.getId()
                + File.separator
                + System.currentTimeMillis()
                + ".mp4";
    }
}