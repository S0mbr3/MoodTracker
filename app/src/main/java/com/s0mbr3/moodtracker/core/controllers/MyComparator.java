package com.s0mbr3.moodtracker.core.controllers;

import java.io.File;
import java.math.BigDecimal;
import java.util.Comparator;

public class MyComparator implements Comparator<File> {

    @Override
    public int compare(File o1, File o2) {
        return new BigDecimal(o1.getName()).compareTo(new BigDecimal(o2.getName()));
    }
}
