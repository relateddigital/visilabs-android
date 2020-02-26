
<p align="center">
  <a target="_blank" rel="noopener noreferrer" href="https://github.com/relateddigital/visilabs-android"><img src="https://github.com/relateddigital/visilabs-android/blob/master/app/visilabs.png" alt="Visilabs Android Library" width="600" style="max-width:100%;"></a>
</p>

# Latest Version 

***February 10, 2020*** - [Visilabs v3.0.3](https://github.com/relateddigital/visilabs-android/releases) 

# Table of Contents

- [Latest Version](#latest-version)
- [Visilabs Android](#visilabs-android)
  * [Installation](#installation)
      - [Note that:](#note-that-)
    + [Sample Applications](#sample-applications)
    + [Using the SDK](#using-the-sdk)
    + [Licence](#licence)
    
# Visilabs Android

The Visilabs Android Sdk is a java implementation of an Android client for Visilabs.

## Installation


Add Visilabs to the ```dependencies``` in app/build.gradle.

```java
implementation "com.visilabs.android:visilabs-android:3.0.3
```
This version uses AndroidX

#### Note: 
 
You may use  [Visilabs Module](https://github.com/relateddigital/visilabs-android/tree/master/visilabs-android) directly.

  For that, 
- Download the module
- Open your project which you want to use Visilabs
- Follow steps : Android Studio -> File -> New -> Import Module and select path where you want to locate module and rename it.
- Please do not forget to link visilabs module in app/build.gradle  
```java
implementation project(path: ':visilabs-android') 
```


### Sample Applications 

- [Visilabs Sample Application](https://github.com/relateddigital/visilabs-android/releases/tag/3.0.3) (master branch)
- [Visilabs Sample Application - (Support Library) ](https://github.com/relateddigital/visilabs-android/tree/support_library)      
#### Note 
You may use the support library module but we will not contribute support library in the future. This is our last support.

### Using the SDK

Mobile Tagging for Visilab and more information :  [Please check docs](https://docs.relateddigital.com/display/KB/Android+-+API+Setup). 

### Licence


 [Related Digital ](https://www.relateddigital.com/)
