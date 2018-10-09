package org.testah.driver.web.element;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.testah.TS;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

public class WebElementWrapperV1Test {

    private static final String formPage = "http://htmlpreview.github.io/?https://raw.githubusercontent.com/" +
        "SeleniumHQ/selenium/master/common/src/web/formPage.html";

    /**
     * Test setup.
     */
    @Before
    public void setup() {

    }

    public void formPage() {
        TS.browser().goTo(formPage).waitForTitle("We Leave From Here", 20).assertTitle("We Leave From Here");
    }

    @After
    public void tearDown() throws Exception {
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
    public void waitTillNotDisplayed1() {
        formPage();
        TS.browser().getWebElement(By.cssSelector("input[value='Click!']"))
            .waitTillIsDisplayed().click().waitTillGone().assertNotFound();
    }

}