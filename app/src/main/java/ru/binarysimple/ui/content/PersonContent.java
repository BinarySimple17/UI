package ru.binarysimple.ui.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.binarysimple.ui.Result;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * TODO: Replace all uses of this class before publishing your app.
 */


public class PersonContent {

    /**
     * An array of sample (dummy) items.
     */

    public static final List<Result> ITEMS = new ArrayList<Result>();

    /**
     * A map of sample (dummy) items, by ID.
     */

    public static final Map<String, Result> ITEM_MAP = new HashMap<String, Result>();

    private static final int COUNT = 25;

    /*static*/
    public PersonContent() {
        // Add some sample items.
/*        for (int i = 1; i <= COUNT; i++) {
            addItem(createResultItem(i));}*/
    }

    private void clearITEMS (){
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    public PersonContent(ArrayList<Result> list) {
        // Add some sample items.
        clearITEMS();
        for (int i = 0; i < list.size(); i++) {
            addItem(createResultItem(list.get(i)),i);
        }
    }

 /*   static (ArrayList list){

    }*/

    private static void addItem(Result item, Integer i) {
        ITEMS.add(item);
        ITEM_MAP.put(i.toString(), item);
    }

    private static Result createResultItem(Result result) {
        //TODO create item here
        //return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));

        return new Result.ResultBuilder()
                .set_id(result.get_id())
                .setId_person(result.getId_person())
                .setMonth(result.getMonth())
                .setYear(result.getYear())
                .setNdfl(result.getNdfl())
                .setFfoms(result.getFfoms())
                .setPfr(result.getPfr())
                .setFss(result.getFss())
                .setName(result.getName())
                .setPosition(result.getPosition())
                .setSalary(result.getSalary())
                .setComp_id(result.getComp_id())
                .build();
    }
}
