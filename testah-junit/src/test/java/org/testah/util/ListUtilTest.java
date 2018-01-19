package org.testah.util;

import static org.testah.util.ListUtil.getAllSublists;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.testah.TS;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListUtilTest {
    private static final List<String> strings = Arrays.asList(new String[] {"a", "b", "c", "d", "e", "f"});
    private static final String json1 =
        "[[\"a\"],[\"b\"],[\"c\"],[\"d\"],[\"e\"],[\"f\"]]";
    private static final String json2 =
        "[[\"a\",\"b\"],[\"a\",\"c\"],[\"a\",\"d\"],[\"a\",\"e\"],[\"a\",\"f\"],[\"b\",\"c\"],[\"b\",\"d\"],"
            + "[\"b\",\"e\"],[\"b\",\"f\"],[\"c\",\"d\"],[\"c\",\"e\"],[\"c\",\"f\"],[\"d\",\"e\"],[\"d\",\"f\"],[\"e\",\"f\"]]";
    private static final String json3 =
        "[[\"a\",\"b\",\"c\"],[\"a\",\"b\",\"d\"],[\"a\",\"b\",\"e\"],[\"a\",\"b\",\"f\"],[\"a\",\"c\",\"d\"],"
            + "[\"a\",\"c\",\"e\"],[\"a\",\"c\",\"f\"],[\"a\",\"d\",\"e\"],[\"a\",\"d\",\"f\"],[\"a\",\"e\",\"f\"],"
            + "[\"b\",\"c\",\"d\"],[\"b\",\"c\",\"e\"],[\"b\",\"c\",\"f\"],[\"b\",\"d\",\"e\"],[\"b\",\"d\",\"f\"],"
            + "[\"b\",\"e\",\"f\"],[\"c\",\"d\",\"e\"],[\"c\",\"d\",\"f\"],[\"c\",\"e\",\"f\"],[\"d\",\"e\",\"f\"]]";
    private static final String json4 =
        "[[\"a\",\"b\",\"c\",\"d\"],[\"a\",\"b\",\"c\",\"e\"],[\"a\",\"b\",\"c\",\"f\"],[\"a\",\"b\",\"d\",\"e\"],"
            + "[\"a\",\"b\",\"d\",\"f\"],[\"a\",\"b\",\"e\",\"f\"],[\"a\",\"c\",\"d\",\"e\"],[\"a\",\"c\",\"d\",\"f\"],"
            + "[\"a\",\"c\",\"e\",\"f\"],[\"a\",\"d\",\"e\",\"f\"],[\"b\",\"c\",\"d\",\"e\"],[\"b\",\"c\",\"d\",\"f\"],"
            + "[\"b\",\"c\",\"e\",\"f\"],[\"b\",\"d\",\"e\",\"f\"],[\"c\",\"d\",\"e\",\"f\"]]";
    private static final String json5 = "[[\"a\",\"b\",\"c\",\"d\",\"e\"],[\"a\",\"b\",\"c\",\"d\",\"f\"],"
        + "[\"a\",\"b\",\"c\",\"e\",\"f\"],[\"a\",\"b\",\"d\",\"e\",\"f\"],"
        + "[\"a\",\"c\",\"d\",\"e\",\"f\"],[\"b\",\"c\",\"d\",\"e\",\"f\"]]";

    private static final Map<Integer, List<List<String>>> expectedLists = new HashMap<>();

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        try {
            expectedLists.put(1, mapper.readValue(json1, new TypeReference<List<List<String>>>() {
            }));
            expectedLists.put(2, mapper.readValue(json2, new TypeReference<List<List<String>>>() {
            }));
            expectedLists.put(3, mapper.readValue(json3, new TypeReference<List<List<String>>>() {
            }));
            expectedLists.put(4, mapper.readValue(json4, new TypeReference<List<List<String>>>() {
            }));
            expectedLists.put(5, mapper.readValue(json5, new TypeReference<List<List<String>>>() {
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFixedSizeSublists() throws Exception {
        List<List<String>> listOfSubLists;
        for (int lengthOfSublists = 1; lengthOfSublists < strings.size(); lengthOfSublists++) {
            listOfSubLists = ListUtil.getSublists(strings, lengthOfSublists);
            TS.asserts().equalsTo("Number of subsets with " + lengthOfSublists + "elements.",
                expectedLists.get(lengthOfSublists).size(),
                listOfSubLists.size());
            TS.asserts().equalsTo("Check sublists", expectedLists.get(lengthOfSublists), listOfSubLists);
        }
    }

    @Test
    public void testAllSublists() throws Exception {
        List<List<String>> listOfSubLists = getAllSublists(strings);

        TS.asserts().equalsTo("Number of subsets.",
            1 + expectedLists.get(1).size() + expectedLists.get(2).size() + expectedLists.get(3).size()
                + expectedLists.get(4).size() + expectedLists.get(5).size(),
            listOfSubLists.size());

        Map<Integer, List<List<String>>> actualSublists = new HashMap<>();
        actualSublists.put(1, listOfSubLists.stream().filter(list -> list.size() == 1).collect(Collectors.toList()));
        actualSublists.put(2, listOfSubLists.stream().filter(list -> list.size() == 2).collect(Collectors.toList()));
        actualSublists.put(3, listOfSubLists.stream().filter(list -> list.size() == 3).collect(Collectors.toList()));
        actualSublists.put(4, listOfSubLists.stream().filter(list -> list.size() == 4).collect(Collectors.toList()));
        actualSublists.put(5, listOfSubLists.stream().filter(list -> list.size() == 5).collect(Collectors.toList()));

        for (int lengthOfSublists = 1; lengthOfSublists < strings.size(); lengthOfSublists++) {
            TS.asserts().equalsTo("Number of subsets with " + lengthOfSublists + " elements.",
                expectedLists.get(lengthOfSublists).size(),
                actualSublists.get(lengthOfSublists).size());
            TS.asserts().equalsTo("Check sublists", expectedLists.get(lengthOfSublists), actualSublists.get(lengthOfSublists));
        }
    }
}
