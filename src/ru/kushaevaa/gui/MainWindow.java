package ru.kushaevaa.gui;

import ru.kushaevaa.Converter;
import ru.kushaevaa.graphics.*;
import ru.kushaevaa.graphics.Painter;
import ru.kushaevaa.math.Function;
import ru.kushaevaa.math.FunctionExplicit;
import ru.kushaevaa.math.FunctionImplicit;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

public class MainWindow extends JFrame {

    private Dimension minSize = new Dimension(600, 450);
    private GraphicsPanel mainPanel;
    private JPanel controlPanel;
    private GroupLayout gl;
    private GroupLayout glcp;
    private JLabel xmin, ymin, xmax, ymax;
    private JSpinner xmins, ymins, xmaxs, ymaxs;
    private SpinnerNumberModel nmxmins, nmymins, nmxmaxs, nmymaxs;
    private JPanel clr1, clr2,clr3;
    private JLabel clr1n, clr2n, clr3n;
    private JCheckBox ch1, ch2, ch3;
    private Graphics g;
    public MainWindow(){
        setSize(minSize);
        setMinimumSize(minSize);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        gl = new GroupLayout(getContentPane());
        controlPanel = new JPanel();
        glcp = new GroupLayout(controlPanel);
        controlPanel.setLayout(glcp);
        setLayout(gl);
        controlPanel.setBackground(Color.white);

        //панельки
        clr1 = new JPanel();
        clr2 = new JPanel();
        clr3 = new JPanel();
        clr1.setBackground(Color.BLACK);
        clr2.setBackground(Color.BLUE);
        clr3.setBackground(Color.orange);

        ch1 = new JCheckBox("", true);
        ch2 = new JCheckBox("", true);
        ch3 = new JCheckBox("", true);

        clr1n = new JLabel("Явное задание");
        clr2n = new JLabel("Неявное задание");
        clr3n = new JLabel("Цвет производной");


        xmin = new JLabel();
        ymin = new JLabel();
        xmax = new JLabel();
        ymax = new JLabel();
        xmin.setText("xmin");
        ymin.setText("ymin");
        xmax.setText("xmax");
        ymax.setText("ymax");


        //спиннеры
        nmxmins = new SpinnerNumberModel(-5., -100., 4.9, 0.1);
        nmxmaxs = new SpinnerNumberModel(5., -4.9, 100., 0.1);
        nmymins = new SpinnerNumberModel(-5., -100., 4.9, 0.1);
        nmymaxs = new SpinnerNumberModel(5., -4.9, 100., 0.1);
        xmins = new JSpinner(nmxmins);
        ymins = new JSpinner(nmymins);
        xmaxs = new JSpinner(nmxmaxs);
        ymaxs = new JSpinner(nmymaxs);

        Converter cnv = new Converter((Double) xmins.getValue(), (Double)xmaxs.getValue(), (Double)ymins.getValue(), (Double)ymaxs.getValue(),
                0,0);
        //события для спиннеров
        xmins.addChangeListener(e -> {
            nmxmaxs.setMinimum((Double)nmxmins.getNumber()+2*(Double)nmxmaxs.getStepSize());
            cnv.setXEdges((Double)nmxmins.getValue(), (Double)nmxmaxs.getValue());
            mainPanel.repaint();
        });
        xmaxs.addChangeListener(e->{
            nmxmins.setMaximum((Double)nmxmaxs.getNumber()-2*(Double)nmxmins.getStepSize());
            cnv.setXEdges((Double)nmxmins.getValue(), (Double)nmxmaxs.getValue());
            mainPanel.repaint();
        });
        ymins.addChangeListener(e -> {
            nmymaxs.setMinimum((Double)nmymins.getNumber()+2*(Double)nmymaxs.getStepSize());
            cnv.setYEdges((Double)nmymins.getValue(), (Double)nmymaxs.getValue());
            mainPanel.repaint();
        });
        ymaxs.addChangeListener(e->{
            nmymins.setMaximum((Double)nmymaxs.getNumber()-2*(Double)nmymins.getStepSize());
            cnv.setYEdges((Double)nmymins.getValue(), (Double)nmymaxs.getValue());
            mainPanel.repaint();
        });




        //оси
        var pts = new ArrayList<Painter>();//список всех пэйнтеров
        var crt = new CrtPainter(cnv);
        pts.add(crt);

        mainPanel = new GraphicsPanel(pts);
        mainPanel.setBackground(Color.white);

        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                cnv.setWidth(mainPanel.getWidth());
                cnv.setHeight(mainPanel.getHeight());
                mainPanel.repaint();
            }
        });


        Function f = new FunctionExplicit();
        var fpnts = new FunctionPainterExp(cnv, f, clr1.getBackground(), ch1.isSelected());
        mainPanel.addPainter(fpnts);

        Function g = new FunctionImplicit();
        var gpnts = new FunctionPainterImp(cnv, g, clr2.getBackground(), ch2.isSelected());
        mainPanel.addPainter(gpnts);


        //события изменения цветов
        clr1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                var newColor = JColorChooser.showDialog(
                        MainWindow.this, "Выберите цвет графика функции, заданной явно", clr1.getBackground());
                if(newColor != null){
                    clr1.setBackground(newColor);
                    fpnts.setColor(newColor);
                    mainPanel.repaint();
                }
            }
        });
        clr2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                var newColor = JColorChooser.showDialog(
                        MainWindow.this, "Выберите цвет графика функции, заданной неявно", clr2.getBackground());
                if(newColor != null){
                    clr2.setBackground(newColor);
                    gpnts.setColor(newColor);
                    mainPanel.repaint();
                }
            }
        });
        clr3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                var newColor = JColorChooser.showDialog(
                        MainWindow.this, "Выберите цвет производной", clr3.getBackground());
                if(newColor != null){
                    clr3.setBackground(newColor);
                    mainPanel.repaint();
                }
            }
        });

        //события для чекбоксов
        ch1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(ch1.isSelected()){
                    mainPanel.addPainter(fpnts);
                }
                else mainPanel.removePainter(fpnts);
            }
        });
        ch2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(ch2.isSelected()){
                    mainPanel.addPainter(gpnts);
                }
                else mainPanel.removePainter(gpnts);
            }
        });
        ch3.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(ch3.isSelected()){
                    if(ch1.isSelected()) {
                        //mainPanel.addPainterToTheEnd(pnts);
                        //mainPanel.removePainter(pnts);
                    }
                }
                //else mainPanel.removePainter(dfpnts);
            }
        });


        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addGap(8)
                .addGroup(gl.createParallelGroup()
                        .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addComponent(controlPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                )
                .addGap(8)
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGap(8)
                .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                .addGap(8)
                .addComponent(controlPanel, 70,70,70)
                .addGap(8)
        );



        glcp.setHorizontalGroup(glcp.createSequentialGroup()
                .addGap(8)
                .addGroup(glcp.createParallelGroup()
                        .addGroup(glcp.createSequentialGroup()
                                .addComponent(xmin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                                .addGap(8)
                                .addComponent(xmins, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        )
                        .addGap(8)
                        .addGroup(glcp.createSequentialGroup()
                                .addComponent(ymin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                                .addGap(8)
                                .addComponent(ymins, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        )
                )
                .addGap(8)
                .addGroup(glcp.createParallelGroup()
                        .addGroup(glcp.createSequentialGroup()
                                .addComponent(xmax, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                                .addGap(8)
                                .addComponent(xmaxs, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        )
                        .addGap(8)
                        .addGroup(glcp.createSequentialGroup()
                                .addComponent(ymax, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                                .addGap(8)
                                .addComponent(ymaxs, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        )
                )
                .addGap(8)
                .addGroup(glcp.createParallelGroup()
                        .addGroup(glcp.createSequentialGroup()
                                .addComponent(ch1)
                                .addGap(8)
                                .addComponent(clr1, 45,45,45)
                                .addGap(8)
                                .addComponent(clr1n, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        )
                        .addGap(8)
                        .addGroup(glcp.createSequentialGroup()
                                .addComponent(ch2)
                                .addGap(8)
                                .addComponent(clr2, 45,45,45)
                                .addGap(8)
                                .addComponent(clr2n, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        )
                        .addGap(8)
                        .addGroup(glcp.createSequentialGroup()
                                .addComponent(ch3)
                                .addGap(8)
                                .addComponent(clr3, 45,45,45)
                                .addGap(8)
                                .addComponent(clr3n, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        ))
                .addGap(8)
        );

        glcp.setVerticalGroup(glcp.createSequentialGroup()
                .addGap(8)
                .addGroup(glcp.createParallelGroup()
                        .addGroup(glcp.createSequentialGroup()
                                .addGroup(glcp.createParallelGroup()
                                        .addComponent(xmin, GroupLayout.Alignment.CENTER)
                                        .addComponent(xmins, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                                )
                                .addGroup(glcp.createParallelGroup()
                                        .addComponent(ymin, GroupLayout.Alignment.CENTER)
                                        .addComponent(ymins, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                                ))
                        .addGap(8)
                        .addGroup(glcp.createSequentialGroup()
                                .addGroup(glcp.createParallelGroup()
                                        .addComponent(xmax, GroupLayout.Alignment.CENTER)
                                        .addComponent(xmaxs, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                                )
                                .addGroup(glcp.createParallelGroup()
                                        .addComponent(ymax, GroupLayout.Alignment.CENTER)
                                        .addComponent(ymaxs, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                                ))
                        .addGap(8)
                        .addGroup(glcp.createSequentialGroup()
                                .addGroup(glcp.createParallelGroup()
                                        .addComponent(ch1)
                                        .addComponent(clr1, 20,20,20)
                                        .addComponent(clr1n, GroupLayout.Alignment.CENTER)
                                )
                                .addGroup(glcp.createParallelGroup()
                                        .addComponent(ch2)
                                        .addComponent(clr2, 20,20,20)
                                        .addComponent(clr2n, GroupLayout.Alignment.CENTER)
                                )
                                .addGroup(glcp.createParallelGroup()
                                        .addComponent(ch3)
                                        .addComponent(clr3, 20,20,20)
                                        .addComponent(clr3n, GroupLayout.Alignment.CENTER)
                                ))
                )
                .addGap(8)
        );
    }
}