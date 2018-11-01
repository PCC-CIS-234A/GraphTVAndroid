package edu.pcc.marc.graphtv.logic;

import java.util.ArrayList;

import edu.pcc.marc.graphtv.data.WebData;

public class Genre {
    private String m_Name;

    public Genre(String name) {
        m_Name = name;
    }

    public static ArrayList<Genre> fetchGenres() {
        return WebData.fetchGenres();
    }

    public String getName() {
        return m_Name;
    }

    public String toString() {
        return m_Name;
    }
}
