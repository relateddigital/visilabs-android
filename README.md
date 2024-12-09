
<p align="center">
  <a target="_blank" rel="noopener noreferrer" href="https://github.com/relateddigital/visilabs-android"><img src="https://github.com/relateddigital/visilabs-android/blob/master/app/visilabs.png" alt="Visilabs Android Library" width="500" style="max-width:100%;"></a>
</p>

# Latest Version 

***Dec 3, 2024*** - [Visilabs v6.4.0](https://github.com/relateddigital/visilabs-android/releases)

# Latest Version [![Build Status](https://travis-ci.com/relateddigital/visilabs-android.svg?branch=master)](https://travis-ci.com/relateddigital/visilabs-android)

# Table of Contents

- [Latest Version](#latest-version)
- [Visilabs Android](#visilabs-android)
  * [Installation](#installation)
    + [Sample Applications](#sample-applications)
    + [Using the SDK](#using-the-sdk)
    + [Licence](#licence)
  * [Mail Subscription Form](#mail-subscription-form)
  * [Sending Campaign Parameters](#sending-campaign-parameters)
    
# Visilabs Android

The Visilabs Android Sdk is a java implementation of an Android client for Visilabs.

## Installation

Visilabs Android SDK requires minimum API level 21.

Add maven jitpack repository to your project/build.gradle file

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Add Visilabs to the ```dependencies``` in app/build.gradle.

```java
implementation 'com.github.relateddigital:visilabs-android:6.4.6'
```
This version uses AndroidX

### Visilabs SDK Set Up

Mobile Tagging for Visilab and more information :  [Please check docs](https://relateddigital.atlassian.net/wiki/spaces/RMCKBT/pages/428802408/Android+-+API+Setup) 

        
        
### Sample Applications 

- [Visilabs Sample Application **NEW REPO ](https://github.com/relateddigital/sample_visilabs_android) 

#### Note : 
You may use the support library module in sample application but we will not contribute support library in the future. This is our last support.


|              In App Notification              | Story                                                            |
|:----------------------------------------------------------------:|----------------------------------------------------------------------------|
| ![Image of InAppNew](/Screenshots/inappnew.gif)                 | ![Image of Story](/Screenshots/story.png)                          |


## Mail Subscription Form

After form is created at **RMC** panel, likewise **in-app message**, existence of mail subscription form is controlled by after each `customEvent` call.
You need to call overload of `customEvent` which takes `Activity` as third parameter in order to show mail subscription forms.

```java
customEvent(String pageName, HashMap<String, String> properties, Activity parent)
```

It is shown as follows;

![mail-subscription-form](/Screenshots/mail-subscription-form.png)

## Spin To Win

After form is created at **RMC** panel, likewise **in-app message**, existence of spin to win is controlled by after each `customEvent` call.
You need to call overload of `customEvent` which takes `Activity` as third parameter in order to show spin to win.

```java
customEvent(String pageName, HashMap<String, String> properties, Activity parent)
```

It is shown as follows;

|               Spin to Win Full                                   |                        Spin to Win Half                                    |
|:----------------------------------------------------------------:|----------------------------------------------------------------------------|
| ![spin-to-win-full](/Screenshots/spin_to_win_full_en.jpeg)          | ![spin-to-win-half](/Screenshots/spin_to_win_half_en.jpeg)                    |


## Sending Campaign Parameters

You can send campaign parameters using `sendCampaignParameters` :

```java
HashMap<String, String> properties = new HashMap<>();
properties.put("utm_campaign","euromsg campaign");
properties.put("utm_source","euromsg");
properties.put("utm_medium","push");
Visilabs.CallAPI().sendCampaignParameters(properties);
```

### Licences


 - [Related Digital ](https://www.relateddigital.com/)
 - [Visilabs ](http://visilabs.com/)
