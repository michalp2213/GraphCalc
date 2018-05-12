package com.github.michalp2213.GraphCalc.Model.AlgorithmEvents;

import javafx.scene.Node;

public abstract class AlgorithmEvent {
    private Object eventTarget;

    AlgorithmEvent(Object target){
        eventTarget = target;
    }

    public Object getTarget(){
        return eventTarget;
    }
}
