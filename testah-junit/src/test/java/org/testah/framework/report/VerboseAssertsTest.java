package org.testah.framework.report;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class VerboseAssertsTest {

    private VerboseAsserts va;

    @Before
    public void setUp() throws Exception {
        va = new VerboseAsserts().onlyVerify();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void customAssert() {

        Assert.assertFalse(
                va.customAssert(new Runnable() {
                    @Override
                    public void run() {
                        throw new AssertionError("failure");
                    }
                }));

        Assert.assertTrue(
                va.customAssert(new Runnable() {
                    @Override
                    public void run() {
                        //do nothing to pass
                    }
                }));
    }

    @Test
    public void customAssert1() {

        Assert.assertFalse(
                va.customAssert("Custome Assert Unit Test", new Runnable() {
                    @Override
                    public void run() {
                        throw new AssertionError("failure");
                    }
                }));

        Assert.assertTrue(
                va.customAssert("Custome Assert Unit Test", new Runnable() {
                    @Override
                    public void run() {
                        //do nothing to pass
                    }
                }));
    }

    @Test
    public void startsWith() {
        Assert.assertTrue(va.startsWith("test", null, null));
        Assert.assertTrue(va.startsWith("test", "t1234", "t"));
        Assert.assertTrue(va.startsWith("test", "t1234", "t1234"));
        Assert.assertTrue(va.startsWith("test", "t1234", ""));
        Assert.assertTrue(va.startsWith("test", "", ""));

        Assert.assertFalse(va.startsWith("test", "t1234", null));
        Assert.assertFalse(va.startsWith("test", "t1234", "t123a4"));
        Assert.assertFalse(va.startsWith("test", "t1234", "T1234"));
        Assert.assertFalse(va.startsWith("test", "", "t"));
    }

    @Test
    public void startsWithIgnoreCase() {
        Assert.assertTrue(va.startsWithIgnoreCase("test", null, null));
        Assert.assertTrue(va.startsWithIgnoreCase("test", "t1234", "t"));
        Assert.assertTrue(va.startsWithIgnoreCase("test", "t1234", "t1234"));
        Assert.assertTrue(va.startsWithIgnoreCase("test", "t1234", ""));
        Assert.assertTrue(va.startsWithIgnoreCase("test", "", ""));

        Assert.assertFalse(va.startsWithIgnoreCase("test", "t1234", null));
        Assert.assertFalse(va.startsWithIgnoreCase("test", "t1234", "t123a4"));
        Assert.assertFalse(va.startsWithIgnoreCase("test", "", "t"));

        Assert.assertTrue(va.startsWithIgnoreCase("test", null, null));
        Assert.assertTrue(va.startsWithIgnoreCase("test", "T1234", "t"));
        Assert.assertTrue(va.startsWithIgnoreCase("test", "T1234a", "T1234A"));
        Assert.assertTrue(va.startsWithIgnoreCase("test", "t1234", ""));
        Assert.assertTrue(va.startsWithIgnoreCase("test", "", ""));

        Assert.assertFalse(va.startsWithIgnoreCase("test", "T1234", null));
        Assert.assertFalse(va.startsWithIgnoreCase("test", "R1234a", "R1234A2"));
        Assert.assertFalse(va.startsWithIgnoreCase("test", "", "t"));
    }

    @Test
    public void endsWith() {
        Assert.assertTrue(va.endsWith("test", "ending", "ing"));
        Assert.assertFalse(va.endsWith("test", "ending", "Ing"));
        Assert.assertFalse(va.endsWith("test", "ending", "izng"));
    }

    @Test
    public void endsWithIgnoreCase() {
        Assert.assertTrue(va.endsWithIgnoreCase("test", "ending", "ing"));
        Assert.assertTrue(va.endsWithIgnoreCase("test", "ending", "Ing"));
        Assert.assertFalse(va.endsWithIgnoreCase("test", "ending", "izng"));
    }

    @Test
    public void contains() {
        Assert.assertTrue(va.contains("test", "contains-me And this", " And "));
        Assert.assertFalse(va.contains("test", "contains-me And this", " and "));
        Assert.assertFalse(va.contains("test", "contains-me and this", "-me  "));
    }

    @Test
    public void containsIgnoreCase() {
        Assert.assertTrue(va.containsIgnoreCase("test", "contains-me And this", " And "));
        Assert.assertTrue(va.containsIgnoreCase("test", "contains-me And this", " and "));
        Assert.assertFalse(va.containsIgnoreCase("test", "contains-me and this", "-me  "));
    }

    @Test
    public void sizeEquals() {
        Assert.assertTrue(va.sizeEquals("String", "123", 3));

        Assert.assertTrue(va.sizeEquals("String", new String[]{"1", "2", "3"}, 3));
        List<String> lst = new ArrayList<>();
        lst.add("1");
        lst.add("2");
        lst.add("3");
        Assert.assertTrue(va.sizeEquals("list", lst, 3));
        lst.remove(0);
        Assert.assertFalse(va.sizeEquals("list", lst, 3));

        HashMap<String, Integer> hash = new HashMap<String, Integer>();
        hash.put("1", 1);
        hash.put("2", 2);
        hash.put("3", 3);
        Assert.assertTrue(va.sizeEquals("hash", hash, 3));
        hash.remove("1");
        Assert.assertFalse(va.sizeEquals("hash", hash, 3));

        HashSet<String> set = new HashSet<String>();
        set.add("1");
        set.add("2");
        set.add("3");
        Assert.assertTrue(va.sizeEquals("set", set, 3));
        set.remove("1");
        Assert.assertFalse(va.sizeEquals("set", set, 3));

        Assert.assertFalse(va.sizeEquals("", null, 3));
    }

    @Test
    public void notEndsWith() {
        Assert.assertFalse(va.notEndsWith("test", "ending", "ing"));
        Assert.assertTrue(va.notEndsWith("test", "ending", "Ing"));
        Assert.assertTrue(va.notEndsWith("test", "ending", "izng"));
    }

    @Test
    public void notEndsWithIgnoreCase() {
        Assert.assertFalse(va.notEndsWithIgnoreCase("test", "ending", "ing"));
        Assert.assertFalse(va.notEndsWithIgnoreCase("test", "ending", "Ing"));
        Assert.assertTrue(va.notEndsWithIgnoreCase("test", "ending", "izng"));
    }

    @Test
    public void notContains() {
        Assert.assertFalse(va.notContains("test", "contains-me And this", " And "));
        Assert.assertTrue(va.notContains("test", "contains-me And this", " and "));
        Assert.assertTrue(va.notContains("test", "contains-me and this", "-me  "));
    }

    @Test
    public void notContainsIgnoreCase() {
        Assert.assertFalse(va.notContainsIgnoreCase("test", "contains-me And this", " And "));
        Assert.assertFalse(va.notContainsIgnoreCase("test", "contains-me And this", " and "));
        Assert.assertTrue(va.notContainsIgnoreCase("test", "contains-me and this", "-me  "));
    }

    @Test
    public void notSizeEquals() {
        Assert.assertFalse(va.notSizeEquals("String", "123", 3));

        Assert.assertFalse(va.notSizeEquals("String", new String[]{"1", "2", "3"}, 3));
        List<String> lst = new ArrayList<>();
        lst.add("1");
        lst.add("2");
        lst.add("3");
        Assert.assertFalse(va.notSizeEquals("list", lst, 3));
        lst.remove(0);
        Assert.assertTrue(va.notSizeEquals("list", lst, 3));

        HashMap<String, Integer> hash = new HashMap<String, Integer>();
        hash.put("1", 1);
        hash.put("2", 2);
        hash.put("3", 3);
        Assert.assertFalse(va.notSizeEquals("hash", hash, 3));
        hash.remove("1");
        Assert.assertTrue(va.notSizeEquals("hash", hash, 3));

        HashSet<String> set = new HashSet<String>();
        set.add("1");
        set.add("2");
        set.add("3");
        Assert.assertFalse(va.notSizeEquals("set", set, 3));
        set.remove("1");
        Assert.assertTrue(va.notSizeEquals("set", set, 3));

        Assert.assertTrue(va.notSizeEquals("", null, 3));
    }

    @Test
    public void sameJson() {
    }

    @Test
    public void sameJson1() {
    }

    @Test
    public void sameJson2() {
    }

    @Test
    public void sameJson3() {
    }

    @Test
    public void sameJson4() {
    }

    @Test
    public void sameJson5() {
    }

    @Test
    public void same() {
    }

    @Test
    public void same1() {
    }

    @Test
    public void fail() {
    }

    @Test
    public void fail1() {
    }

    @Test
    public void critical() {
    }

    @Test
    public void notNull() {
        Assert.assertFalse(va.notNull(null));
        String tempStringIsNull = null;
        Assert.assertFalse(va.notNull(tempStringIsNull));
        List<String> tempList = null;
        Assert.assertFalse(va.notNull(tempList));

        Assert.assertTrue(va.notNull(""));
        String tempString = "not null";
        Assert.assertTrue(va.notNull(tempString));
        List<String> tempListNotNull = new ArrayList<>();
        Assert.assertTrue(va.notNull(tempListNotNull));
    }

    @Test
    public void notNullWithMessage() {
        Assert.assertFalse(va.notNull("message", null));
        String tempStringIsNull = null;
        Assert.assertFalse(va.notNull("message", tempStringIsNull));
        List<String> tempList = null;
        Assert.assertFalse(va.notNull("message", tempList));

        Assert.assertTrue(va.notNull("message", ""));
        String tempString = "not null";
        Assert.assertTrue(va.notNull("message", tempString));
        List<String> tempListNotNull = new ArrayList<>();
        Assert.assertTrue(va.notNull("message", tempListNotNull));
    }

    @Test
    public void isNull() {
        Assert.assertTrue(va.isNull(null));
        String tempStringIsNull = null;
        Assert.assertTrue(va.isNull(tempStringIsNull));
        List<String> tempList = null;
        Assert.assertTrue(va.isNull(tempList));

        Assert.assertFalse(va.isNull(""));
        String tempString = "not null";
        Assert.assertFalse(va.isNull(tempString));
        List<String> tempListNotNull = new ArrayList<>();
        Assert.assertFalse(va.isNull(tempListNotNull));

    }

    @Test
    public void isNullWithMessage() {
        Assert.assertTrue(va.isNull("message", null));
        String tempStringIsNull = null;
        Assert.assertTrue(va.isNull("message", tempStringIsNull));
        List<String> tempList = null;
        Assert.assertTrue(va.isNull("message", tempList));

        Assert.assertFalse(va.isNull("message", ""));
        String tempString = "not null";
        Assert.assertFalse(va.isNull("message", tempString));
        List<String> tempListNotNull = new ArrayList<>();
        Assert.assertFalse(va.isNull("message", tempListNotNull));
    }

    @Test
    public void notSame() {
    }

    @Test
    public void notSame1() {
    }

    @Test
    public void equals() {
    }

    @Test
    public void equals1() {
    }

    @Test
    public void equals2() {
    }

    @Test
    public void equals3() {
    }

    @Test
    public void equals4() {
    }

    @Test
    public void equals5() {
    }

    @Test
    public void equals6() {
    }

    @Test
    public void equals7() {
    }

    @Test
    public void equals8() {
    }

    @Test
    public void equals9() {
    }

    @Test
    public void equals10() {
    }

    @Test
    public void equals11() {
    }

    @Test
    public void equalsToIgnoreCase() {
    }

    @Test
    public void equalsToIgnoreCase1() {
    }

    @Test
    public void assertFileExists() {
    }

    @Test
    public void assertFileExists1() {
    }

    @Test
    public void equalsToMultilineString() {
        Assert.assertTrue(va.equalsToMultilineString("", "this is a test", "this is a test"));
        Assert.assertFalse(va.equalsToMultilineString("", "this is a test 1", "this is a test 2"));

        Assert.assertTrue(va.equalsToMultilineString("", "this \nis\n a \ntest", "this \nis\n a \ntest"));
        Assert.assertFalse(va.equalsToMultilineString("", "this \nis\n a \ntest 1", "this \nis\n a \ntest 2"));

        Assert.assertTrue(va.equalsToMultilineString("", "", ""));
        String nullString = null;
        Assert.assertTrue(va.equalsToMultilineString("", null, nullString));
    }

    @Test
    public void equalsTo() {
        Assert.assertTrue(va.equalsTo("", "this is a test", "this is a test"));
        Assert.assertFalse(va.equalsTo("", "this is a test 1", "this is a test 2"));
        Assert.assertTrue(va.equalsTo("", "", ""));
        String nullString = null;
        Assert.assertTrue(va.equalsTo("", null, nullString));
    }

    @Test
    public void equalsTo2() {
    }

    @Test
    public void equalsTo3() {
    }

    @Test
    public void equalsTo4() {
    }

    @Test
    public void equalsTo5() {
    }

    @Test
    public void equalsTo6() {
    }

    @Test
    public void equalsTo7() {
    }

    @Test
    public void equalsTo8() {
    }

    @Test
    public void equalsTo9() {
    }

    @Test
    public void equalsTo10() {
    }

    @Test
    public void equalsTo11() {
    }

    @Test
    public void unExpectedException() {
    }

    @Test
    public void assertThat() {
    }

    @Test
    public void assertThat1() {
    }

    @Test
    public void isFalse() {
    }

    @Test
    public void isFalse1() {
    }

    @Test
    public void isTrue() {
    }

    @Test
    public void isTrue1() {
    }

    @Test
    public void notEquals() {
    }

    @Test
    public void notEquals1() {
    }

    @Test
    public void notEquals2() {
    }

    @Test
    public void notEquals3() {
    }

    @Test
    public void notEquals4() {
    }

    @Test
    public void notEquals5() {
    }

    @Test
    public void notEquals6() {
    }

    @Test
    public void notEquals7() {
    }

    @Test
    public void that() {
    }

    @Test
    public void that1() {
    }

    @Test
    public void arrayEquals() {
    }

    @Test
    public void arrayEquals1() {
    }

    @Test
    public void arrayEquals2() {
    }

    @Test
    public void arrayEquals3() {
    }

    @Test
    public void arrayEquals4() {
    }

    @Test
    public void arrayEquals5() {
    }

    @Test
    public void arrayEquals6() {
    }

    @Test
    public void arrayEquals7() {
    }

    @Test
    public void arrayEquals8() {
    }

    @Test
    public void arrayEquals9() {
    }

    @Test
    public void arrayEquals10() {
    }

    @Test
    public void arrayEquals11() {
    }

    @Test
    public void arrayEquals12() {
    }

    @Test
    public void arrayEquals13() {
    }

    @Test
    public void arrayEquals14() {
    }

    @Test
    public void arrayEquals15() {
    }

    @Test
    public void arrayEquals16() {
    }

    @Test
    public void arrayEquals17() {
    }

    @Test
    public void isEmpty() {

        Assert.assertTrue(va.isEmpty("String", ""));
        Assert.assertTrue(va.isEmpty("String array", new String[]{}));
        Assert.assertTrue(va.isEmpty("HashMap", new HashMap<String, String>()));
        Assert.assertTrue(va.isEmpty("HashSet", new HashSet<String>()));
        Assert.assertTrue(va.isEmpty("String List", new ArrayList<String>()));

        Assert.assertFalse(va.isEmpty("null", null));
        Assert.assertFalse(va.isEmpty("String", " "));
        Assert.assertFalse(va.isEmpty("String", "Not Empty"));
        Assert.assertFalse(va.isEmpty("String array", new String[]{""}));
        HashMap<String, String> tempHashMap = new HashMap<>();
        tempHashMap.put("item", "1");
        Assert.assertFalse(va.isEmpty("HashMap", tempHashMap));
        Set<String> tempHashSet = new HashSet<String>();
        tempHashSet.add("item");
        Assert.assertFalse(va.isEmpty("HashSet", tempHashSet));
        List<String> tempList = new ArrayList<String>();
        tempList.add("item");
        Assert.assertFalse(va.isEmpty("String List", tempList));
    }

    @Test
    public void isNotEmpty() {
        Assert.assertFalse(va.isNotEmpty("String", ""));
        Assert.assertFalse(va.isNotEmpty("String array", new String[]{}));
        Assert.assertFalse(va.isNotEmpty("HashMap", new HashMap<String, String>()));
        Assert.assertFalse(va.isNotEmpty("HashSet", new HashSet<String>()));
        Assert.assertFalse(va.isNotEmpty("String List", new ArrayList<String>()));
        Assert.assertFalse(va.isNotEmpty("null", null));

        Assert.assertTrue(va.isNotEmpty("String", " "));
        Assert.assertTrue(va.isNotEmpty("String", "Not Empty"));
        Assert.assertTrue(va.isNotEmpty("String array", new String[]{""}));
        HashMap<String, String> tempHashMap = new HashMap<>();
        tempHashMap.put("item", "1");
        Assert.assertTrue(va.isNotEmpty("HashMap", tempHashMap));
        Set<String> tempHashSet = new HashSet<String>();
        tempHashSet.add("item");
        Assert.assertTrue(va.isNotEmpty("HashSet", tempHashSet));
        List<String> tempList = new ArrayList<String>();
        tempList.add("item");
        Assert.assertTrue(va.isNotEmpty("String List", tempList));
    }

    private HashMap<Number, Number> getNumberData() {
        HashMap<Number, Number> numbers = new HashMap<>();
        numbers.put(2, 3);
        numbers.put(2.0, 3.0);
        numbers.put(2L, 4L);
        numbers.put(2f, 3f);
        return numbers;
    }

    @Test
    public void testAssertGreaterThan() {
        getNumberData().forEach((key, value) -> {
            Assert.assertTrue(va.isGreaterThan("test " + key.getClass().getSimpleName(), key, value));
            Assert.assertTrue(va.isGreaterThanOrEqualTo("test " + key.getClass().getSimpleName(), key, key));
        });
    }

    @Test()
    public void testAssertGreaterThanNegativeCase() {
        getNumberData().forEach((key, value) -> {
            Assert.assertFalse("expecting to fail",
                    va.isGreaterThan("test " + key.getClass().getSimpleName(), value, key));
            Assert.assertFalse("expecting to fail",
                    va.isGreaterThan("test " + key.getClass().getSimpleName(), value, value));
        });
    }

    @Test
    public void testAssertLessThan() {
        getNumberData().forEach((key, value) -> {
            Assert.assertTrue(va.isLessThan("test " + key.getClass().getSimpleName(), value, key));
            Assert.assertTrue(va.isLessThanOrEqualTo("test " + key.getClass().getSimpleName(), value, value));
        });
    }

    @Test
    public void testAssertLessThanNegativeCase() {
        getNumberData().forEach((key, value) -> {
            Assert.assertFalse("expecting to fail",
                    va.isLessThan("test " + key.getClass().getSimpleName(), key, value));
            Assert.assertFalse("expecting to fail",
                    va.isLessThan("test " + key.getClass().getSimpleName(), key, value));
        });
    }

    @Test
    public void pass() {
    }

    @Test
    public void pass1() {
    }

    @Test
    public void addAssertHistory() {
    }

    @Test
    public void addAssertHistory1() {
    }

    @Test
    public void getThrowExceptionOnFail() {
    }

    @Test
    public void isThrowExceptionOnFail() {
    }

    @Test
    public void onlyVerify() {
    }

    @Test
    public void isRecordSteps() {
    }

    @Test
    public void setThrowExceptionOnFail() {
    }

    @Test
    public void setRecordSteps() {
    }

    @Test
    public void isVerifyOnly() {
    }

    @Test
    public void setVerifyOnly() {
    }
}