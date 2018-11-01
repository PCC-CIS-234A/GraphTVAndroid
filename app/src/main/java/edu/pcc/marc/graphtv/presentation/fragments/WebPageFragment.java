package edu.pcc.marc.graphtv.presentation.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import edu.pcc.marc.graphtv.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebPageFragment extends Fragment {
    public static final String TAG = "WebPageFragment";
    private WebView m_WebView = null;
    private String m_CurrentURL = null;

    public WebPageFragment() {
        // Required empty public constructor
    }

    public void setWebPage(String url) {
        m_CurrentURL = url;
        Log.d(TAG, "Setting url to " + url);
        if (url != null && m_WebView != null) {
            m_WebView.loadUrl(url);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_web_page, container, false);
        m_WebView = rootView.findViewById(R.id.web_view);
        m_WebView.setWebViewClient(new WebViewClient());
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setWebPage(m_CurrentURL);
    }
}
