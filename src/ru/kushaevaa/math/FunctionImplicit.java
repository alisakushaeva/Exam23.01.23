package ru.kushaevaa.math;

public class FunctionImplicit implements Function{
    @Override
    public Object invoke(Object t) {
        double T = (double)t;
        Pair a = new Pair((4-T*T)/(1+T*T*T), T*T/(1+T*T*T));
        return a;
    }
}
