package edu.pcc.marc.graphtv.presentation.components;

public class Line2D {
    static class Float {
        float m_X1;
        float m_Y1;
        float m_X2;
        float m_Y2;

        public Float(float x1, float y1, float x2, float y2) {
            m_X1 = x1;
            m_Y1 = y1;
            m_X2 = x2;
            m_Y2 = y2;
        }

        public float getX1() {
            return m_X1;
        }

        public float getY1() {
            return m_Y1;
        }

        public float getX2() {
            return m_X2;
        }

        public float getY2() {
            return m_Y2;
        }
    }
}
