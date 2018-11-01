package edu.pcc.marc.graphtv.logic;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import edu.pcc.marc.graphtv.data.WebData;

public class Episode implements Parcelable {
    private String m_ID;
    private int m_SeasonNumber;
    private int m_EpisodeNumber;
    private String m_Title;
    private int m_Year;
    private float m_Rating;
    private int m_NumVotes;

    public Episode(String id, int season, int episode, String title, int year, float rating, int numVotes) {
        m_ID = id;
        m_SeasonNumber = season;
        m_EpisodeNumber = episode;
        m_Title = title;
        m_Year = year;
        m_Rating = rating;
        m_NumVotes = numVotes;
    }

    public static ArrayList<Episode> fetchEpisodes(String id) {
        // return Database.fetchEpisodes(id);
        return WebData.fetchEpisodes(id);
    }

    public String getID() {
        return m_ID;
    }

    public int getSeasonNumber() {
        return m_SeasonNumber;
    }

    public int getEpisodeNumber() {
        return m_EpisodeNumber;
    }

    public String getTitle() {
        return m_Title;
    }

    public int getYear() {
        return m_Year;
    }

    public float getRating() {
        return m_Rating;
    }

    public int getNumVotes() {
        return m_NumVotes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(m_ID);
        parcel.writeInt(m_SeasonNumber);
        parcel.writeInt(m_EpisodeNumber);
        parcel.writeString(m_Title);
        parcel.writeInt(m_Year);
        parcel.writeFloat(m_Rating);
        parcel.writeInt(m_NumVotes);
    }

    public Episode(Parcel parcel) {
        m_ID = parcel.readString();
        m_SeasonNumber = parcel.readInt();
        m_EpisodeNumber = parcel.readInt();
        m_Title = parcel.readString();
        m_Year = parcel.readInt();
        m_Rating = parcel.readFloat();
        m_NumVotes = parcel.readInt();
    }
}
