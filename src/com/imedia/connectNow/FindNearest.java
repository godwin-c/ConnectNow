package com.imedia.connectNow;

import com.codename1.ui.Display;
import com.codename1.ui.Form;
import userclasses.StateMachine;

public class FindNearest {

    private Form current;

    public void init(Object context) {
    }

    public void start() {
//        if(current != null){
//            current.show();
//            return;
//        }
//        new StateMachine("/theme"); 

        GetMyCoordinate myCoordinate;

        if (current != null) {
            current.show();
            return;
        }
        new StateMachine("/theme");



        myCoordinate = new GetMyCoordinate();
        myCoordinate.run();
    }

    public void stop() {
        current = Display.getInstance().getCurrent();
    }

    public void destroy() {
    }
}
