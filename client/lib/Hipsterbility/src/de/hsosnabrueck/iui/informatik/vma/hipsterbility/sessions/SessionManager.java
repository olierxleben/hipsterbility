package de.hsosnabrueck.iui.informatik.vma.hipsterbility.sessions;

import java.util.ArrayList;

/**
 * Created by Albert Hoffmann on 13.02.14.
 */
public class SessionManager {

    private static SessionManager instance = new SessionManager();

    private ArrayList<Session> sessions;
    private Session sessionInProgress;

    private SessionManager() {
        sessionInProgress = null;
        this.sessions = new ArrayList<Session>();
    }

    public ArrayList<Session> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<Session> sessions) {
        this.sessions = sessions;
    }

    public Session getSessionInProgress() {
        return sessionInProgress;
    }

    public void setSessionInProgress(Session sessionInProgress) {
        this.sessionInProgress = sessionInProgress;
    }

    public static SessionManager getInstace(){
        return instance;
    }
}
