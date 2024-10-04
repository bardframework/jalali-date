Bard Framework Jalali Date
===================
[![Build Status](https://travis-ci.org/bardframework/jalali-date.svg)](https://travis-ci.org/bardframework/jalali-date)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=bardframework_jalali-date&metric=alert_status)](https://sonarcloud.io/dashboard?id=bardframework_jalali-date)
[![Coverage Status](https://coveralls.io/repos/bardframework/jalali-date/badge.svg)](https://coveralls.io/r/bardframework/jalali-date)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.bardframework/jalali-date/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.bardframework/jalali-date/)
[![Javadocs](https://javadoc.io/badge/org.bardframework/jalali-date/0.5.svg)](https://javadoc.io/doc/org.bardframework/jalali-date/0.5)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Bard Framework Jalali Date, is a Jalali (Shamsi) calendar implementation in java for parsing, validating, manipulating,
converting and formatting persian dates (like java 8 LocalDate(Time) in java 8)

Documentation
-------------
More information can be found on the [Bard Framework homepage](https://bardframework.org).
The [Javadoc](https://javadoc.io/doc/org.bardframework/jalali-date/latest/index.html) can be browsed.
Questions related to the usage of Bard Framework Jalali Date should be posted to the [user mailing list][ml].

Where can I get the latest release?
-----------------------------------
You can download source and binaries from [download page](https://repo1.maven.org/maven2/org/bardframework/jalali-date).

Alternatively you can pull it from the central Maven repositories:

```xml
<dependency>
  <groupId>org.bardframework</groupId>
  <artifactId>jalali-date</artifactId>
    <version>3.6.7</version>
</dependency>
```

Contributing
------------
We accept Pull Requests via GitHub. The [developer mailing list][ml] is the main channel of communication for
contributors.
There are some guidelines which will make applying PRs easier for us:

+ No spaces! Please use tabs for indentation.
+ Respect the code style.
+ Create minimal diffs - disable on save actions like reformat source code or organize imports. If you feel the source
  code should be reformatted create a separate PR for this change.
+ Provide JUnit tests for your changes and make sure your changes don't break any existing tests by
  running ```mvn clean test```.

You can learn more about contributing via GitHub in our [contribution guidelines](CONTRIBUTING.md).

License
-------
This code is under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0).

Donations
---------
You like Bard Framework? Then [donate](https://bardframework.org/donate) to support the development.

Additional Resources
--------------------

+ [Bard Framework Homepage](https://bardframework.org/)
+ [Bard Framework Jalali Date Issue Tracker](https://github.com/bardframework/jalali-date/issues)
+ [Bard Framework Twitter Account](https://twitter.com/BardFramework)

[ml]:https://bardframework.org/mails-list.html
