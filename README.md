[![Build Status](https://travis-ci.org/gmantele/experimental-taplib.svg?branch=master)](https://travis-ci.org/gmantele/experimental-taplib)
[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](http://www.gnu.org/licenses/lgpl-3.0)

**WARNING: This repository is just an EXPERIMENTAL GitHub repository. For the
real UWS-/ADQL-/TAP-Libraries, you should visit the
[taplib repository](https://github.com/gmantele/taplib).**

README
======

This GitHub repository contains the sources of 3 libraries implementing
[IVOA](http://www.ivoa.net/ "International Virtual Observatory Alliance")
standards and protocols:
* [ADQL](http://www.ivoa.net/documents/latest/ADQL.html "Astronomical Data Query Language")
* [UWS](http://www.ivoa.net/documents/UWS/index.html "Universal Worker Service pattern")
* [TAP](http://www.ivoa.net/documents/TAP/ "Table Access Protocol")

### Documentation
For a complete documentation/tutorial and a demo of the 3 libraries you should
visit the following website: https://gmantele.github.io/experimental-taplib/.

### Java version
These libraries are developed in **Java 1.6**.

### License
The three of these libraries are under the terms of the
[LGPL v3 license](https://www.gnu.org/licenses/lgpl.html). You can find the full
description and all the conditions of use in the files src/COPYING and
src/COPYING.LESSER.

Collaboration
-------------

I strongly encourage you **to declare any issue you encounter**
[here](https://github.com/gmantele/experimental-taplib/issues). Thus anybody who
has the same problem can see whether his/her problem is already known. If the
problem is known the progress and/or comments about its resolution will be
published.

In addition, if you have forked this repository and made some corrections on
your side which are likely to interest any other user of the libraries, please,
**send a pull request**
[here](https://github.com/gmantele/experimental-taplib/pulls). If these
modifications are in adequation with the IVOA definition and are not too
specific to your usecase, they will be integrated (maybe after some
modifications) on this repository and thus made available to everybody.

Repository content
------------------

### Branch `master`

* `adql/` : directory gathering the core of ADQLLib and its official extensions
  * `adqllib/` : _subproject_ containing the sources and test files for ADQLLib.
  * `extensions/` : directory gathering all extensions of ADQLLib
                    (e.g. `pgsphere`, `sqlserver`, `h2`).

* `uws/` : directory gathering the core of UWSLib and its official extensions
  * `uwslib/` : _subproject_ containing the sources and test files for UWSLib.
  * `extensions/` : directory gathering all extensions of UWSLib
                    (e.g. `irods`, `vospace`, _some authentication method_).

* `tap/` : directory gathering the core of TAPLib and its official extensions
  * `taplib/` : _subproject_ containing the sources and test files for TAPLib.
  * `extensions/` : directory gathering all extensions of TAPLib
                    (e.g. `moc`).


### Branch `gh-pages`

* Website containing the textual documentation of the three libraries
  in addition of their Javadoc.
  * `adqltuto/` : documentation website for ADQLLib
  * `uwstuto/` : documentation website for UWSLib
  * `taptuto/` : documentation website for TAPLib

