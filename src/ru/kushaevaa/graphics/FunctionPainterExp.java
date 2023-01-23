package ru.kushaevaa.graphics;

import ru.kushaevaa.Converter;
import ru.kushaevaa.math.Function;

import java.awt.*;


public class FunctionPainterExp implements Painter {
    private Color clr;
    private Converter cnv;
    private boolean check;
    private Function f;

    public FunctionPainterExp(Converter cnv, Function f, Color color, boolean check){
        this.f = f;
        this.clr = color;
        this.cnv = cnv;
        this.check = check;
    }

    public void paint(Graphics g, int width, int height) {
        if (check) {
            g.setColor(clr);
            for (int xScr = 0; xScr < width - 1; xScr++) {
                double xCrt1 = cnv.xScr2Crt(xScr);
                double yCrt1 = (double) f.invoke(xCrt1);
                double xCrt2 = cnv.xScr2Crt(xScr + 1);
                double yCrt2 = (double) f.invoke(xCrt2);
                g.drawLine(cnv.xCrt2Scr(xCrt1), cnv.yCrt2Scr(yCrt1), cnv.xCrt2Scr(xCrt2), cnv.yCrt2Scr(yCrt2));
            }
        }
    }
    public void setColor(Color clr) {
        this.clr = clr;
    }

    public void setF(Function f) {
        this.f = f;
    }
}
