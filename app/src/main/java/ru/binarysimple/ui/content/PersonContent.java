package ru.binarysimple.ui.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.binarysimple.ui.Result;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
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

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createResultItem(i));
        }
    }

    private static void addItem(Result item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.get_id().toString(), item);
    }

    private static Result createResultItem(int position) {
        //TODO create item here
        //return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));

        return new Result.ResultBuilder()
                .set_id(position)
                .setId_person(-1)
                .setMonth(-1)
                .setYear(-1)
                .setNdfl("ndfl")
                .setFfoms("ffoms")
                .setPfr("pfr")
                .setFss("fss")
                .build();
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    /*public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }*/
}
