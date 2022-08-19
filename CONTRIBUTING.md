Contributing to Bard Framework
======================

You have found a bug or you have an idea for a cool new feature? Contributing code is a great way to give something back
to
the open source community. Before you dig right into the code there are a few guidelines that we need contributors to
follow so that we can have a chance of keeping on top of things.

Getting Started
---------------

+ Make sure you have a [GitHub account][github-signup].
+ If you're planning to implement a new feature it makes sense to discuss your changes on the [dev list][mails] first.
  This way you can make sure you're not wasting your time on something that isn't considered to be in Bard 's scope.
+ Submit a [Ticket] for your issue, assuming one does not already exist.
    + Clearly describe the issue including steps to reproduce when it is a bug.
    + Make sure you fill in the earliest version that you know has the issue.
+ Find the corresponding [repository on GitHub][github-search],
  [fork][github-fork] and check out your forked repository.

Making Changes
--------------

+ Create a _topic branch_ for your isolated work.
    * Usually you should base your branch on the `master` or `trunk` branch.
    * A good topic branch name can be the bug id plus a keyword, e.g. `BARD-123-InputStream`.
    * If you have submitted multiple issues, try to maintain separate branches and pull requests.
+ Make commits of logical units.
    * Make sure your commit messages are meaningful and in the proper format. Your commit message should contain the key
      of the issue.
    * e.g. `BARD-123: Close input stream earlier`
+ Respect the original code style:
    + Only use spaces for indentation.
    + Create minimal diffs - disable _On Save_ actions like _Reformat Source Code_ or _Organize Imports_. If you feel
      the source code should be reformatted create a separate PR for this change first.
    + Check for unnecessary whitespace with `git diff` -- check before committing.
+ Make sure you have added the necessary tests for your changes, typically in `src/test/java`.
+ Run all the tests with `mvn clean verify` to assure nothing else was accidentally broken.

Making Trivial Changes
----------------------

The tickets are used to generate the changelog for the next release.

For changes of a trivial nature to comments and documentation, it is not always necessary to create a new ticket in bug
tracking system.
In this case, it is appropriate to start the first line of a commit with '(doc)' instead of a ticket number.


Submitting Changes
------------------

+ Push your changes to a topic branch in your fork of the repository.
+ Submit a _Pull Request_ to the corresponding repository in the `bard` organization.
    * Verify _Files Changed_ shows only your intended changes and does not
      include additional files like `target/*.class`
+ Update your ticket and include a link to the pull request in the ticket.

If you prefer to not use GitHub, then you can instead use
`git format-patch` (or `svn diff`) and attach the patch file to the issue.


Additional Resources
--------------------

+ [General GitHub documentation][github-help]
+ [GitHub pull request documentation][github-pull-request]
+ [Bard Framework Twitter Account][twitter]

[github-fork]:https://help.github.com/en/github/getting-started-with-github/fork-a-repo

[github-help]:https://help.github.com

[github-pull-request]:https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request

[github-search]:https://github.com/bardframework?tab=repositories

[github-signup]:https://github.com/signup/free

[mails]:https://bardframework.org/mails-list.html

[maven]:https://maven-badges.herokuapp.com/maven-central/org.bardframework/bard-parent

[twitter]:https://twitter.com/BardFramework
