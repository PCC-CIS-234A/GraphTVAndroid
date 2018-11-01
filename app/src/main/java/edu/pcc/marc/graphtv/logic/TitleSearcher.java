package edu.pcc.marc.graphtv.logic;

import android.content.Context;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import edu.pcc.marc.graphtv.data.TitleData;

public class TitleSearcher {
    private ArrayList<ShowListener> m_Listeners;
    private String m_Pending = null;
    private boolean m_Searching = false;
    private Timer m_SearchDelayTimer;
    private static final long DELAY = 100;
    private static final int MAX_SHOWS = 200;

    public TitleSearcher(Context context) {
        m_Listeners = new ArrayList<>();
        m_SearchDelayTimer = new Timer("Search Delay Timer");
        TitleData.init(context);
    }

    public void search(final String title) {
        m_Pending = title;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (title == m_Pending) {
                    ArrayList<Show> shows = TitleData.findShowsByTitle(title, MAX_SHOWS);
                    showsArrived(shows);
                }
            }
        };
        m_SearchDelayTimer.schedule(task, DELAY);
    }

    private void showsArrived(ArrayList<Show> shows) {
        for (ShowListener listener: m_Listeners) {
            listener.showsArrived(shows);
        }
    }

    public void addShowListener(ShowListener listener) {
        m_Listeners.add(listener);
    }
}
