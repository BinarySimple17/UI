package ru.binarysimple.ui;

public class Period {
    String comp_id;
    String month;
    String monthIndex;
    String year;
    String yearIndex;

    public Period(String comp_id, String month, String monthIndex, String year, String yearIndex) {
        this.comp_id = comp_id;
        this.month = month;
        this.monthIndex = monthIndex;
        this.year = year;
        this.yearIndex = yearIndex;
    }
}
