package edu.pcc.marc.graphtv.presentation.components;

import android.graphics.Paint;
import android.graphics.Rect;

public class FontMetrics {
    private Paint m_Paint;

    public FontMetrics(Paint paint) {
        m_Paint = paint;
    }

    public int stringWidth(String string) {
        Rect r = new Rect();

        m_Paint.getTextBounds(string, 0, string.length(), r);
        return Math.round(r.right - r.left);
    }
}
