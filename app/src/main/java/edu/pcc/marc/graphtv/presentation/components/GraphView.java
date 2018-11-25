package edu.pcc.marc.graphtv.presentation.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import edu.pcc.marc.graphtv.logic.Episode;
import edu.pcc.marc.graphtv.logic.Show;
import edu.pcc.marc.graphtv.main.MainActivity;

public class GraphView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    public static final String TAG = "GraphView";

    private ArrayList<Episode> m_Episodes = null;
    private String m_Title = "";
    private ArrayList<DataPoint> m_Points = null;
    private DataPoint m_SelectedPoint = null;

    private class Sizes {
        float regularStrokeSize;
        float axisStrokeSize;
        float leftMargin;
        float topMargin;
        float rightMargin;
        float bottomMargin;
        float leading;
        float ratingTextHeight;
        float popupTextHeight;
        float popupLineHeight;
        float titleHeight;
        float popupMargin;
        float twicePopupMargin;
        float popupOffset;
        float arrowSize;
        float minInfoWidth;
        float pointRadius;
        float pointSize;
        float innerPointRadius;
        float innerPointSize;
        float popupFontSize;
        float regularFontSize;
        float titleFontSize;

        float graphWidth;
        float graphHeight;
        float graphRight;
        float graphBottom;
        float graphMiddle;
        float gridLineLeft;
        float popupArrowOffset;

        int leftMarginInt;
        int topMarginInt;
        int graphRightInt;
        int graphBottomInt;
        int gridLineLeftInt;
        int popupLineHeightInt;
        int popupMarginInt;

        BasicStroke regularStroke;
        BasicStroke axisStroke;
        BasicStroke regressionStroke;
        BasicStroke regressionBorderStroke;

        Font popupFont;
        Font regularFont;
        Font titleFont;
        Font pointFont;
        Font innerPointFont;

        private void calculateSizes(int panelWidth, int panelHeight) {
            float base = (float) Math.sqrt(panelWidth * panelHeight) / 500.0f;

            regularStrokeSize = 1 * base;
            axisStrokeSize = 2 * base;
            leftMargin = 40 * base;
            topMargin = 25 * base;
            rightMargin = 10 * base;
            bottomMargin = 40 * base;
            leading = 5 * base;
            ratingTextHeight = 10 * base;
            popupTextHeight = 8 * base;
            titleHeight = 20 * base;
            popupMargin = 5 * base;
            popupOffset = 10 * base;
            arrowSize = 8 * base;
            minInfoWidth = 120 * base;
            popupFontSize = 10 * base;
            regularFontSize = 12 * base;
            titleFontSize = 18 * base;
            pointSize = 8 * base;
            pointRadius = pointSize / 2;
            innerPointSize = pointSize * .75f;
            innerPointRadius = innerPointSize / 2;

            graphWidth = Math.round(panelWidth - m_Sizes.leftMargin - m_Sizes.rightMargin);
            graphHeight = Math.round(panelHeight - m_Sizes.topMargin - m_Sizes.bottomMargin);
            graphRight = leftMargin + graphWidth;
            graphBottom = topMargin + graphHeight;
            graphMiddle = graphBottom - graphHeight / 2;
            gridLineLeft = leftMargin - leading;
            popupArrowOffset = popupOffset + arrowSize;
            twicePopupMargin = popupMargin * 2;
            popupLineHeight = popupMargin + popupTextHeight;
            graphRightInt = Math.round(graphRight);
            graphBottomInt = Math.round(graphBottom);
            leftMarginInt = Math.round(leftMargin);
            topMarginInt = Math.round(topMargin);
            gridLineLeftInt = Math.round(gridLineLeft);
            popupLineHeightInt = Math.round(popupLineHeight);
            popupMarginInt = Math.round(popupMargin);

            axisStroke = new BasicStroke(axisStrokeSize);
            regressionStroke = new BasicStroke(2 * base);
            regressionBorderStroke = new BasicStroke(4 * base);
            regularStroke = new BasicStroke(regularStrokeSize);

            popupFont = new Font("TimesRoman", Font.PLAIN, Math.round(popupFontSize));
            regularFont = new Font("TimesRoman", Font.PLAIN, Math.round(regularFontSize));
            titleFont = new Font("TimesRoman", Font.BOLD, Math.round(titleFontSize));
            pointFont = new Font("Helvetica", Font.BOLD, Math.round(1.8f * pointSize));
            innerPointFont = new Font("Helvetica", Font.BOLD, Math.round(1.8f * innerPointSize));
        }
    }

    private Sizes m_Sizes;
    private int m_InfoColor;
    private int m_InfoBorderColor;
    private Rect m_SelectedInfoBox = null;
    private Show m_InitialShow = null;

    public GraphView(Context context) {
        super(context);
        init();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        m_InfoColor = Color.rgb(255, 245, 220);
        m_InfoBorderColor = Color.rgb(200, 120, 0);
        m_Sizes = new Sizes();
        setOnTouchListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        repaint();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        repaint();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }


    public void setEpisodes(Show initial, ArrayList<Episode> episodes, String title) {
        m_InitialShow = initial;
        m_Episodes = episodes;
        m_Title = title;
        repaint();
    }

    private void repaint() {
        SurfaceHolder holder = getHolder();
        Log.d(TAG, "Holder: " + holder);
        if (holder != null) {
            Graphics2D g2 = new Graphics2D(holder);
            Log.d(TAG, "g2: " + g2);
            if (g2.isValid()) {
                paint(g2);
            }
            g2.finish();
        }
    }

    public void paint(Graphics2D g2) {
        if (!g2.isValid()) {
            return;
        }

        int panelWidth = this.getWidth();
        int panelHeight = this.getHeight();

        m_Sizes.calculateSizes(panelWidth, panelHeight);
        /*
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rh.add(new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP));
        g2.setRenderingHints(rh);
        */

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, panelWidth, panelHeight);

        if (m_Episodes == null || m_Episodes.size() == 0) {
            drawNoInfo(g2, panelWidth, panelHeight);
            return;
        }

        Episode firstEp = m_Episodes.get(0);
        float minRating = firstEp.getRating();
        float maxRating = firstEp.getRating();
        int maxSeason = 0;

        for (Episode episode: m_Episodes) {
            float rating = episode.getRating();

            if (rating > maxRating)
                maxRating = rating;
            if (rating > 0 && (minRating == 0 || rating < minRating))
                minRating = rating;
            if (episode.getSeasonNumber() > maxSeason) {
                maxSeason = episode.getSeasonNumber();
            }
        }

        minRating = (float)Math.floor(2 * minRating) / 2;
        maxRating = (float)Math.ceil(2 * maxRating) / 2;

        if (minRating == maxRating) {
            minRating -= 0.5f;
            maxRating += 0.5f;
        }

        g2.setColor(Color.YELLOW);
        g2.setFont(m_Sizes.titleFont);
        int width = g2.getFontMetrics().stringWidth(m_Title);
        g2.drawString(m_Title, (panelWidth - width) / 2, panelHeight - m_Sizes.titleHeight / 2);
        g2.setFont(m_Sizes.regularFont);
        drawAxes(g2, minRating, maxRating);
        plotPoints(minRating, maxRating);
        drawRegressionLines(g2, maxSeason);
        drawPoints(g2, maxSeason);

        drawSelectedInfo(g2, panelWidth, panelHeight);
    }

    private void drawNoInfo(Graphics2D g2, int panelWidth, int panelHeight) {
        g2.setFont(m_Sizes.titleFont);
        g2.setColor(Color.YELLOW);
        g2.setStroke(m_Sizes.axisStroke);

        float w1 = g2.getFontMetrics().stringWidth(m_Title);
        String line1 = "Sorry, no episode info is";
        float w2 = g2.getFontMetrics().stringWidth(line1);
        String line2 = "available for this series.";
        float w3 = g2.getFontMetrics().stringWidth(line2);
        float width = Math.max(w1, Math.max(w2, w3));

        g2.drawLine(
                (int)((panelWidth - width) / 2),
                (int)((panelHeight - 2.5 * m_Sizes.titleHeight) / 2),
                (int)((panelWidth + width) / 2),
                (int)((panelHeight - 2.5 * m_Sizes.titleHeight) / 2)
        );
        centerText(g2, m_Title, panelWidth / 2, panelHeight / 2 - 2 * m_Sizes.titleHeight);
        centerText(g2, line1, panelWidth / 2, panelHeight / 2);
        centerText(g2, line2, panelWidth / 2, panelHeight / 2 + m_Sizes.titleHeight);
    }

    private void drawAxes(Graphics2D g2, float minRating, float maxRating) {
        g2.setStroke(m_Sizes.axisStroke);
        g2.drawLine(m_Sizes.leftMarginInt, Math.round(m_Sizes.topMargin - m_Sizes.leading), m_Sizes.leftMarginInt, m_Sizes.graphBottomInt);
        g2.drawLine(m_Sizes.leftMarginInt, m_Sizes.graphBottomInt, m_Sizes.graphRightInt, m_Sizes.graphBottomInt);
        g2.setStroke(m_Sizes.regularStroke);
        for (float rating = minRating; rating <= maxRating; rating += 0.5f) {
            int y = Math.round(m_Sizes.graphBottom - m_Sizes.graphHeight * (rating - minRating) / (maxRating - minRating));
            String ratingString = "" + rating;
            int ratingWidth = g2.getFontMetrics().stringWidth(ratingString);
            g2.drawString(ratingString, m_Sizes.leftMargin - 2 * m_Sizes.leading - ratingWidth, y + m_Sizes.ratingTextHeight / 2.3f);
            g2.drawLine(m_Sizes.gridLineLeftInt, y, m_Sizes.graphRightInt, y);
        }
    }

    private void drawRegressionLines(Graphics2D g2, int maxSeason) {
        if (m_Points == null)
            return;
        HashMap<Integer, ArrayList<DataPoint>> map = DataPoint.splitBySeason(m_Points);
        for (Integer seasonNumber: map.keySet()) {
            ArrayList<DataPoint> seasonPoints = map.get(seasonNumber);
            if (seasonPoints.size() > 1) {
                Line2D.Float line = DataPoint.linearRegression(seasonPoints);
                g2.setStroke(m_Sizes.regressionBorderStroke);
                g2.setColor(Color.BLACK);
                g2.drawLine((int) line.getX1(), (int) line.getY1(), (int) line.getX2(), (int) line.getY2());
                g2.setStroke(m_Sizes.regressionStroke);
                g2.setColor(ColorChooser.chooseColor(seasonNumber, maxSeason));
                g2.drawLine((int) line.getX1(), (int) line.getY1(), (int) line.getX2(), (int) line.getY2());
            }
        }
        g2.setStroke(m_Sizes.regularStroke);
    }

    private void plotPoints(float minRating, float maxRating) {
        int length = m_Episodes.size();

        if (m_Points == null)
            m_Points = new ArrayList<>();
        else
            m_Points.clear();
        for (int i = 0; i < m_Episodes.size(); i++) {
            Episode episode = m_Episodes.get(i);
            if (episode.getRating() > 0) {
                float x = m_Sizes.leftMargin + m_Sizes.graphWidth * (i + 0.5f) / length;
                float y = m_Sizes.graphBottom - m_Sizes.graphHeight * (episode.getRating() - minRating) / (maxRating - minRating);
                DataPoint p = new DataPoint(Math.round(x), Math.round(y), episode);
                m_Points.add(p);
                // System.out.println(episode.getTitle() + "   " + episode.getSeasonNumber() + "   " + episode.getEpisodeNumber() + "  " + episode.getRating() + " " + episode.getNumVotes());
            } else {
                float x = m_Sizes.leftMargin + m_Sizes.graphWidth * (i + 0.5f) / length;
                float y = m_Sizes.graphMiddle;
                DataPoint p = new DataPoint(Math.round(x), Math.round(y), episode);
                m_Points.add(p);
            }
        }
    }

    private void drawPoints(Graphics2D g2, int maxSeason) {
        for (DataPoint p: m_Points) {
            g2.setColor(Color.BLACK);
            g2.setStroke(m_Sizes.axisStroke);
            if (p.getEpisode().getRating() > 0) {
                int left = Math.round(p.getX() - m_Sizes.pointRadius);
                int top = Math.round(p.getY() - m_Sizes.pointRadius);
                int innerLeft = Math.round(left + m_Sizes.pointRadius - m_Sizes.innerPointRadius);
                int innerTop = Math.round(top + m_Sizes.pointRadius - m_Sizes.innerPointRadius);
                g2.fillArc(left, top, Math.round(m_Sizes.pointSize), Math.round(m_Sizes.pointSize), 0, 360);
                g2.setColor(ColorChooser.chooseColor(p.getEpisode().getSeasonNumber(), maxSeason));
                g2.fillArc(innerLeft, innerTop,
                        Math.round(m_Sizes.innerPointSize), Math.round(m_Sizes.innerPointSize), 0, 360);
            } else {
                int left = Math.round(p.getX() - m_Sizes.pointRadius);
                int top = Math.round(p.getY() + m_Sizes.pointRadius /* - m_Sizes.pointRadius / 4 */);
                int innerLeft = Math.round(left + m_Sizes.pointRadius - m_Sizes.innerPointRadius);
                int innerTop = Math.round(top + m_Sizes.pointRadius - m_Sizes.innerPointRadius);
                int shift = (int)Math.ceil(m_Sizes.pointRadius - m_Sizes.innerPointRadius);

                /*
                g2.translate(left, top);
                FontRenderContext frc = g2.getFontRenderContext();
                GlyphVector gv = m_Sizes.innerPointFont.createGlyphVector(frc, "?");
                Shape outline = gv.getOutline();

                g2.setColor(Color.BLACK);
                g2.draw(outline);
                g2.setColor(ColorChooser.chooseColor(p.getEpisode().getSeasonNumber(), maxSeason));
                g2.fill(outline);
                g2.translate(-left, -top);
                */
                g2.setColor(Color.BLACK);
                g2.outlineString("?", left, top);
                g2.setColor(ColorChooser.chooseColor(p.getEpisode().getSeasonNumber(), maxSeason));
                g2.drawString("?", left, top);
            }
        }
        g2.setColor(Color.BLACK);
    }

    private void drawSelectedInfo(Graphics2D g2, int panelWidth, int panelHeight) {
        if(m_SelectedPoint == null) {
            m_SelectedInfoBox = null;
            return;
        }

        Episode episode = m_SelectedPoint.getEpisode();
        g2.setFont(m_Sizes.popupFont);
        String title = episode.getTitle();
        float titleWidth = Math.max(m_Sizes.minInfoWidth, g2.getFontMetrics().stringWidth(title));
        float width = m_Sizes.twicePopupMargin + titleWidth;
        float height = m_Sizes.twicePopupMargin + 3 * m_Sizes.popupLineHeight;
        float pointX = m_SelectedPoint.getX();
        float pointY = m_SelectedPoint.getY();
        float x, y;

        g2.setColor(Color.BLACK);

        if(pointX < panelWidth / 2) {
            x = pointX + m_Sizes.popupArrowOffset;
        } else {
            x = pointX - m_Sizes.popupOffset - width;
        }

        if(pointY < panelHeight / 3) {
            y = pointY + m_Sizes.popupArrowOffset;
        } else if (pointY > 2 * panelHeight / 3){
            y = pointY - m_Sizes.popupOffset - height;
        } else {
            y = pointY - height / 2;
        }

        float oneLine = y + m_Sizes.popupMargin + m_Sizes.popupLineHeight;
        float twoLines = y + m_Sizes.popupMargin + 2 * m_Sizes.popupLineHeight;
        float threeLines = y + m_Sizes.popupMargin + 3 * m_Sizes.popupLineHeight;
        int xInt = Math.round(x);
        int yInt = Math.round(y);
        int widthInt = Math.round(width);
        int heightInt = Math.round(height);
        int stroke = (int)Math.ceil(m_Sizes.regularStrokeSize);

        m_SelectedInfoBox = new Rect(xInt - 2 * stroke, yInt - 2 * stroke,  xInt + widthInt + 2 * stroke,yInt + heightInt + 2 * stroke);
        g2.setColor(Color.BLACK);
        g2.fillRoundRect(xInt - 2 * stroke, yInt - 2 * stroke,  widthInt + 4 * stroke,heightInt + 4 * stroke, m_Sizes.popupMarginInt, m_Sizes.popupMarginInt);
        g2.setColor(m_InfoBorderColor);
        g2.fillRoundRect(xInt - stroke, yInt - stroke,  widthInt + 2 * stroke,heightInt + 2 * stroke, m_Sizes.popupMarginInt, m_Sizes.popupMarginInt);
        g2.setColor(m_InfoColor);
        g2.fillRoundRect(xInt, yInt,  widthInt, heightInt, m_Sizes.popupMarginInt, m_Sizes.popupMarginInt);
        g2.setColor(Color.BLACK);
        centerText(g2, title, Math.round(x + width / 2), Math.round(y + m_Sizes.popupLineHeight));
        g2.drawLine(Math.round(x + m_Sizes.popupMargin), Math.round(oneLine),
                Math.round(x + m_Sizes.popupMargin + titleWidth), Math.round(oneLine));
        float column1 = x + width / 4;
        float column2 = x + 3 * width / 4;
        centerText(g2, "Season: " + ((episode.getSeasonNumber() > 0) ? episode.getSeasonNumber() : "???"), column1, twoLines);
        centerText(g2, "Episode: " + ((episode.getEpisodeNumber() > 0) ? episode.getEpisodeNumber() : "???"), column1, threeLines);
        centerText(g2, "Rating: " + (episode.getRating() > 0 ? episode.getRating() : "???"), column2, twoLines);
        centerText(g2, "Votes: " + (episode.getNumVotes() > 0 ? episode.getNumVotes() : "<5"), column2, threeLines);
    }

    private void centerText(Graphics2D g2, String text, float x, float y){
        int width = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, x - width / 2, y);
    }

    @Override
    public boolean onTouch(View view, MotionEvent e) {
        if (m_SelectedInfoBox != null
                && e.getX() <= m_SelectedInfoBox.right
                && e.getX() >= m_SelectedInfoBox.left
                && e.getY() <= m_SelectedInfoBox.bottom
                && e.getY() >= m_SelectedInfoBox.top
                ) {
            ((MainActivity) getContext()).showWebPage("http://www.imdb.com/title/" + m_SelectedPoint.getEpisode().getID());
            return true;
        }
        if (m_Points != null && m_Points.size() > 0) {
            DataPoint point = DataPoint.findDataPoint(m_Points, e.getX(), e.getY(), m_Sizes.pointRadius);
            if (point == null) {
                m_SelectedPoint = point;
                ((MainActivity) getContext()).setSelectedShow(m_InitialShow);
            } else if(point != m_SelectedPoint) {
                m_SelectedPoint = point;
                Episode episode = m_SelectedPoint.getEpisode();
                ((MainActivity) getContext()).setSelectedShow(new Show(
                        episode.getID(),
                        episode.getTitle()
                ));
                repaint();
            }
        }
        return true;
    }
}
