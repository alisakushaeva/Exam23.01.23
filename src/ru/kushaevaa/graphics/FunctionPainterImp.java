package ru.kushaevaa.graphics;

import ru.kushaevaa.Converter;
import ru.kushaevaa.math.Function;
import ru.kushaevaa.math.Pair;

import java.awt.*;

import static java.lang.Double.isNaN;

public class FunctionPainterImp implements Painter {
    private Color clr;
    private Converter cnv;
    private boolean check;
    private Function f;
    private double tMin, tMax;

    public FunctionPainterImp(Converter cnv, Function f, Color color, boolean check, double tMin, double tMax){
        this.f = f;
        this.clr = color;
        this.cnv = cnv;
        this.check = check;
        this.tMin = tMin;
        this.tMax = tMax;
    }

    public void paint(Graphics g, int width, int height) {
        /*Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);*/
        double dt = 0.0001;
        if (check) {
            g.setColor(clr);
            for (double t = tMin; t < tMax; t+=dt) {
                Pair a = (Pair)f.invoke(t);
                double x1Crt = a.getX();
                double y1Crt = a.getY();
                Pair b = (Pair)f.invoke(t+dt);
                double x2Crt = b.getX();
                double y2Crt = b.getY();
                if(!isNaN(x1Crt)&&!isNaN(y1Crt)){
                    g.drawLine(cnv.xCrt2Scr(x1Crt), cnv.yCrt2Scr(y1Crt), cnv.xCrt2Scr(x2Crt), cnv.yCrt2Scr(y2Crt));}
            }
        }
    }
    public void setColor(Color clr) {
        this.clr = clr;
    }

    public void setF(Function f) {
        this.f = f;
    }
    public void setTMin(double tMin){
        this.tMin = tMin;
    }
    public void setTMax(double tMax){
        this.tMax = tMax;
    }
}
