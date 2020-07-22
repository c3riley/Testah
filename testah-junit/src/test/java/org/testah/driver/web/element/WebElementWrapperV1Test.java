package org.testah.driver.web.element;

import org.junit.*;
import org.openqa.selenium.By;
import org.testah.TS;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.testah.TS;
import org.testah.framework.cli.Params;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class WebElementWrapperV1Test {

    private static String formPage = "http://htmlpreview.github.io/?https://raw.githubusercontent.com/" +
        "SeleniumHQ/selenium/master/common/src/web/formPage.html";

    @BeforeClass
    public static void setupForClass() {
        formPage = "file://" + Params.getUserDir() + "/src/test/resources/formPage.html";
        TS.browser().goTo(formPage).waitForTitle("We Leave From Here", 20).assertTitle("We Leave From Here");
    }


    /**
     * Before method setup .
     */
    @Before
    public void setup() {
        if (!TS.browser().getTitle().equals("We Leave From Here")) {
            TS.browser().goTo(formPage).waitForTitle("We Leave From Here", 20).assertTitle("We Leave From Here");
        }
    }

    public void formPage() {
        TS.browser().goTo(formPage).waitForTitle("We Leave From Here", 20).assertTitle("We Leave From Here");
    }

    @Test
    public void assertsForTextBox() {
        AbstractWebElementWrapper ele = testForTextTypeElements("email");
        TS.asserts().equalsTo("email", ele.assertAttributeExists("type").getAttribute("type"));
        TS.asserts().equalsTo("<input type=\"email\" id=\"email\">", ele.getOuterHtml());
    }

    @Test
    public void assertsForTextArea() {
        AbstractWebElementWrapper ele = testForTextTypeElements("withText");
        TS.asserts().equalsTo("5", ele.assertAttributeExists("rows").getAttribute("rows"));
        TS.asserts().equalsTo("<textarea id=\"withText\" rows=\"5\" cols=\"5\">Example text</textarea>",
            ele.getOuterHtml());
    }

    /**
     * Test for text type elements abstract web element wrapper.
     *
     * @param elementId the element id
     * @return the abstract web element wrapper
     */
    public AbstractWebElementWrapper testForTextTypeElements(final String elementId) {
        By selector = By.id(elementId);
        formPage();
        AbstractWebElementWrapper ele = TS.browser().getWebElement(selector);
        assertThat(selector, is(ele.getBy()));
        assertThat(ele.verifyFound(), is(true));
        ele.assertFound();
        assertThat(ele.isDisplayed(true), is(true));
        assertThat(ele.isDisplayed(false), is(true));
        try {
            ele.assertNotFound();
            Assert.fail("Should have thrown an error");
        } catch (AssertionError expectedError) {
            assertThat(expectedError.getMessage(), notNullValue());
        }
        try {
            ele.assertAttributeExists("thisWillNotBeFound");
            Assert.fail("Should have thrown an error");
        } catch (AssertionError expectedError) {
            assertThat(expectedError.getMessage(), notNullValue());
        }

        TS.asserts().equalsTo("test", ele.assertTypeText("test").getText());
        TS.asserts().equalsTo("", ele.clearText().assertTypeText("").getText());
        ele.getActionBuilder().sendKeys("cool").build().perform();
        ele.assertTypeText("cool");


        return ele;
    }

    @Test
    public void testDropDown() {
        By selector = By.name("selectomatic");
        formPage();
        AbstractWebElementWrapper ele = TS.browser().getWebElement(selector);
        assertThat(ele.assertFound().isEnabled(), is(true));
        ele.getAsSelectList().selectByIndex(1);
        assertThat(ele.getAsSelectList().getFirstSelectedOption().getText(), equalTo("Two"));
        ele.click().assertIsDisplayed().assertIsEnabled();
        ele.getOptions().get(1).assertIsSelected();
        assertThat(ele.getOptions().size(), equalTo(4));
    }

    @Test
    public void testCheckbox() {
        By selector = By.name("checky");
        formPage();
        AbstractWebElementWrapper ele = TS.browser().getWebElement(selector);
        assertThat(ele.assertFound().assertIsEnabled().isEnabled(), is(true));
        assertThat(ele.assertIsNotSelected().isSelected(), is(false));
        ele.click();
        assertThat(ele.assertIsSelected().isSelected(), is(true));
        assertThat(ele.getSelf().verifyAttributeValue("value", "furrfu"), is(true));
        assertThat(ele.verifyAttributeValue("value", "not found"), is(false));
        ele.scrollIntoView(10, 10).pause().waitTillIsDisplayed();
    }

    @Test
    public void getSelf() {

        AbstractWebElementWrapper element = TS.browser().getWebElement(By.cssSelector("input[value='Click!']"));
        assertThat(element.getSelf(), equalTo(element));
    }

    @Test
    public void assertAttributeExists() {

        assertThat(TS.browser().getWebElement(By.cssSelector("input[value='Click!']"))
            .assertAttributeExists("value").getAttribute("value"), equalTo("Click!"));

        try {
            TS.browser().getWebElement(By.cssSelector("input[value='Click!']")).assertAttributeExists("value2");
            Assert.fail("Expected error to be thrown");
        } catch (AssertionError expectedError) {

        }

        try {
            assertThat(TS.browser().getWebElement(By.cssSelector("input[value='Click!']"))
                .assertAttributeExists("value").getAttribute("value"), equalTo("NOT_FOUND"));
            Assert.fail("Expected error to be thrown");
        } catch (AssertionError expectedError) {

        }
    }


    @Test
    public void elementIsOk() {
    }

    @Test
    public void isAutoReport() {
    }

    @Test
    public void setAutoReport() {
    }

    @Test
    public void getDriverWebElement() {
    }

    @Test
    public void getBy() {
    }

    @Test
    public void assertIsEnabled() {
    }

    @Test
    public void isEnabled() {
    }

    @Test
    public void assertIsNotEnabled() {
    }

    @Test
    public void assertIsNotSelected() {
    }

    @Test
    public void isSelected() {
    }

    @Test
    public void assertIsSelected() {
    }

    @Test
    public void assertIsDisplayed() {
    }

    @Test
    public void isDisplayed() {
    }

    @Test
    public void isDisplayed1() {
    }

    @Test
    public void verifyIsDisplayed() {
    }

    @Test
    public void assertNotFound() {
    }

    @Test
    public void assertTypeText() {
    }

    @Test
    public void assertAttributeValue() {
    }

    @Test
    public void clearText() {
    }

    @Test
    public void click() {
    }

    @Test
    public void waitTillIsDisplayed() {
    }

    @Test
    public void waitTillIsDisplayed1() {
    }

    @Test
    public void resetElement() {
    }

    @Test
    public void dragToAndDropOn() {
    }

    @Test
    public void dragToAndDropOn1() {
    }

    @Test
    public void getActionBuilder() {
    }

    @Test
    public void dragToAndDropBy() {
    }

    @Test
    public void moveTo() {
    }

    @Test
    public void moveTo1() {
    }

    @Test
    public void moveTo2() {
    }

    @Test
    public void getAsSelectList() {
    }

    @Test
    public void getElementsWithIn() {
    }

    @Test
    public void getElementsWithIn1() {
    }

    @Test
    public void assertFound() {
    }

    @Test
    public void getListOfWebelementsWrapped() {
    }

    @Test
    public void getElementsWithInNoWait() {
    }

    @Test
    public void getElementWithIn() {
    }

    @Test
    public void getElementWithIn1() {
    }

    @Test
    public void getElementWithInNoWait() {
    }

    @Test
    public void getOptions() {
    }

    @Test
    public void getHtml() {
    }

    @Test
    public void getOuterHtml() {
    }

    @Test
    public void getText() {
    }

    @Test
    public void getTimeout() {
    }

    @Test
    public void setTimeout() {
    }

    @Test
    public void mouseOver() {
    }

    @Test
    public void mouseOverAndClick() {
    }

    @Test
    public void mouseOverAndClick1() {
    }

    @Test
    public void pause() {
    }

    @Test
    public void pause1() {
    }

    @Test
    public void typeText() {
    }

    @Test
    public void verfifyElementsWithIn() {
        By selector = By.name("login");
        AbstractWebElementWrapper loginElement = TS.browser().getWebElement(selector);
        assertThat(loginElement.verfifyElementsWithIn(By.id("email")), is(true));
        assertThat(loginElement.verfifyElementsWithIn(By.id("not_found")), is(false));
        loginElement.getElementWithIn(By.id("email")).assertIsDisplayed();
        try {
            loginElement.getElementWithIn(By.id("not_found"));
            Assert.fail("Expected to throw error");
        } catch (AssertionError expectedError) {

        }
        try {
            loginElement.getElementWithIn(By.id("not_found"),true,true);
            Assert.fail("Expected to throw error");
        } catch (AssertionError expectedError) {

        }
        try {
            assertThat(loginElement.getElementWithIn(By.id("not_found"),true,false),is(nullValue()));
        } catch (AssertionError unExpectedError) {
            Assert.fail("Expected to throw error");
        }
    }

    @Test
    public void verifyFound() {
    }

    @Test
    public void verifyNotfound() {
    }

    @Test
    public void waitTillAttributeEquals() {
    }

    @Test
    public void verifytAttributeValue() {
    }

    @Test
    public void waitTillGone() {
    }

    @Test
    public void waitTillGone1() {
    }

    @Test
    public void waitTillNotDisplayed() {
        formPage();
        TS.browser().getWebElement(By.cssSelector("input[value='Click!']"))
            .waitTillIsDisplayed().click().waitTillGone().assertNotFound();
    }

    @Test
    public void scrollIntoView() {
    }

    @Test
    public void scrollIntoView1() {
    }

}
