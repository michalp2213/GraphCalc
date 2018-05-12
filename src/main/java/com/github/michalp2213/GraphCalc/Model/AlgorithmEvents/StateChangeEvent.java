package com.github.michalp2213.GraphCalc.Model.AlgorithmEvents;

import com.github.michalp2213.GraphCalc.Model.GraphElementState;
public class StateChangeEvent extends TouchEvent {
    private GraphElementState state;

    public StateChangeEvent(Object target, GraphElementState state){
        super(target);
        this.state = state;
    }

    public GraphElementState getState() {
        return state;
    }
}
