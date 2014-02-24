package de.hsosnabrueck.iui.informatik.vma.hipsterbility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import de.hsosnabrueck.iui.informatik.vma.hipsterbility.modules.ScreenshotTaker;
import de.hsosnabrueck.iui.informatik.vma.hipsterbility.services.CaptureService;
import de.hsosnabrueck.iui.informatik.vma.hipsterbility.services.HipsterbilityService;
import de.hsosnabrueck.iui.informatik.vma.hipsterbility.sessions.*;

import java.util.ArrayList;

/**
 * The Hipsterbility class is a monolithic wrapper for the Hipsterbility-library and implements all public methods which
 * a user can use in own application, similar to the facade pattern.
 */
public class Hipsterbility{

    //================================================================================
    // Properties
    //================================================================================


//    Activity activity; // Calling activity to get context for Service TODO: check if needed later on
    private static Hipsterbility instance = new Hipsterbility();
    //Base dir for stored files on SD-Card
    public static final String BASE_DIR = "hipsterbility";

    private Context context;
    private Activity activity;
    private SharedPreferences sharedPreferences;

    //================================================================================
    // Constructors
    //================================================================================

    /**
     * The default constructor to create a Hipsterbility object.
     * It uses default values.
     */

    private Hipsterbility(){
        //TODO: remove after testing
        createTestData();
    }

    //================================================================================
    // public Methods
    //================================================================================


    public static Hipsterbility getInstance(){
        return instance;
    }
    public String test() {
       // TODO: delete method, just for dev testing purposes
       return "hello Hipsterbility";
    }

    public void testAlert(Activity activity) {
        // TODO: delete method, just for dev testing purposes
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage("Alert from lib")
                .setTitle(this.test());

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    //TODO: delete me after testing
    public void testCapture() {

        // use this to start and trigger a service

        Session session = new Session(123);
        Intent i= new Intent(context, CaptureService.class);
        i.putExtra("Session", session);
        context.startService(i);
    }

    public void stopCapture() {
        context.stopService(new Intent(context, CaptureService.class));
    }


    public void enableTesting(Activity activity) {
        //TODO usefull stuff
        this.activity = activity;
        this.context = activity.getApplicationContext();
        //TODO remove after testing
        testScreenshot(activity);
//        activity.startActivity(new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
        startService();
    }



    //================================================================================
    // Private Methods
    //================================================================================

    private void startService(){
        Intent i= new Intent(context, HipsterbilityService.class);
        context.startService(i);
    }


    //TODO: delete after testing
    private void createTestData() {
        SessionManager sm = SessionManager.getInstace();
//        TodoManager tm = TodoManager.getInstance();
        ArrayList<Session> sessions = new ArrayList<Session>();
        Session s = new Session(1, "wurst", "mhhhh, tasty", "Nexus", "bla", true);
        sessions.add(s);
        Todo todo = new Todo(1,"Todo 1","Description goes here",true,s);
        Task task = new Task(1,"Task 1", todo);
        todo.addTask(task);
        s.addTodo(todo);
//        tm.addTodo(s,t);

        sessions.add(new Session(3, "salat", "vitamins ftw", "Galaxy", "blubb", false));
        sessions.add(new Session(5, "foo", "poop! i did it again", "Nexus", "blubb", true));
        sessions.add(new Session(2 ,"bar", "vodka lemon", "HTC", "blubb", false));
        sessions.add(new Session(25, "baz", "more bass digga", "Dumbphone", "bla", true));
        sessions.add(new Session(13, "42", "The answer to the question of quesions", "Nexus", "bla", true));
        sessions.add(new Session(34, "baz", "more bass digga", "Dumbphone", "bla", false));
        sessions.add(new Session(4, "baz", "more bass digga", "Dumbphone", "bla", true));
        sm.setSessions(sessions);
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 10; j++){
                Todo t = new Todo(j++,"TODO " + j ,"Todo desription goes here",j%2==0,sessions.get(i));
                for(int x = 0; x < 10; x++){
                    t.addTask(new Task(x,"task " + x, t));
                }
                sessions.get(i).addTodo(t);
            }
        }
        sm.setSessions(sessions);
    }


    private void testScreenshot(Activity activity) {
        Session session = new Session(124);
        ScreenshotTaker s = new ScreenshotTaker(session);
//        s.takeScreenshot(activity);
//        s.takeScreenshotRoot();
        s.takeContinuousScreenshots(activity, 10, 100, false);
    }


}