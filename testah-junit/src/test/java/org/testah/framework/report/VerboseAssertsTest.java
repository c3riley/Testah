package org.testah.framework.report;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testah.TS;
import org.testah.client.dto.TestCaseDto;
import org.testah.client.dto.TestStepDto;
import org.testah.framework.report.asserts.base.AssertNotAllowedWithNullActual;
import org.unitils.reflectionassert.ReflectionAssert;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.Matchers.is;

public class VerboseAssertsTest
{

    private VerboseAsserts va;

    @Before
    public void setUp() throws Exception
    {
        va = new VerboseAsserts().onlyVerify();
    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void customAssert()
    {

        Assert.assertFalse(
                va.customAssert(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        throw new AssertionError("failure");
                    }
                }));

        Assert.assertTrue(
                va.customAssert(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //do nothing to pass
                    }
                }));
    }

    @Test
    public void customAssert1()
    {

        Assert.assertFalse(
                va.customAssert("Custom Assert Unit Test", new Runnable()
                {
                    @Override
                    public void run()
                    {
                        throw new AssertionError("failure");
                    }
                }));

        Assert.assertTrue(
                va.customAssert("Custom Assert Unit Test", new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //do nothing to pass
                    }
                }));
    }

    @Test
    public void startsWith()
    {
        Assert.assertTrue(va.given("test").startsWith(null).isFailed());
        Assert.assertTrue(va.given("t1234").startsWith("t").isPassed());
        Assert.assertTrue(va.given("t1234").startsWith("t1234").isPassed());
        Assert.assertTrue(va.given("t1234").startsWith("").isPassed());
        Assert.assertTrue(va.given("").startsWith("").isPassed());

        Assert.assertFalse(va.given("t1234").startsWith(null).isPassed());
        Assert.assertFalse(va.given("t1234").startsWith("t123a4").isPassed());
        Assert.assertFalse(va.given("t1234").startsWith("T1234").isPassed());
        Assert.assertFalse(va.given("").startsWith("t").isPassed());
    }

    @Test(expected = AssertNotAllowedWithNullActual.class)
    public void startsWithIgnoreCaseWithNullNotAllowed()
    {
        String nullValue = null;
        Assert.assertTrue(va.given(nullValue).startsWithIgnoreCase(null).isFailed());
    }

    @Test
    public void startsWithIgnoreCase()
    {
        String nullValue = null;
        Assert.assertTrue(va.given("t1234").startsWithIgnoreCase("t").isPassed());
        Assert.assertTrue(va.given("t1234").startsWithIgnoreCase("t1234").isPassed());
        Assert.assertTrue(va.given("t1234").startsWithIgnoreCase("").isPassed());
        Assert.assertTrue(va.given("").startsWithIgnoreCase("").isPassed());

        Assert.assertFalse(va.given("t1234").startsWithIgnoreCase(null).isPassed());
        Assert.assertFalse(va.given("t1234").startsWithIgnoreCase("t123a4").isPassed());
        Assert.assertFalse(va.given("").startsWithIgnoreCase("t").isPassed());
    }

    @Test
    public void endsWith()
    {
        Assert.assertTrue(va.given("ending").endsWith("ing").isPassed());
        Assert.assertFalse(va.given("ending").endsWith("Ing").isPassed());
        Assert.assertFalse(va.given("ending").endsWith("izng").isPassed());
    }

    @Test
    public void endsWithIgnoreCase()
    {
        Assert.assertTrue(va.given("ending").endsWithIgnoreCase("ing").isPassed());
        Assert.assertTrue(va.given("ending").endsWithIgnoreCase("Ing").isPassed());
        Assert.assertFalse(va.given("ending").endsWithIgnoreCase("izng").isPassed());
    }

    @Test
    public void contains()
    {
        Assert.assertTrue(va.given("contains-me And this").contains(" And ").isPassed());
        Assert.assertFalse(va.given("contains-me And this").contains(" and ").isPassed());
        Assert.assertFalse(va.given("contains-me And this").contains("-me  ").isPassed());
    }

    @Test
    public void containsIgnoreCase()
    {
        Assert.assertTrue(va.given("contains-me And this").containsIgnoreCase(" And ").isPassed());
        Assert.assertTrue(va.given("contains-me And this").containsIgnoreCase(" and ").isPassed());
        Assert.assertFalse(va.given("contains-me And this").containsIgnoreCase("-me  ").isPassed());
    }

    @Test
    public void sizeEquals()
    {
        Assert.assertTrue(va.given("String").size().equalsTo(6).isPassed());

        Assert.assertTrue(va.given(new String[]{"1", "2", "3"}).size().equalsTo(3).isPassed());
        List<String> lst = new ArrayList<>();
        lst.add("1");
        lst.add("2");
        lst.add("3");
        Assert.assertTrue(va.given(lst).size().equalsTo(3).isPassed());
        lst.remove(0);
        Assert.assertFalse(va.given(lst).size().equalsTo(3).isPassed());

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
    public void notEndsWith()
    {
        Assert.assertFalse(va.given("ending").notEndsWith("ing").isPassed());
        Assert.assertTrue(va.given("ending").notEndsWith("Ing").isPassed());
        Assert.assertTrue(va.given("ending").notEndsWith("izng").isPassed());
    }

    @Test
    public void notEndsWithIgnoreCase()
    {
        Assert.assertFalse(va.given("ending").notEndsWithIgnoreCase("ing").isPassed());
        Assert.assertFalse(va.given("ending").notEndsWithIgnoreCase("Ing").isPassed());
        Assert.assertTrue(va.given("ending").notEndsWithIgnoreCase("izng").isPassed());
    }

    @Test
    public void notContains()
    {
        Assert.assertFalse(va.given("contains-me And this").notContains(" And ").isPassed());
        Assert.assertTrue(va.given("contains-me And this").notContains(" and ").isPassed());
        Assert.assertTrue(va.given("contains-me and this").notContains("-me  ").isPassed());
    }

    @Test
    public void notContainsIgnoreCase()
    {
        Assert.assertFalse(va.given("contains-me And this").notContainsIgnoreCase(" And ").isPassed());
        Assert.assertFalse(va.given("contains-me And this").notContainsIgnoreCase(" and ").isPassed());
        Assert.assertTrue(va.given("contains-me and this").notContainsIgnoreCase("-me  ").isPassed());
    }

    @Test
    public void notSizeEquals()
    {
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
    public void sameJsonWithMatchingObjects() throws IOException
    {
        String obj1 = "{ \"name\":\"John\", \"age\":30, \"car\":null }";
        String obj2 = "{\"age\":30,  \"name\":\"John\", \"car\":null }";

        Assert.assertThat(va.sameJson("Identical String objects should match", obj1, obj2), is(true));

        JSONObject json1 = TS.util().getMap().readValue(obj1, JSONObject.class);
        JSONObject json2 = TS.util().getMap().readValue(obj2, JSONObject.class);

        Assert.assertThat(va.sameJson("Identical Json Objects should match", json1, json2, true), is(true));
    }

    @Test
    public void sameJsonStrict()
    {
        String obj1 = "{\"id\":1, \"friends\":[{\"id\":2}, {\"id\":3}]}";
        String obj2 = "{\"id\":1, \"friends\":[{\"id\":3}, {\"id\":2}]}";

        Assert.assertThat(va.sameJson("Extra field in the expected Json should not match", obj1, obj2, true), is(true));
    }

    @Test
    public void sameJsonWithoutMatchingObjects() throws IOException
    {
        String obj1 = "{\"id\":1, \"friends\":[{\"id\":2}, {\"id\":3}]}";
        String obj2 = "{\"id\":1, \"friends\":[{\"id\":3}, {\"id\":2}, {\"id\":4}]}";

        Assert.assertThat(va.sameJson("Extra field in the expected object should not match the actual", obj1, obj2, true), is(false));

        JSONObject json1 = TS.util().getMap().readValue(obj1, JSONObject.class);
        JSONObject json2 = TS.util().getMap().readValue(obj2, JSONObject.class);

        Assert.assertThat(va.sameJson("Extra field in the expected JsonObject should not match", json1, json2, true), is(false));

        obj1 = "{\"id\":1, \"friends\":[{\"id\":2}, {\"id\":3}]}";
        obj2 = "{\"id\":1, \"friends\":[{\"id\":3}, {\"id\":2}]}";

        Assert.assertThat(va.sameJson("Extra field in the expected Json should not match", obj1, obj2, true), is(true));


        obj1 = "{\"id\":1, \"friends\":[{\"id\":2}, {\"id\":3}]}";
        obj2 = "{\"id\":null, \"friends\":[{\"id\":2}, {\"id\":3}]}";

        Assert.assertThat(va.sameJson("Null value in the expected Json should not match", obj1, obj2, true), is(false));

        obj1 = "{\"id\":1, \"friends\":[{\"id\":2}, {\"id\":3}]}";
        obj2 = "{\"friends\":[{\"id\":2}, {\"id\":3}]}";

        Assert.assertThat(va.sameJson("Missing field in the expected Json should not match", obj1, obj2, true), is(false));

        obj1 = "{\"id\":1, \"friends\":[{\"id\":2}, {\"id\":3}]}";
        obj2 = "{\"id\":\"\", \"friends\":[{\"id\":2}, {\"id\":3}]}";

        Assert.assertThat(va.sameJson("Empty value in the expected Json should not match", obj1, obj2, true), is(false));
    }

    @Test(expected = AssertionError.class)
    public void sameJsonTestException()
    {
        String obj1 = "{\"id\":1, \"friends\":[{\"id\":2}, {\"id\":3}]}";
        String obj2 = "{\"id\":1, \"friends\":[{\"id\":3}, {\"id\":2}, {\"id\":4}]}";

        VerboseAsserts verboseAsserts = new VerboseAsserts(true);
        verboseAsserts.sameJson("Should throw Assertion Error", obj1, obj2, true);
    }

    @Test
    public void same()
    {
        HashMap<String, Integer> hash1 = new HashMap<String, Integer>();
        hash1.put("1", 1);
        hash1.put("2", 2);
        hash1.put("3", 3);

        Assert.assertThat(va.same(hash1, hash1), is(true));

        String obj1 = "{\"id\":1, \"friends\":[{\"id\":2}, {\"id\":3}]}";

        Assert.assertThat(va.same("Same objects should match", obj1, obj1), is(true));
    }

    @Test(expected = AssertionError.class)
    public void sameTestException()
    {
        HashMap<String, Integer> hash1 = new HashMap<String, Integer>();
        hash1.put("1", 1);
        hash1.put("2", 2);
        hash1.put("3", 3);

        HashMap<String, Integer> hash2 = new HashMap<String, Integer>();
        hash1.put("1", 1);
        hash1.put("2", 2);

        VerboseAsserts verboseAsserts = new VerboseAsserts(true);
        verboseAsserts.same(hash1, hash2);
    }

    @Test
    public void fail()
    {
        Assert.assertThat(va.fail(), is(false));

        if (1 != 2)
        {
            Assert.assertThat(va.fail("Fail method should return false"), is(false));
        }
    }

    @Test(expected = AssertionError.class)
    public void failWithException()
    {
        VerboseAsserts verboseAsserts = new VerboseAsserts(true);
        verboseAsserts.fail();
    }

    @Test
    public void critical()
    {
    }

    @Test
    public void notNull()
    {
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
    public void notNullWithMessage()
    {
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
    public void isNull()
    {
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
    public void isNullWithMessage()
    {
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
    public void notSame()
    {
    }

    @Test
    public void notSame1()
    {
    }

    @Test
    public void equals()
    {
    }

    @Test
    public void equals1()
    {
    }

    @Test
    public void equals2()
    {
    }

    @Test
    public void equals3()
    {
    }

    @Test
    public void equals4()
    {
    }

    @Test
    public void equals5()
    {
    }

    @Test
    public void equals6()
    {
    }

    @Test
    public void equals7()
    {
    }

    @Test
    public void equals8()
    {
    }

    @Test
    public void equals9()
    {
    }

    @Test
    public void equals10()
    {
    }

    @Test
    public void equals11()
    {
    }

    @Test
    public void equalsToIgnoreCase()
    {
    }

    @Test
    public void equalsToIgnoreCase1()
    {
    }

    @Test
    public void assertFileExists()
    {
        TestCaseDto test1 = new TestCaseDto();
        test1.setName("test1");
        test1.addTestStep(new TestStepDto("test", "test1"));
        test1.getTestSteps().get(0).setStatus(false);
        TestCaseDto test2 = new TestCaseDto();
        test2.setName("test2");
        test2.addTestStep(new TestStepDto("test", "test2"));
        test2.getTestSteps().get(0).setStatus(true);
        try
        {
            ReflectionAssert.assertReflectionEquals(test1, test2);
        } catch (Throwable throwable)
        {
            TS.step().action().createInfo("assertReflectionEquals", throwable.getMessage());
        }
        EqualsBuilder.reflectionEquals(test1, test2);

        List<String> lst1 = new ArrayList<>();
        lst1.add("1");
        lst1.add("1");
        lst1.add("2");
        lst1.add("1");
        List<String> lst2 = new ArrayList<>();
        lst2.add("1");
        lst2.add("1");
        lst2.add("1");
        lst2.add("1");

        try
        {
            ReflectionAssert.assertReflectionEquals(lst1, lst2);
        } catch (Throwable throwable)
        {
            TS.step().action().createInfo("assertReflectionEquals", throwable.getMessage());
        }


    }

    @Test
    public void assertFileExists1()
    {


    }

    @Test
    public void equalsToMultilineString()
    {
        Assert.assertTrue(va.equalsToMultilineString("", "this is a test", "this is a test"));
        Assert.assertFalse(va.equalsToMultilineString("", "this is a test 1", "this is a test 2"));

        Assert.assertTrue(va.equalsToMultilineString("", "this \nis\n a \ntest", "this \nis\n a \ntest"));
        Assert.assertFalse(va.equalsToMultilineString("", "this \nis\n a \ntest 1", "this \nis\n a \ntest 2"));

        Assert.assertTrue(va.equalsToMultilineString("", "", ""));
        String nullString = null;
        Assert.assertTrue(va.equalsToMultilineString("", null, nullString));
    }

    @Test
    public void equalsTo()
    {
        Assert.assertTrue(va.equalsTo("", "this is a test", "this is a test"));
        Assert.assertFalse(va.equalsTo("", "this is a test 1", "this is a test 2"));
        Assert.assertTrue(va.equalsTo("", "", ""));
        String nullString = null;
        Assert.assertTrue(va.equalsTo("", null, nullString));
    }

    @Test
    public void equalsTo2()
    {
    }

    @Test
    public void equalsTo3()
    {
    }

    @Test
    public void equalsTo4()
    {
    }

    @Test
    public void equalsTo5()
    {
    }

    @Test
    public void equalsTo6()
    {
    }

    @Test
    public void equalsTo7()
    {
    }

    @Test
    public void equalsTo8()
    {
    }

    @Test
    public void equalsTo9()
    {
    }

    @Test
    public void equalsTo10()
    {
    }

    @Test
    public void equalsTo11()
    {
    }

    @Test
    public void unExpectedException()
    {
    }

    @Test
    public void assertThat()
    {
    }

    @Test
    public void assertThat1()
    {
    }

    @Test
    public void isFalse()
    {
    }

    @Test
    public void isFalse1()
    {
    }

    @Test
    public void isTrue()
    {
    }

    @Test
    public void isTrue1()
    {
    }

    @Test
    public void notEquals()
    {
    }

    @Test
    public void notEquals1()
    {
    }

    @Test
    public void notEquals2()
    {
    }

    @Test
    public void notEquals3()
    {
    }

    @Test
    public void notEquals4()
    {
    }

    @Test
    public void notEquals5()
    {
    }

    @Test
    public void notEquals6()
    {
    }

    @Test
    public void notEquals7()
    {
    }

    @Test
    public void that()
    {
    }

    @Test
    public void that1()
    {
    }

    @Test
    public void arrayEquals()
    {
    }

    @Test
    public void arrayEquals1()
    {
    }

    @Test
    public void arrayEquals2()
    {
    }

    @Test
    public void arrayEquals3()
    {
    }

    @Test
    public void arrayEquals4()
    {
    }

    @Test
    public void arrayEquals5()
    {
    }

    @Test
    public void arrayEquals6()
    {
    }

    @Test
    public void arrayEquals7()
    {
    }

    @Test
    public void arrayEquals8()
    {
    }

    @Test
    public void arrayEquals9()
    {
    }

    @Test
    public void arrayEquals10()
    {
    }

    @Test
    public void arrayEquals11()
    {
    }

    @Test
    public void arrayEquals12()
    {
    }

    @Test
    public void arrayEquals13()
    {
    }

    @Test
    public void arrayEquals14()
    {
    }

    @Test
    public void arrayEquals15()
    {
    }

    @Test
    public void arrayEquals16()
    {
    }

    @Test
    public void arrayEquals17()
    {
    }

    @Test
    public void isEmpty()
    {

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
    public void isNotEmpty()
    {
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

    @Test
    public void testAssertGreaterThan()
    {
        getNumberData().forEach((key, value) ->
        {
            Assert.assertTrue(va.isGreaterThan("test " + key.getClass().getSimpleName(), key, value));
            Assert.assertTrue(va.isGreaterThanOrEqualTo("test " + key.getClass().getSimpleName(), key, key));
        });
    }

    private HashMap<Number, Number> getNumberData()
    {
        HashMap<Number, Number> numbers = new HashMap<>();
        numbers.put(2, 3);
        numbers.put(2.0, 3.0);
        numbers.put(2L, 4L);
        numbers.put(2f, 3f);
        return numbers;
    }

    @Test()
    public void testAssertGreaterThanNegativeCase()
    {
        getNumberData().forEach((key, value) ->
        {
            Assert.assertFalse("expecting to fail",
                    va.isGreaterThan("test " + key.getClass().getSimpleName(), value, key));
            Assert.assertFalse("expecting to fail",
                    va.isGreaterThan("test " + key.getClass().getSimpleName(), value, value));
        });
    }

    @Test
    public void testAssertLessThan()
    {
        getNumberData().forEach((key, value) ->
        {
            Assert.assertTrue(va.isLessThan("test " + key.getClass().getSimpleName(), value, key));
            Assert.assertTrue(va.isLessThanOrEqualTo("test " + key.getClass().getSimpleName(), value, value));
        });
    }

    @Test
    public void testAssertLessThanNegativeCase()
    {
        getNumberData().forEach((key, value) ->
        {
            Assert.assertFalse("expecting to fail",
                    va.isLessThan("test " + key.getClass().getSimpleName(), key, value));
            Assert.assertFalse("expecting to fail",
                    va.isLessThan("test " + key.getClass().getSimpleName(), key, value));
        });
    }

    @Test
    public void pass()
    {
    }

    @Test
    public void pass1()
    {
    }

    @Test
    public void addAssertHistory()
    {
    }

    @Test
    public void addAssertHistory1()
    {
    }

    @Test
    public void getThrowExceptionOnFail()
    {
    }

    @Test
    public void isThrowExceptionOnFail()
    {
    }

    @Test
    public void onlyVerify()
    {
    }

    @Test
    public void isRecordSteps()
    {
    }

    @Test
    public void setThrowExceptionOnFail()
    {
    }

    @Test
    public void setRecordSteps()
    {
    }

    @Test
    public void isVerifyOnly()
    {
    }

    @Test
    public void setVerifyOnly()
    {
    }
}