package com.s0mbr3.moodtracker.core.controllers;

import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;


import com.s0mbr3.moodtracker.core.models.Humor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * MainController class control what humor to choose depending of an index to a built string list
 * containing names of methods to use
 *
 * Created by Oxhart on 21/01/2019.
 */
public class MainController {
    private List<String> humorsList = new ArrayList<>();
    private Method m;
    private Humor humor;
    private int mIndex;

    public MainController(ConstraintLayout layout, ImageView smiley) {
        this.humor = new Humor(layout, smiley);

        this.humorsList.add("getSadSmiley");
        this.humorsList.add("getDisappointedSmiley");
        this.humorsList.add("getNormalSmiley");
        this.humorsList.add("getHappySmiley");
        this.humorsList.add("getSuperHappySmiley");
    }


    /**
     * getMethodName method invoke by reflection the according method stored in the humorsList String
     * List depending of the index determined in the MyGestureListener class
     *
     * @see MyGestureListener
     * @param index
     */
    public void getMethodName(int index) {
        Class c1;
        try {
            c1 = Class.forName(Humor.class.getName());
            this.m = c1.getMethod(this.humorsList.get(index), (Class[]) null);
            this.m.invoke(this.humor, (Object[]) null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
