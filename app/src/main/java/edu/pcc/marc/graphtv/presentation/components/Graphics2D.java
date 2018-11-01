package edu.pcc.marc.graphtv.presentation.components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceHolder;

public class Graphics2D {
    private SurfaceHolder m_SurfaceHolder;
    private Canvas m_Canvas;
    private Paint m_Paint;
    private Font m_Font;

    public Graphics2D(SurfaceHolder holder) {
        m_SurfaceHolder = holder;
        m_Canvas = m_SurfaceHolder.lockCanvas();
        if (m_Canvas != null) {
            m_Paint = new Paint();
            m_Paint.setAntiAlias(true);
        }
    }

    public boolean isValid() {
        return m_Canvas != null;
    }

    public void setColor(int color) {
        m_Paint.setColor(color);
    }

    public void setFont(Font font) {
        m_Paint.setTypeface(font.getTypeface());
        m_Paint.setTextSize(font.getSize());
        m_Font = font;
    }

    public FontMetrics getFontMetrics() {
        // kind of a hack, but it avoids some code changes.
        return new FontMetrics(m_Paint);
    }

    public void setStroke(BasicStroke stroke) {
        m_Paint.setStrokeWidth(stroke.getSize());
    }

    public void fillRect(float x, float y, float width, float height) {
        m_Paint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRect(x, y, x + width, y + height, m_Paint);
    }

    public void fillRoundRect(float x, float y,  float width, float height, float xr, float yr) {
        m_Paint.setStyle(Paint.Style.FILL);
        m_Canvas.drawRoundRect(new RectF(x, y, x + width, y + height), xr, yr, m_Paint);
    }

    public void drawLine(float x1, float y1, float x2, float y2) {
        m_Paint.setStyle(Paint.Style.STROKE);
        m_Canvas.drawLine(x1, y1, x2, y2, m_Paint);
    }

    public void fillArc(float left, float top, float width, float height, float start, float end) {
        m_Paint.setStyle(Paint.Style.FILL);
        m_Canvas.drawArc(new RectF(left, top, left + width, top + height), start, end, false, m_Paint);
    }

    public void drawString(String text, float x, float y) {
        m_Paint.setStyle(Paint.Style.FILL);
        m_Canvas.drawText(text, x, y, m_Paint);
    }

    public void outlineString(String text, float x, float y) {
        m_Paint.setStyle(Paint.Style.STROKE);
        m_Canvas.drawText(text, x, y, m_Paint);
    }

    public void finish() {
        if (m_Canvas != null)
            m_SurfaceHolder.unlockCanvasAndPost(m_Canvas);
    }
}
