package org.testah.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {
    /**
     * Given a list, generate all possible sublists of the specified size. The generator maintains
     * the order of the original list, no elements are duplicated. If n is the number of elements in
     * the original list, the number of sublists of length k is n!/((n-k)!*k!). The sublists are build
     * by choosing an element from the element following the current element.
     *
     * @param list        the original list from which to compute the sublists
     * @param subListSize the size of the sublists
     * @param <T>         type of list elements
     * @return list of sublists
     * @throws Exception when trying to create sublists that contain more elements than the original list
     */
    public static <T> List<List<T>> getSublists(List<T> list, int subListSize) throws Exception {
        // sublist cannot be longer than original list
        if (subListSize > list.size()) {
            throw new Exception("A sublist cannot contain more elements than the orginal list.");
        }
        List<List<T>> listOfSubLists = new ArrayList<>();
        getSublists(list, new ArrayList<T>(), subListSize, listOfSubLists);
        return listOfSubLists;
    }

    /**
     * Given a list, generate all possible sublists of the specified size. The generator maintains
     * the order of the original list, no elements are duplicated. If n is the number of elements in
     * the original list, the number of sublists of length k is n!/((n-k)!*k!). The sublists are build
     * by choosing an element from the element following the current element.
     *
     * @param list           the original list from which to compute the sublists
     * @param listOfSubLists list of sublists
     * @param sublist        the current sublist, initially empty.
     * @param subListSize    the size of the sublists
     * @param <T>            type of list elements
     * @return list of sublists
     */
    private static <T> void getSublists(List<T> list, List<T> sublist, int subListSize, List<List<T>> listOfSubLists) {
        // the sublist is complete when the desired size is reached
        if (sublist.size() == subListSize) {
            listOfSubLists.add(sublist);
        } else {
            // from the remaining list elements choose one to add the sublist currently being constructed
            for (int currListElem = 0; currListElem < list.size(); currListElem++) {
                // create a new list containing all the elements computed so far
                List<T> newSublist = new ArrayList<T>();
                newSublist.addAll(sublist);
                // add an element from the remaining elements
                newSublist.add(list.get(currListElem));
                // repeat element selection with the ones following the element just selected
                getSublists(list.subList(currListElem + 1, list.size()), newSublist, subListSize, listOfSubLists);
            }
        }
    }

    /**
     * Generate all sublists of the provided list. The original order is maintained. The number of
     * generated sublists is Sum(k=1..n) n!/((n-k)!*k!).
     *
     * @param list the original list
     * @param <T>  type of list elements
     * @return the list of all sublists
     */
    public static <T> List<List<T>> getAllSublists(List<T> list) {
        List<List<T>> listOfSubLists = new ArrayList<>();
        getAllSublists(list, list.size(), new ArrayList<T>(), listOfSubLists);
        return listOfSubLists;
    }

    private static <T> void getAllSublists(List<T> list, int origSize, List<T> sublist, List<List<T>> listOfSubLists) {
        if (sublist.size() < origSize) {
            for (int ilistElem = 0; ilistElem < list.size(); ilistElem++) {
                // create a new list containing all the elements computed so far
                List<T> newSublist = new ArrayList<T>();
                newSublist.addAll(sublist);
                // add an element from the remaining elements
                newSublist.add(list.get(ilistElem));
                listOfSubLists.add(newSublist);
                // repeat element selection with the ones following the element just selected
                getAllSublists(list.subList(ilistElem + 1, list.size()), origSize, newSublist, listOfSubLists);
            }
        }
    }
}
