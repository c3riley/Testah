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
    compile 'org.testah:testah-junit:x.x.x'
}
```

# Development

To build Testah projects, execute the following command within the testah-junit folder. With the use of the gradle wrapper, this will download the appropriate version of gradle to build with. Then clean the target directory, build the project, and run the tests.

```
cd testah-junit
./gradlew clean build
```
