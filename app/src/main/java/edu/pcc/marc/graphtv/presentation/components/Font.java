package edu.pcc.marc.graphtv.presentation.components;

import android.graphics.Typeface;

public class Font {
    private Typeface m_Typeface;
    private int m_Style;
    private float m_Size;

    public static final int PLAIN = Typeface.NORMAL;
    public static final int BOLD = Typeface.BOLD;
    public static final int ITALIC = Typeface.ITALIC;
    public static final int BOLD_ITALIC = Typeface.BOLD_ITALIC;

    public Font(String font, int style, float size) {
        if (font.equals("TimesRoman"))
            m_Typeface = Typeface.create("serif", style);
        else
            m_Typeface = Typeface.create("sans-serif", style);
        m_Size = size;
    }

    public Typeface getTypeface() {
        return m_Typeface;
    }

    public int getStyle() {
        return m_Style;
    }

    public float getSize() {
        return m_Size;
    }
}
