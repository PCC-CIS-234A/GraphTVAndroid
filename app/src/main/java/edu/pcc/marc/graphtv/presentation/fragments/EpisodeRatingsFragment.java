package edu.pcc.marc.graphtv.presentation.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.pcc.marc.graphtv.R;
import edu.pcc.marc.graphtv.data.WebData;
import edu.pcc.marc.graphtv.logic.Episode;
import edu.pcc.marc.graphtv.logic.Show;
import edu.pcc.marc.graphtv.main.MainActivity;
import edu.pcc.marc.graphtv.presentation.components.DataPoint;
import edu.pcc.marc.graphtv.presentation.components.GraphView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EpisodeRatingsFragment extends Fragment {
    public static final String TAG = "EpisodeRatingsFragment";
    private static final String EPISODES_KEY = "episodes";
    private static final String TITLE_KEY = "title";
    private GraphView m_GraphView;
    private ArrayList<Episode> m_Episodes = null;
    private String m_Title = null;

    public EpisodeRatingsFragment() {
        // Required empty public constructor
    }

    public void showEpisodeGraph(final MainActivity activity, final Show series) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<Episode> episodes = WebData.fetchEpisodes(series.getID());
                Log.d(TAG, episodes.toString());
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        m_Episodes = episodes;
                        m_Title = series.getTitle();
                        if (m_GraphView != null) {
                            m_GraphView.setEpisodes(episodes, series.getTitle());
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_episode_ratings, container, false);
        m_GraphView = (GraphView) rootView.findViewById(R.id.graph_view);
        if (savedInstanceState != null) {
            m_Episodes = savedInstanceState.getParcelableArrayList(EPISODES_KEY);
            m_Title = savedInstanceState.getString(TITLE_KEY);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(EPISODES_KEY, m_Episodes);
        savedInstanceState.putString(TITLE_KEY, m_Title);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume " + (m_Episodes != null ? m_Episodes.toString() : "null"));
        if (m_Episodes != null) {
            m_GraphView.setEpisodes(m_Episodes, m_Title);
        }
    }
}
