package ru.kushaevaa.math;

public class FunctionExplicit implements Function{
    @Override
    public Object invoke(Object x) {
        return Math.abs(2*(double)x - Math.pow((double)x,2));
    }
}
