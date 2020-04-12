Bard Framework Jalali Date
===================
[![Build Status](https://travis-ci.org/bardframework/jalali-date.svg)](https://travis-ci.org/bardframework/jalali-date)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=bardframework_jalali-date&metric=alert_status)](https://sonarcloud.io/dashboard?id=bardframework_jalali-date)
[![Coverage Status](https://coveralls.io/repos/bardframework/jalali-date/badge.svg)](https://coveralls.io/r/bardframework/jalali-date)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.bardframework/jalali-date/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.bardframework/jalali-date/)
[![Javadocs](https://javadoc.io/badge/org.bardframework/jalali-date/0.2.svg)](https://javadoc.io/doc/org.bardframework/jalali-date/0.2)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Bard Framework Jalali Date, is a Jalali (Shamsi) calendar implementation in java for parsing, validating, manipulating, 
  converting and formatting persian dates (like java 8 LocalDate(Time) in java 8)


Documentation
-------------

More information can be found on the [Bard Framework Jalali Date homepage](https://bardframework.org/proper/jalali-date).
The [Javadoc](https://bardframework.org/proper/jalali-date/apidocs) can be browsed.
Questions related to the usage of Bard Framework Jalali Date should be posted to the [user mailing list][ml].

Where can I get the latest release?
-----------------------------------
You can download source and binaries from our [download page](https://bardframework.org/proper/jalali-date/download_lang.cgi).

Alternatively you can pull it from the central Maven repositories:

```xml
<dependency>
  <groupId>org.bardframework</groupId>
  <artifactId>jalali-date</artifactId>
  <version>0.2</version>
</dependency>
```

Contributing
------------

We accept Pull Requests via GitHub. The [developer mailing list][ml] is the main channel of communication for contributors.
There are some guidelines which will make applying PRs easier for us:
+ No spaces! Please use tabs for indentation.
+ Respect the code style.
+ Create minimal diffs - disable on save actions like reformat source code or organize imports. If you feel the source code should be reformatted create a separate PR for this change.
+ Provide JUnit tests for your changes and make sure your changes don't break any existing tests by running ```mvn clean test```.

If you plan to contribute on a regular basis, please consider filing a [contributor license agreement](https://www.apache.org/licenses/#clas).
You can learn more about contributing via GitHub in our [contribution guidelines](CONTRIBUTING.md).

License
-------
This code is under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0).

See the `NOTICE.txt` file for required notices and attributions.

Donations
---------
You like Bard Framework Jalali Date? Then [donate back to the ASF](https://www.apache.org/foundation/contributing.html) to support the development.

Additional Resources
--------------------

+ [Bard Framework Homepage](https://bardframework.org/)
+ [Bard Framework Issue Tracker](https://issues.bardframework.org/browse/JALALIDATE)
+ [Bard Framework Twitter Account](https://twitter.com/BardFramework)
+ `#bardframework` IRC channel on `irc.freenode.org`

[ml]:https://bardframework.org/mail-lists.html
