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
    private final Long id_person;
    private final Integer month;
    private final Integer year;
    private final String ndfl;
    private final String ffoms;
    private final String pfr;
    private final String fss;
    private final String name;
    private final String position;
    private final String salary;
    private final String comp_id;

    private Result(final ResultBuilder resultBuilder) {
//        this.context = resultBuilder.getContext();
        this._id = resultBuilder.get_id();
        this.id_person = resultBuilder.getId_person();
        this.month = resultBuilder.getMonth();
        this.year = resultBuilder.getYear();
        this.ndfl = resultBuilder.getNdfl();
        this.ffoms = resultBuilder.getFfoms();
        this.pfr = resultBuilder.getPfr();
        this.fss = resultBuilder.getFss();
        this.name = resultBuilder.getName();
        this.position = resultBuilder.getPosition();
        this.salary = resultBuilder.getSalary();
        this.comp_id = resultBuilder.getComp_id();
    }

/*    public Context getContext() {
        return context;
    }*/

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getSalary() {
        return salary;
    }

    public String getComp_id() {
        return comp_id;
    }

    public Integer get_id() {
        return _id;
    }

    public Long getId_person() {
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

    public static class ResultBuilder {
        //      private  Context context;
        private Integer _id;
        private Long id_person;
        private Integer month;
        private Integer year;
        private String ndfl;
        private String ffoms;
        private String pfr;
        private String fss;
        private String name;
        private String position;
        private String salary;
        private String comp_id;

/*        public ResultBuilder setContext(Context context) {
            this.context = context;
            return this;
        }*/

        public ResultBuilder set_id(Integer _id) {
            this._id = _id;
            return this;
        }

        public ResultBuilder setId_person(Long id_person) {
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

        public ResultBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ResultBuilder setPosition(String position) {
            this.position = position;
            return this;
        }

        public ResultBuilder setSalary(String salary) {
            this.salary = salary;
            return this;
        }

        public ResultBuilder setComp_id(String comp_id) {
            this.comp_id = comp_id;
            return this;
        }
/*        public Context getContext() {
            return context;
        }*/

        public Integer get_id() {
            return _id;
        }

        public Long getId_person() {
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

        public String getName() {
            return name;
        }

        public String getPosition() {
            return position;
        }

        public String getSalary() {
            return salary;
        }

        public String getComp_id() {
            return comp_id;
        }

        public Result build() {
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
