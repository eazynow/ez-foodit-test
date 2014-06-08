package com.foodit.test.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.foodit.test.model.Response.CountStatistic;

public class MapHelper {

	/**
	 * This method will sort a KV map based on the V and limit the number of results
	 * returned if necessary
	 * @param unsortMap is the map to be sorted
	 * @param orderAsc is a boolean. If true then the map is sorted ascending, otherwise descending
	 * @param maxItems is the maximum number of items to return
	 * @return a sorted list of CountStatistic objects
	 */
	public static List<CountStatistic> sortMapIntoOrderedList(Map<String, Integer> unsortMap, final boolean orderAsc, int maxItems)
    {
        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2)
            {
                if (orderAsc)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });
        
        int itemCount = 0;
        List<CountStatistic> sortedList = new LinkedList<CountStatistic>();
        for (Entry<String, Integer> entry : list)
        {
        	if(itemCount >= maxItems) {
        		break;
        	}
        	sortedList.add(new CountStatistic(entry.getKey(), entry.getValue()));
        	itemCount++;
        }
        return sortedList;
    }
}
