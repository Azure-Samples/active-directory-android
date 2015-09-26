---
services: active-directory
platforms: android
author: brandwe
---

# Integrating an android app with Azure AD (Android API Level 18+)

This sample shows how to build an Android application that calls a web API that requires a Work Account for authentication. This sample uses the Active Directory authentication library for Android to do the interactive OAuth 2.0 authorization code flow with public client.

**What is a Work Account?**

A Work Account is an identity you use to get work done no matter if at your business or on a college campus. Anywhere you need to get access to your work life you'll use a Work Account. The Work Account can be tied to an Active Directory server running in your datacenter or live completely in the cloud like when you use Office365. A Work Account will be how your users know that they are accessing their important documents and data backed my Microsoft security.

## Quick Start

Getting started with the sample is easy. It is configured to run out of the box with minimal setup. 

### Step 1: Register a Microsoft Azure AD Tenant

To use this sample you will need a Microsoft Azure Active Directory Tenant. If you're not sure what a tenant is or how you would get one, read [What is a Microsoft Azure AD tenant](http://technet.microsoft.com/library/jj573650.aspx)? or [Sign up for Azure as an organization](http://www.windowsazure.com/en-us/manage/services/identity/organizational-account/). These docs should get you started on your way to using Microsoft Azure AD.


### Step 2: Download and run either the .Net or Node.js REST API TODO Sample Server

This sample is written specifically to work against our existing sample for building a single tenant ToDo REST API for Azure Active Directory. This is a pre-requisite for the Quick Start.

For information on how to set this up, visit our existing samples here:

* [Microsoft Azure Active Directory Sample REST API Service for Node.js](https://github.com/Azure-Samples/WebAPI-Nodejs)
* [Microsoft Azure Active Directory Sample Web API Single Sign-On for .Net](https://github.com/Azure-Samples/active-directory-dotnet-native-client)

We recommend you deploy these on Microsoft Azure instead of running them locally (trust us, you'll want to show it off!) but you are free to do either.


### Step 3: Register your Web API with your Microsoft Azure AD Tenant

**What am I doing?**   

*Microsoft Active Directory supports adding two types of applications. Web APIs that offer services to users and applications: (either on the web or an applicaiton running on a device) that access those Web APIs. In this step you are registering the Web API you are running locally for testing this sample. Normally this Web API would be a REST service that is offering functionaltiy you want an app to access. Microsoft Azure Active Directory can protect any endpoint!* 

*Here we are assuming you are registering the TODO REST API referenced above, but this works for any Web API you'd want Azure Active Directory to protect.*

Steps to register a Web API with Microsoft Azure AD

1. Sign in to the [Azure management portal](https://manage.windowsazure.com).
2. Click on Active Directory in the left hand nav.
3. Click the directory tenant where you wish to register the sample application.
4. Click the Applications tab.
5. In the drawer, click Add.
6. Click "Add an application my organization is developing".
7. Enter a friendly name for the application, for example "TodoListService", select "Web Application and/or Web API", and click next.
8. For the sign-on URL, enter the base URL for the sample, which is by default `https://localhost:8080`.
9. For the App ID URI, enter `https://<your_tenant_name>/TodoListService`, replacing `<your_tenant_name>` with the name of your Azure AD tenant.  Click OK to complete the registration.
10. While still in the Azure portal, click the Configure tab of your application.
11. **Find the Client ID value and copy it aside**, you will need this later when configuring your application.

### Step 4: Register the sample Android Native Client application

Registering your web application is the first step. Next, you'll need to tell Azure Active Directory about your application as well. This allows your application to communicate with the just registered Web API

**What am I doing?**  

*As stated above, Microsoft Azure Active Directory supports adding two types of applications: Web APIs that offer services to users and applications (either on the web or an applicaiton running on a device) that access those Web APIs. In this step you are registering the application in this sample. You must do that in order for this application to be able to request to access the Web API you just registered. Azure Active Directory will refuse to even allow your application to ask for sign-in unless it's registered! That's part of the security of the model.* 

*Here we are assuming you are registering this sample application referenced above, but this works for any app you are developing.*

**Why am I putting both an application and a Web API in one tenant?**

*As you might have guessed, you could build an app that accesses an external API that is registered in Azure Active Directory from another tenant. If you do that your customers will be prompted to consent to the use of the API in the application. The nice part is Active Directory Authentication Library for Android takes care of this consent for you! As we get in to more advanced features you'll see this is an important part of the work needed to access the suite of Microsoft APIs from Azure and Office as well as any other service provider. For now, because you registered both your Web API and application under the same tenant you won't see any prompts for consent. This is usually the case if you are developing an application just for your own company to use.*

1. Sign in to the [Azure management portal](https://manage.windowsazure.com).
2. Click on Active Directory in the left hand nav.
3. Click the directory tenant where you wish to register the sample application.
4. Click the Applications tab.
5. In the drawer, click Add.
6. Click "Add an application my organization is developing".
7. Enter a friendly name for the application, for example "TodoListClient-DotNet", select "Native Client Application", and click next.
8. For the Redirect URI, enter `http://TodoListClient`.  Click finish.
9. Click the Configure tab of the application.
10. Find the Client ID value and copy it aside, you will need this later when configuring your application.
11. In "Permissions to Other Applications", click "Add Application."  Select "Other" in the "Show" dropdown, and click the upper check mark.  Locate & click on the TodoListService, and click the bottom check mark to add the application.  Select "Access TodoListService" from the "Delegated Permissions" dropdown, and save the configuration.


#
## Step 5: Download ADAL for Android and add it to your Eclipse Workspace

#### Download the ADAL for Android

* `git clone git@github.com:AzureAD/azure-activedirectory-library-for-android.git`

#### Import the library in to your Workspace

* In Eclipse, click on "File" -> "Import Existing Project"...

* When you are prompted, select "Existing Android Code in to Workspace" 

* Select the directory where you've cloned the ADAL for Android library. 

**NOTE:** Make sure you select the /src folder of the library and not the /target or /samples directories.

The project "AuthenticationActivity" should be imported and in your workspace.

#### IMPORTANT: Make sure the imported ADAL for Android library is marked as a "library"

Sometimes the imported library is not marked as an Android library for your applicaiton to use. To make sure that your library is available to applications, do the following:

* Right mouse click the library project "AuthenticationActivity" and select "Properties"
* Under Properties, select "Android"
* You'll see a section called "Libraries". Make sure "is a Library" is checked.

### Step 6: Download the Android Native Client Sample code and add it to your Eclipse Workspace

#### Download the Android Native Client sample code

* `$ git clone git@github.com:AzureADSamples/NativeClient-Android.git`


#### Import the library in to your Workspace

* In Eclipse, click on "File" -> "Import Existing Project"...

* When you are prompted, select "Existing Android Code in to Workspace" 

* Select the directory where you've cloned the Android Native Client Sample.

 
#### Select the ADAL for Android library for use with the sample application

* Right mouse click the library project and select "Properties"
* Under Properties, select "Android"
* You'll see a section called "Libraries". 
* Select "Add" and see the library "AuthenticationActivity" listed (you previously ensured this was available above)
* Add the library and click "OK"

 
### Step 7: Configure the Constants.java file with your Azure AD application settings

This will be where you use the information you gathered in Step 1 and Step 2.

#### Open the Constants.java file in "ToDoActivity"

* In Eclipse, navigate to the /src folder
* Find the com.microsoft.aad.test.todoapi folder
* Open the Constants.java file

Update the constants with your Azure Active Directory information. Explination of the mapping is below.

Explination of the parameters:
    
  * RESOURCE_ID is the APP ID from your registration to the Azure Portal. It is required and identifies your registered application. This is from the portal.
  
  * CLIENT_ID is required and represents your tenant. A Client ID is a way we know what permissions you are requesting for your application and validate your application in your tenant is authorized to be used by the user. 
  
  * REDIRECT_URL is where the token is redirected to in web flows. In native clients, this isn't used **yet**. Please see the important note below if you are using this with broker flows in the future.
  
  * SERVICE_URL is the deployed Web API you are trying to you access that is protected by Azure Active Directory. This should be the endpoint you registered in the Azure Active Directory portal in Step 2. It should take the form of https://<your_tenant_name>/TodoListService
  
  **NOTE:** Leave the rest of the values in this file alone for now. We'll be playing with them later in other Samples, such as when we starting using the Microsoft Azure Android Authenticator for Work.
  
### Step 8: Run the application!

Now that you've configured the app to point to the right Azure Active Directory endpoints, you can run the app! This is easy because the sample application already knows the semantics of the Rest API from our Samples so the application is ready to read and write tasks!

#### Debug the application and run the emulator

* In Eclipse, right mouse click on the ToDoActivity sample
* Select "Debug As.." and pick Android Application.

At this point, you should see your emulator launch and load the ToDoActivity in to your emulator. You can also deploy directory to any device running Android API Level 18 or higher.

## Using the Microsoft Azure Android Authenticator for Work

We will be writing more about this in the future. Please stay tuned!

## Advanced Topics

After you've gotten through this walk-through and poked around the code, you'll probably have questions about how the code works. We've got you covered! Navigate to the [Active Directory Authentication Library for Android Wiki](https://github.com/AzureAD/azure-activedirectory-library-for-android/wiki) in the ADAL for Android GitHub repo for more detailed technical discussions.






