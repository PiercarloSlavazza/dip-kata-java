
# Overview
This repo contains Java code that allows to execute a code kata aimed at learning the Dependency Inversion principle.

# How to execute the Kata
The code models the following domain:

* we have a (toy) Document Management System
* the DMS tracks actions carried on by the users
* the DMS has a portal home, where there is a "portlet" which is supposed to show a list of the most recent documents accessed by the user

Our task is to implement the "Most Recent Documents" portlet.

The idea is to use data collected for the User Analytics.

Each "event" of user interaction with the DMS is tracked - and we therefore can fetch from this data the most recent documents.

So we have:

* a `DocumentManagementPortalHome` - that must render the portlet
* and that uses a `UserAnalytics`

The code in the `main` however violates DIP in the way it "connects" these two classes: you must "decouple" them so that the resulting code is compliant with DIP.

Two solutions are provided:

* one, in branch `dip_implementation_1`
* and the other one, with decoupling further improved, in branch `dip_implementation_2`
