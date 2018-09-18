package org.testah.framework.report.asserts;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testah.TS;
import org.testah.framework.report.VerboseAsserts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class AssertCollectionsTest {

    VerboseAsserts noThrow = new VerboseAsserts().setThrowExceptionOnFail(false);

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void hasSize() {
        List<String> lst = new ArrayList<String>();
        new AssertCollections(lst).size().equalsTo(0);

        lst.add("item 1");
        new AssertCollections(lst).size().equalsTo(1);
    }

    @Test
    void hasSizeArrayString() {
        String[] array = new String[]{};
        new AssertCollections(array).size().equalsTo(0);

        array = new String[]{"item 1"};
        new AssertCollections(array).size().equalsTo(1);
    }

    @Test
    void containsStringList() {
        List<String> lst = new ArrayList<String>();
        try {
            new AssertCollections(lst).contains(null);
            Assert.assertTrue("If the assert above worked would not make it here", false);
        } catch (AssertionError assertFail) {
            TS.log().info("Error thrown as expected", assertFail);
        }
        lst.add(null);
        new AssertCollections(lst, noThrow).contains(null);
        lst.add("item 1");
        new AssertCollections(lst, noThrow).contains("item 1");
    }

    @Test
    void containsStringSet() {
        HashSet<String> set = new HashSet<String>();
        try {
            new AssertCollections(set).contains(null);
            Assert.assertTrue("If the assert above worked would not make it here", false);
        } catch (AssertionError assertFail) {
            TS.log().info("Error thrown as expected", assertFail);
        }
        set.add(null);
        new AssertCollections(set, noThrow).contains(null);
        set.add("item 1");
        new AssertCollections(set, noThrow).contains("item 1");
    }

    @Test
    void containsIntegerList() {
        List<Integer> lst = new ArrayList<Integer>();
        try {
            new AssertCollections(lst).contains(null);
            Assert.assertTrue("If the assert above worked would not make it here", false);
        } catch (AssertionError assertFail) {
            TS.log().info("Error thrown as expected", assertFail);
        }
        lst.add(null);
        new AssertCollections(lst, noThrow).contains(null);
        lst.add(2);
        new AssertCollections(lst, noThrow).contains(2);
    }

    @Test
    void containsIntegerSet() {
        Set<Integer> set = new HashSet<Integer>();
        try {
            new AssertCollections(set).contains(null);
            Assert.assertTrue("If the assert above worked would not make it here", false);
        } catch (AssertionError assertFail) {
            TS.log().info("Error thrown as expected", assertFail);
        }
        set.add(null);
        new AssertCollections(set, noThrow).contains(null);
        set.add(2);
        new AssertCollections(set, noThrow).contains(2);
    }

    @Test
    void doesNotContainStringList() {
        List<String> lst = new ArrayList<String>();
        new AssertCollections(lst).doesNotContain(null);
        try {
            lst.add(null);
            new AssertCollections(lst).doesNotContain(null);
            Assert.assertTrue("If the assert above worked would not make it here", false);
        } catch (AssertionError assertFail) {
            TS.log().info("Error thrown as expected", assertFail);
        }

        lst.add("");
        new AssertCollections(lst).doesNotContain(" ");
    }

    @Test
    void doesNotContainIntegerSet() {
        Set<Integer> set = new HashSet<Integer>();
        new AssertCollections(set).doesNotContain(null);
        try {
            set.add(null);
            new AssertCollections(set).doesNotContain(null);
            Assert.assertTrue("If the assert above worked would not make it here", false);
        } catch (AssertionError assertFail) {
            TS.log().info("Error thrown as expected", assertFail);
        }

        set.add(3);
        new AssertCollections(set).doesNotContain(2);
    }

    @Test
    void noDuplicates() {
        List<String> lst1 = new ArrayList<String>();
        lst1.add("item 1");
        lst1.add("item 2");
        lst1.add("item 3");
        new AssertCollections(lst1).noDuplicates();

        try {
            List<String> lstWithDups = new ArrayList<String>();
            lstWithDups.add("item 1");
            lstWithDups.add("item 2");
            lstWithDups.add("item 1");
            new AssertCollections(lstWithDups).noDuplicates();
            Assert.assertTrue("If the assert above worked would not make it here", false);
        } catch (AssertionError assertFail) {
            TS.log().info("Error thrown as expected", assertFail);
        }
    }

    @Test
    void hasDuplicates() {
        List<String> lst1 = new ArrayList<String>();
        lst1.add("item 1");
        lst1.add("item 2");
        lst1.add("item 1");
        new AssertCollections(lst1).hasDuplicates();

        try {
            List<String> lstWithDups = new ArrayList<String>();
            lstWithDups.add("item 1");
            lstWithDups.add("item 2");
            lstWithDups.add("item 3");
            new AssertCollections(lstWithDups).hasDuplicates();
            Assert.assertTrue("If the assert above worked would not make it here", false);
        } catch (AssertionError assertFail) {
            TS.log().info("Error thrown as expected", assertFail);
        }
    }


    @Test
    void equalsStringList() {
        List<String> lst1 = new ArrayList<String>();
        lst1.add("item 1");
        lst1.add("item 2");
        lst1.add("item 3");

        List<String> lst2 = new ArrayList<String>();
        lst2.add("item 1");
        lst2.add("item 2");
        lst2.add("item 3");
        new AssertCollections(lst1).equalsTo(lst2);
    }


    @Test
    void equalsStringListNegative() {
        try {
            List<String> lst1 = new ArrayList<String>();
            lst1.add("item 1");
            lst1.add("item 2");
            lst1.add("item 3");

            List<String> lst2 = new ArrayList<String>();
            lst2.add("item 1");
            lst2.add("item 3");
            lst2.add("item 2");
            new AssertCollections(lst1).equalsTo(lst2);
            Assert.assertTrue("If the assert above worked would not make it here", false);
        } catch (AssertionError assertFail) {
            TS.log().info("Error thrown as expected", assertFail);
        }


    }

    @Test
    void equalsIgnoreOrder() {
        List<String> lst1 = new ArrayList<String>();
        lst1.add("item 1");
        lst1.add("item 2");
        lst1.add("item 3");

        List<String> lst2 = new ArrayList<String>();
        lst2.add("item 1");
        lst2.add("item 3");
        lst2.add("item 2");
        new AssertCollections(lst1).equalsToIgnoreOrder(lst2);
    }

    @Test
    void testIsPassed() {
        List<String> lst = new ArrayList<String>();

        Assert.assertTrue(new AssertCollections(lst).isEmpty().isPassed());
        Assert.assertFalse(new AssertCollections(lst).isEmpty().isFailed());

        Assert.assertFalse(new AssertCollections(lst, noThrow).isNotEmpty().isPassed());
        Assert.assertTrue(new AssertCollections(lst, noThrow).isNotEmpty().isFailed());
    }

}