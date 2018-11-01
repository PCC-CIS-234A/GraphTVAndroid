package edu.pcc.marc.graphtv.data;

import android.content.Context;
import java.io.*;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import edu.pcc.marc.graphtv.R;
import edu.pcc.marc.graphtv.logic.Show;
import edu.pcc.marc.graphtv.logic.Title;

public class TitleData {
    private static ArrayList<Title> m_Titles;

    public static void init(Context context) {
        m_Titles = loadTitles(context);
    }

    public static ArrayList<Title> loadTitles(Context context) {
        ArrayList<Title> titles = new ArrayList<>();

        try {
            InputStream inFile = context.getResources().openRawResource(R.raw.title_titles_tsv);
            BufferedReader in = new BufferedReader(new InputStreamReader(new GZIPInputStream(inFile)));
            String s;
            do {
                s = in.readLine();
                if (s != null) {
                    String[] parts = s.split("\t");
                    titles.add(new Title(parts[0], parts[1]));
                }
            } while (s != null);
        } catch (FileNotFoundException e) {
            System.err.println("Error: Couldn't open title file!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error: Couldn't extract title file contents!");
            e.printStackTrace();
        }
        return titles;
    }

    public static ArrayList<Show> findShowsByTitle(String title, int maxShows) {
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<Show> results = new ArrayList<>();
        int hits = 0;
        String titleLower = title.toLowerCase();

        for (int i = 0; i < m_Titles.size() && hits < maxShows; i++) {
            if (m_Titles.get(i).getTitle().contains(titleLower)) {
                // System.out.println(m_Titles.get(i).getID() + ": " + m_Titles.get(i).getTitle());
                ids.add(m_Titles.get(i).getID());
                hits++;
            }
        }
        // return Database.findShowsByID(ids);
        return WebData.findShowsByID(ids);
    }
}
