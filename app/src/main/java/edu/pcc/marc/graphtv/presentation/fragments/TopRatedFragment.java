package edu.pcc.marc.graphtv.presentation.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import edu.pcc.marc.graphtv.R;
import edu.pcc.marc.graphtv.logic.Genre;
import edu.pcc.marc.graphtv.logic.Show;
import edu.pcc.marc.graphtv.logic.ShowListener;
import edu.pcc.marc.graphtv.logic.ShowType;
import edu.pcc.marc.graphtv.logic.TitleSearcher;
import edu.pcc.marc.graphtv.logic.TopRatedSearcher;
import edu.pcc.marc.graphtv.main.MainActivity;
import edu.pcc.marc.graphtv.presentation.adapters.ShowTitleListViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TopRatedFragment extends Fragment implements ShowListener, TextWatcher, AdapterView.OnItemSelectedListener {
    public static final String TAG = "TopRatedFragment";
    private Spinner m_TypeSpinner;
    private Spinner m_GenreSpinner;
    private EditText m_NumVotes;
    private MainActivity m_MainActivity;
    private static final String ALL_TYPES = "-- All Types --";
    private static final String ALL_GENRES = "-- All Genres --";
    private static ArrayList<ShowType> m_ShowTypes = null;
    private static ArrayList<Genre> m_Genres = null;
    private TopRatedSearcher m_Searcher = null;
    private TopRatedFragment m_This = this;
    private ArrayList<Show> m_ShowList;
    private ShowTitleListViewAdapter m_Adapter;
    private ListView m_ListView;

    public TopRatedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_top_rated, container, false);
        m_TypeSpinner = rootView.findViewById(R.id.typeSpinner);
        m_GenreSpinner = rootView.findViewById(R.id.genreSpinner);
        m_NumVotes = rootView.findViewById(R.id.num_votes);


        m_ListView = rootView.findViewById(R.id.listview);

        m_TypeSpinner.setOnItemSelectedListener(this);
        m_GenreSpinner.setOnItemSelectedListener(this);
        m_NumVotes.addTextChangedListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                populateTypeSpinner();
                populateGenreSpinner();
            }
        }).start();

        m_ListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        m_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // m_MainActivity.showEpisodeRatings(m_ShowList.get(position));
                m_ListView.setItemChecked(position, true);
                m_MainActivity.setSelectedShow(m_ShowList.get(position));
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        m_MainActivity = (MainActivity) context;
        m_MainActivity.setSelectedShow(null);
        m_Searcher = new TopRatedSearcher(context);
        m_Searcher.addShowListener(this);
    }

    private void populateTypeSpinner() {
        if (m_ShowTypes == null) {
            m_ShowTypes = ShowType.fetchShowTypes();
            m_ShowTypes.add(0, new ShowType(ALL_TYPES, ALL_TYPES));
        }
        final ArrayAdapter<ShowType> adapter = new ArrayAdapter<ShowType>(getContext(),
                R.layout.spinner_item, m_ShowTypes);
        m_MainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_TypeSpinner.setAdapter(adapter);
            }
        });
    }

    private void populateGenreSpinner() {
        if (m_Genres == null) {
            m_Genres = Genre.fetchGenres();
            m_Genres.add(0, new Genre(ALL_GENRES));
        }
        final ArrayAdapter<Genre> adapter = new ArrayAdapter<Genre>(getContext(),
                R.layout.spinner_item, m_Genres);
        m_MainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_GenreSpinner.setAdapter(adapter);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        update();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        update();
    }

    @Override
    public void afterTextChanged(Editable s) {
        update();
    }

    private void update() {
        ShowType type = (ShowType) m_TypeSpinner.getSelectedItem();
        if (type == null)
            return;
        String typeString = (type.getTitleType() == ALL_TYPES) ? null : type.getTitleType();

        Genre genre = (Genre) m_GenreSpinner.getSelectedItem();
        if (genre == null)
            return;
        String genreString = (genre.getName() == ALL_GENRES) ? null : genre.getName();

        String numVotesString = m_NumVotes.getText().toString();
        int numVotes = 0;

        try {
            numVotes = Integer.parseInt(numVotesString);
        } catch (Exception e) {
            return;
        }
        if (numVotes < 1000)
            numVotes = 1000;
        if (m_Searcher != null)
            m_Searcher.search(typeString, genreString, numVotes);
    }

    private void setVisible(int id, boolean visibility) {
        m_MainActivity.findViewById(id).setVisibility(visibility ? TextView.VISIBLE : TextView.GONE);
    }

    @Override
    public void showsArrived(final ArrayList<Show> shows) {
        Activity activity = getActivity();
        if(activity == null)
            return;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean hasType = ((ShowType) m_TypeSpinner.getSelectedItem()).getTitleType() == ALL_TYPES;
                boolean hasParent = false;
                boolean hasEpisodes = false;

                m_ShowList = shows;
                for (Show show : shows) {
                    if (show.getParentTitle().length() > 0)
                        hasParent = true;
                    if (show.getNumEpisodes() != 0)
                        hasEpisodes = true;
                }
                setVisible(R.id.show_header_type, hasType);
                setVisible(R.id.show_header_series_title, hasParent);
                setVisible(R.id.show_header_episodes, hasEpisodes);
                m_Adapter = new ShowTitleListViewAdapter(m_This, m_ShowList, hasType, hasParent, hasEpisodes);
                m_ListView.setAdapter(m_Adapter);
                m_Adapter.notifyDataSetChanged();
                m_MainActivity.setSelectedShow(null);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        update();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
