package edu.pcc.marc.graphtv.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import edu.pcc.marc.graphtv.presentation.fragments.AboutFragment;
import edu.pcc.marc.graphtv.presentation.fragments.EpisodeRatingsFragment;
import edu.pcc.marc.graphtv.R;
import edu.pcc.marc.graphtv.presentation.fragments.TitleSearchFragment;
import edu.pcc.marc.graphtv.presentation.fragments.TopRatedFragment;
import edu.pcc.marc.graphtv.presentation.fragments.WebPageFragment;
import edu.pcc.marc.graphtv.logic.Show;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private static EpisodeRatingsFragment m_EpisodeRatingsFragment = new EpisodeRatingsFragment();
    private static TitleSearchFragment m_TitleSearchFragment = new TitleSearchFragment();
    private static WebPageFragment m_WebPageFragment = new WebPageFragment();
    private static AboutFragment m_AboutFragment = new AboutFragment();
    private static TopRatedFragment m_TopRatedFragment = new TopRatedFragment();
    private final MainActivity m_Activity = this;
    private MenuItem m_IMDbMenu;
    private MenuItem m_ChartMenu;
    private Show m_SelectedShow = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            item.setChecked(true);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.addToBackStack(null);
            switch (item.getItemId()) {
                case R.id.navigation_info:
                    transaction.replace(R.id.content_frame, m_AboutFragment);
                    break;
                case R.id.navigation_title_search:
                    transaction.replace(R.id.content_frame, m_TitleSearchFragment);
                    break;
                case R.id.navigation_top_rated:
                    transaction.replace(R.id.content_frame, m_TopRatedFragment);
                    break;
                case R.id.navigation_web:
                    transaction.replace(R.id.content_frame, m_WebPageFragment);
                    m_WebPageFragment.setWebPage("https://www.imdb.com/title/" + m_SelectedShow.getID());
                    break;
                case R.id.navigation_chart:
                    transaction.replace(R.id.content_frame, m_EpisodeRatingsFragment);
                    m_EpisodeRatingsFragment.showEpisodeGraph(m_Activity, m_SelectedShow);
                    break;
            }
            transaction.commit();
            return true;
        }
    };

    public void showWebPage(String url) {
        m_WebPageFragment.setWebPage(url);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.content_frame, m_WebPageFragment);
        transaction.commit();
    }

    public void showEpisodeRatings(Show show) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.content_frame, m_EpisodeRatingsFragment);
        transaction.commit();
        m_EpisodeRatingsFragment.showEpisodeGraph(m_Activity, show);
    }

    public void showAbout() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.content_frame, m_AboutFragment);
        transaction.commit();
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        m_IMDbMenu = navigation.getMenu().findItem(R.id.navigation_web);
        m_IMDbMenu.setEnabled(false);
        m_ChartMenu = navigation.getMenu().findItem(R.id.navigation_chart);
        m_ChartMenu.setEnabled(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        showAbout();
	}

	public void setSelectedShow(Show show) {
        m_SelectedShow = show;
        m_IMDbMenu.setEnabled(show != null);
        if (show != null && show.getNumEpisodes() > 0) {
            m_ChartMenu.setEnabled(true);
        } else {
            m_ChartMenu.setEnabled(false);
        }
    }
}
