# Visilabs Android

The Visilabs Android Sdk is a java implementation of an Android client for Visilabs.

## Installation

You need to add  ``` implementation "com.visilabs.android:visilabs-android:3.0.3" ``` to build.gradle 

This version uses AndroidX

Also you can check  [Visilabs Demo Application](https://github.com/relateddigital/visilabs-android-sdk/releases/tag/3.0.3) for demo purpose. You may find demo application in master branch

#### Note that: 

If you do not use AndroidX, you may use  [Visilabs Module - (Support Library)](https://github.com/relateddigital/visilabs-android/tree/support_library/visilabs-android). But we will not contribute support library in the future. This is our last supported version. 

For use this module, 
- Please download module
- Open your project which you want to use Visilabs
- Follow steps : Android Studio -> File -> New -> Import Module and select path where you want to locate module and rename it.
- Please do not forget to link visilabs module adding  ```   implementation project(path: ':visilabs-android') ```to your build.gradle


Also you can check  [Visilabs Demo Application - (Support Library) ](https://github.com/relateddigital/visilabs-android/tree/support_library) for demo purpose. You can use its module too. 


### Using the SDK

Mobile Tagging for Visilab and more information :  [Please check this](https://docs.relateddigital.com/display/KB/Android+-+API+Setup). 

### Licence


 [Related Digital ](https://www.relateddigital.com/)
