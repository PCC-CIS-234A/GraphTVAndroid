package edu.pcc.marc.graphtv.presentation.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.pcc.marc.graphtv.R;
import edu.pcc.marc.graphtv.logic.Show;
import edu.pcc.marc.graphtv.logic.ShowListener;
import edu.pcc.marc.graphtv.logic.TitleSearcher;
import edu.pcc.marc.graphtv.main.MainActivity;
import edu.pcc.marc.graphtv.presentation.adapters.ShowTitleListViewAdapter;

public class TitleSearchFragment extends Fragment implements ShowListener, TextWatcher {
    public static final String TAG = "TitleSearchFragment";
    private ArrayList<Show> m_ShowList;
    private TitleSearcher m_Searcher;
    private ShowTitleListViewAdapter m_Adapter;
    private ListView m_ListView;
    private TitleSearchFragment m_This = this;
    private MainActivity m_MainActivity;
    private EditText m_TitleSearchBox;

    public TitleSearchFragment() {
        // Required empty public constructor
        m_This = this;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        m_MainActivity = (MainActivity) context;
        m_MainActivity.setSelectedShow(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_title_search, container, false);

        rootView.findViewById(R.id.show_header_type).setVisibility(TextView.GONE);
        rootView.findViewById(R.id.show_header_series_title).setVisibility(TextView.GONE);

        m_ListView = rootView.findViewById(R.id.listview);
        m_ListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        m_TitleSearchBox = rootView.findViewById(R.id.title_search_box);
        m_TitleSearchBox.addTextChangedListener(this);

        m_Searcher = new TitleSearcher(getContext());
        m_Searcher.addShowListener(this);

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

    private void populateList() {
        m_Searcher.search("kamisama");
    }

    @Override
    public void showsArrived(final ArrayList<Show> shows) {
        Activity activity = getActivity();
        if (activity == null)
            return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_ShowList = shows;
                m_Adapter = new ShowTitleListViewAdapter(m_This, m_ShowList, false,false, true);
                m_ListView.setAdapter(m_Adapter);
                m_Adapter.notifyDataSetChanged();
                m_MainActivity.setSelectedShow(null);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        update(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    private void update(CharSequence text) {
        m_Searcher.search(text.toString());
    }
}
