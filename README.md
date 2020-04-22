
<p align="center">
  <a target="_blank" rel="noopener noreferrer" href="https://github.com/relateddigital/visilabs-android"><img src="https://github.com/relateddigital/visilabs-android/blob/master/app/visilabs.png" alt="Visilabs Android Library" width="500" style="max-width:100%;"></a>
</p>

# Latest Version 

***April 22, 2020*** - [Visilabs v3.1.2](https://github.com/relateddigital/visilabs-android/releases) 

# Table of Contents

- [Latest Version](#latest-version)
- [Visilabs Android](#visilabs-android)
  * [Installation](#installation)
    + [Sample Applications](#sample-applications)
    + [Using the SDK](#using-the-sdk)
    + [Licence](#licence)
    
# Visilabs Android

The Visilabs Android Sdk is a java implementation of an Android client for Visilabs.

## Installation


Add Visilabs to the ```dependencies``` in app/build.gradle.

```java
implementation "com.visilabs.android:visilabs-android:3.1.2
```
This version uses AndroidX

### Visilabs SDK Set Up

Mobile Tagging for Visilab and more information :  [Please check docs](https://docs.relateddigital.com/display/KB/Android+-+API+Setup) 

### In App Set Up

 ```java 
 Visilabs.CreateAPI(organizationID, siteID, "http://lgr.visilabs.net",
                datasource, "http://rt.visilabs.net", "Android", getApplicationContext(),  "http://s.visilabs.net/json", "http://s.visilabs.net/actjson", 30000);
```
You need to get the three paramaters from RMC web panel.

organizationID
siteID
datasource

To show in app message, you need to create an active in app target action on RMC web panel.
After that please add code below to show in app message.

    HashMap<String, String> parameters = new HashMap<>();
                parameters.put("OM.exVisitorID", exVisitorId);
                Visilabs.CallAPI().customEvent("android-visilab", parameters, MainActivity.this);                      


<img src="https://github.com/relateddigital/visilabs-android/blob/master/inapp.gif" alt="Euromessage Android Library" width="300" style="max-width:100%;">

### Sample Applications 

- [Visilabs Sample Application **NEW REPO ](https://github.com/relateddigital/sample_visilabs_android) 

#### Note : 
You may use the support library module in sample application but we will not contribute support library in the future. This is our last support.


### Licences


 - [Related Digital ](https://www.relateddigital.com/)
 - [Visilabs ](http://visilabs.com/)
