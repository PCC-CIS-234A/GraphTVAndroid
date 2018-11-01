package edu.pcc.marc.graphtv.logic;

import java.util.ArrayList;

import edu.pcc.marc.graphtv.data.WebData;

public class ShowType {
    private String m_TitleType;
    private String m_Pretty;

    public ShowType(String titleType, String pretty) {
        this.m_TitleType = titleType;
        this.m_Pretty = pretty;
    }

    public static ArrayList<ShowType> fetchShowTypes() {
        return WebData.fetchShowTypes();
    }

    public String getTitleType() {
        return m_TitleType;
    }

    public String getPretty() {
        return m_Pretty;
    }

    public String toString() {
        return m_Pretty;
    }
}
