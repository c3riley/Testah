# Testah Junit

Automation Testing Framework written in java for testing ui applications and services.

## Using Project

### build.groovy example

```groovy
group 'org.example'
version '1.0-SNAPSHOT'

apply plugin: 'java'
apply plugin: 'groovy'

sourceCompatibility = 1.8

repositories {
    mavenCentral()
    jcenter()
    maven {
        url 'https://github.com/c3riley/maven-repository/raw/master/'
    }
}

dependencies {
    compile localGroovy()
    compile 'org.testah:testah-junit:2.0.1'
}
```

## Project Elements

### Testplan Class

#### Examples

##### Service Test
For services testing, browser only launched if called upon to do so, will not launch by default

```java

@TestPlan
public class DemoTest extends HttpTestPlan {

    @TestCase
    @Test
    public void test() {

    }

}

```

##### Browser Test
Will automatically launch a browser by default

```java

@TestPlan
public class DemoTest extends BrowserTestPlan {

    @TestCase
    @Test
    public void test() {

    }

}

```

#### Test Metadata

##### @TestPlan
Annotation used per class, the top level testing object.  Adds metadata for what the test will do, what jira items its aligned to, and ways to allow the test to be filtered and reported.

##### @TestCase

Annotation used per @Test Method.  Adds metadata for what the test will do, what jira items its aligned to, and ways to allow the test to be filtered and reported.  A TestCase will inherit from the TestPlan unless overridden.

##### step

Step is a build in method for a TestCase, it is designed to hold 1 to many step actions.  An example can be Login and go to dashboard.  This can be 1 test step even though there are multiple actions that occur.  It is not required for use, if it is not used a default step will hold all of the step actions in a test.

##### step actions

Step actions relate to the actions done to preform the test. These can include http calls, browser actions, explicit infos, and asserts. The goal is to allow the code to be readable, and the results to read like a traditional testcase, allowing the reviewer or non programmer to understand the bases of what the test does.

### TS Object

The TS object (Test State) is designed as a quick and easy way to interact with lazy loaded object instances created to enable testing.

#### TS.web

This object encapsulates webdriver and macrofys its usage.  It is designed to reduce the amount of code required to create tests, and removes all of the boiler plate setup, and has built in abilities to deal with the fragility of web testing.  It also has step action documentation built in, so will add actions to the running test to make the results more meaningful.  It is lazy loaded, so will not be loaded till called.

#### TS.http

This object encapsulated http client and can use asynchronous http client as well.  It makes testing and work with rest services into 1 line of code, but still allows for the full power of apache http to be used.

#### TS.asserts

This object is wrapped JUNit asserts as well as expanded asserts to include hammerCrest, Json Asserts and others.  It is designed to record not only failures but to write into the results passed asserts to allow for greater readability of the results.  Often if a failure occurs its good to know what passed right before it.
