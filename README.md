# Testah
Automation Testing tool for service and browser testing

# Using Project
In build.gradle


```
repositories {
    jcenter()
	maven {
            url 'https://github.com/c3riley/maven-repository/raw/master/'
    }
} 
 
dependencies {
    compile 'org.testah:testah-junit:0.3.3'
}
```

# Development

To build Testah projects, execute the following command within their respective root folders. With the use of the gradle wrapper, this will download the appropriate version of gradle to build with. Then clean the target directory, build the project, and run the tests.

```
./gradlew clean build
```
