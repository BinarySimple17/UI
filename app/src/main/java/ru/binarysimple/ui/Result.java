package ru.binarysimple.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;

/**
 * Created by voffka on 23.02.2016. used Builder pattern
 */
public class Result {
 //   private final Context context;
    private final Integer _id;
    private final Integer id_person;
    private final Integer month;
    private final Integer year;
    private final String ndfl;
    private final String ffoms;
    private final String pfr;
    private final String fss;

    Result(final ResultBuilder resultBuilder) {
//        this.context = resultBuilder.getContext();
        this._id = resultBuilder.get_id();
        this.id_person = resultBuilder.getId_person();
        this.month = resultBuilder.getMonth();
        this.year = resultBuilder.getYear();
        this.ndfl = resultBuilder.getNdfl();
        this.ffoms = resultBuilder.getFfoms();
        this.pfr = resultBuilder.getPfr();
        this.fss = resultBuilder.getFss();
    }

/*    public Context getContext() {
        return context;
    }*/

    public Integer get_id(){
        return _id;
    }

    public Integer getId_person(){
        return id_person;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getYear() {
        return year;
    }

    public String getNdfl() {
        return ndfl;
    }

    public String getFfoms() {
        return ffoms;
    }

    public String getPfr() {
        return pfr;
    }

    public String getFss() {
        return fss;
    }

    public static class ResultBuilder{
  //      private  Context context;
        private  Integer _id;
        private  Integer id_person;
        private  Integer month;
        private  Integer year;
        private  String ndfl;
        private  String ffoms;
        private  String pfr;
        private  String fss;

/*        public ResultBuilder setContext(Context context) {
            this.context = context;
            return this;
        }*/

        public ResultBuilder set_id(Integer _id) {
            this._id = _id;
            return this;
        }

        public ResultBuilder setId_person(Integer id_person) {
            this.id_person = id_person;
            return this;
        }

        public ResultBuilder setMonth(Integer month) {
            this.month = month;
            return this;
        }

        public ResultBuilder setYear(Integer year) {
            this.year = year;
            return this;
        }

        public ResultBuilder setNdfl(String ndfl) {
            this.ndfl = ndfl;
            return this;
        }

        public ResultBuilder setFfoms(String ffoms) {
            this.ffoms = ffoms;
            return this;
        }

        public ResultBuilder setPfr(String pfr) {
            this.pfr = pfr;
            return this;
        }

        public ResultBuilder setFss(String fss) {
            this.fss = fss;
            return this;
        }

/*        public Context getContext() {
            return context;
        }*/

        public Integer get_id() {
            return _id;
        }

        public Integer getId_person() {
            return id_person;
        }

        public Integer getMonth() {
            return month;
        }

        public Integer getYear() {
            return year;
        }

        public String getNdfl() {
            return ndfl;
        }

        public String getFfoms() {
            return ffoms;
        }

        public String getPfr() {
            return pfr;
        }

        public String getFss() {
            return fss;
        }

        public Result build(){
            return new Result(this);
        }
    }

    /*    public void load (Integer pers_id){
        WorkDB workDB = new WorkDB();
        Cursor c = workDB.getData(context, "select * from " + Main.TABLE_RESULTS + " where id_person = ?",
                new String[]{pers_id.toString()});
        if (c == null) return;
        month = c.getInt(c.getColumnIndex("month"));
        year = c.getInt(c.getColumnIndex("year"));
        ndfl = c.getFloat(c.getColumnIndex("ndfl"));
        ffoms = c.getFloat(c.getColumnIndex("ffoms"));
        pfr = c.getFloat(c.getColumnIndex("pfr"));
        fss = c.getFloat(c.getColumnIndex("fss"));

    }*/

/*    public void remove (String id){
        WorkDB workDB = new WorkDB();
        workDB.delData(context,Main.TABLE_RESULTS,id);
    }*/
}
