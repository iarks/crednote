package com.iarks.crednote.presentation.fragments.placeholder;

import com.iarks.crednote.models.Good;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    private List<Good> items;
    private Map<String, Good> itemMap;

    public PlaceholderContent(){
        items = new ArrayList<>();
        itemMap = new HashMap<String, Good>();
    }

    private void addItem(Good item) {
        items.add(item);
        itemMap.put(String.valueOf(item.getSerialNumber()), item);
    }

    public List<Good> getItems()
    {
        return items;
    }
}