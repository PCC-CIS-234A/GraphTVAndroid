package edu.pcc.marc.graphtv.presentation.adapters;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import edu.pcc.marc.graphtv.R;
import edu.pcc.marc.graphtv.logic.Show;

/**
 * 
 * @author anfer
 * 
 */
public class ShowTitleListViewAdapter extends BaseAdapter {
	private ArrayList<Show> m_ShowList;
	private Fragment m_Fragment;
	private boolean m_ShowParentTitle = true;
	private boolean m_ShowEpisodes = true;
	private boolean m_ShowType = true;

	public ShowTitleListViewAdapter(Fragment fragment, ArrayList<Show> showList, boolean showType, boolean showParentTitle, boolean showEpisodes) {
		super();
		m_Fragment = fragment;
		m_ShowList = showList;
		m_ShowType = showType;
		m_ShowParentTitle = showParentTitle;
		m_ShowEpisodes = showEpisodes;
	}

	@Override
	public int getCount() {
		return m_ShowList.size();
	}

	@Override
	public Object getItem(int position) {
		return m_ShowList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		TextView m_Title;
		TextView m_Type;
		TextView m_SeriesTitle;
		TextView m_StartYear;
		TextView m_RuntimeMinutes;
		TextView m_Rating;
		TextView m_Votes;
		TextView m_Genres;
		TextView m_NumEpisodes;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		LayoutInflater inflater = m_Fragment.getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.show_title_row, null);
			holder = new ViewHolder();
			holder.m_Title = (TextView) convertView.findViewById(R.id.title);
			holder.m_Type = (TextView) convertView.findViewById(R.id.title_type);
			holder.m_SeriesTitle = (TextView) convertView.findViewById(R.id.series_title);
			holder.m_StartYear = (TextView) convertView.findViewById(R.id.start_year);
			holder.m_RuntimeMinutes = (TextView) convertView.findViewById(R.id.runtime_minutes);
			holder.m_Rating = (TextView) convertView.findViewById(R.id.rating);
			holder.m_Votes = (TextView) convertView.findViewById(R.id.votes);
			holder.m_Genres = (TextView) convertView.findViewById(R.id.genres);
			holder.m_NumEpisodes = (TextView) convertView.findViewById(R.id.episodes);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Show item = m_ShowList.get(position);
		holder.m_Title.setText(item.getTitle());
		holder.m_Type.setText(item.getType());
		holder.m_SeriesTitle.setText(item.getParentTitle());
		holder.m_StartYear.setText(emptyForZero(item.getStartYear()));
		holder.m_RuntimeMinutes.setText(emptyForZero(item.getRuntimeMinutes()));
		holder.m_Rating.setText(emptyForZeroFloat(item.getRating()));
		holder.m_Votes.setText(emptyForZero(item.getVotes()));
		holder.m_Genres.setText(item.getGenres());
		holder.m_NumEpisodes.setText(emptyForZero(item.getNumEpisodes()));

        if (!m_ShowType) {
            holder.m_Type.setVisibility(TextView.GONE);
        }
        if (!m_ShowParentTitle) {
            holder.m_SeriesTitle.setVisibility(TextView.GONE);
        }
        if (!m_ShowEpisodes) {
		    holder.m_NumEpisodes.setVisibility(TextView.GONE);
        }

		return convertView;
	}

    private String emptyForZero(int val) {
        return (val != 0) ? "" + val : "";
    }
    private String emptyForZeroFloat(float val) {
        return (val != 0) ? "" + val : "";
    }
}