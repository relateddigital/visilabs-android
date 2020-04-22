
<p align="center">
  <a target="_blank" rel="noopener noreferrer" href="https://github.com/relateddigital/visilabs-android"><img src="https://github.com/relateddigital/visilabs-android/blob/master/app/visilabs.png" alt="Visilabs Android Library" width="500" style="max-width:100%;"></a>
</p>

# Latest Version 

***February 10, 2020*** - [Visilabs v3.0.3](https://github.com/relateddigital/visilabs-android/releases) 

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


### In App Set Up

 ```java 
 Visilabs.CreateAPI(organizationID, siteID, "http://lgr.visilabs.net",
                "visistore", "http://rt.visilabs.net", "Android", getApplicationContext(),  "http://s.visilabs.net/json", "http://s.visilabs.net/actjson", 30000);
```

You need to add code below to show inApp

    HashMap<String, String> parameters = new HashMap<>();
                parameters.put("OM.exVisitorID", exVisitorId);
                Visilabs.CallAPI().customEvent("android-visilab", parameters, MainActivity.this);                      

### Sample Applications 

- [Visilabs Sample Application](https://github.com/relateddigital/visilabs-android/releases/tag/3.0.3) (master branch)
- [Visilabs Sample Application - (Support Library) ](https://github.com/relateddigital/visilabs-android/tree/support_library)      
#### Note : 
You may use the support library module in sample application but we will not contribute support library in the future. This is our last support.

### Using the SDK

Mobile Tagging for Visilab and more information :  [Please check docs](https://docs.relateddigital.com/display/KB/Android+-+API+Setup). 

### Licences


 - [Related Digital ](https://www.relateddigital.com/)
 - [Visilabs ](http://visilabs.com/)
