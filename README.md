--- 
Services: active-directory
platforms: Android
author: danieldobalian
level: 100
client: Android Mobile App
service: Microsoft Graph
endpoint: AAD V1
---
# ADAL Android Microsoft Graph API Sample 

| [Getting Started](https://docs.microsoft.com/en-us/azure/active-directory/develop/active-directory-devquickstarts-android)| [Library](https://github.com/AzureAD/azure-activedirectory-library-for-android) | [API Reference](http://javadoc.io/doc/com.microsoft.aad/adal/) | [Support](README.md#community-help-and-support)
| --- | --- | --- | --- |

![Build Badge](https://identitydivision.visualstudio.com/_apis/public/build/definitions/a7934fdd-dcde-4492-a406-7fad6ac00e17/506/badge)

The ADAL Android library gives your app the ability to begin using the
[Microsoft Azure Cloud](https://cloud.microsoft.com) by supporting [Microsoft Azure Active Directory accounts](https://azure.microsoft.com/en-us/services/active-directory/) using industry standard OAuth2 and OpenID Connect. This sample demonstrates all the normal lifecycles your application should experience, including:

* Get a token for the Microsoft Graph
* Refresh a token
* Call the Microsoft Graph
* Sign out the user

## Scenario

This app can be used for all Azure AD accounts.  It supports both single and multi Organizational scenarios (discussed in steps).  It demonstrates how a developer can build apps to connect with enterprise users and access their Azure + O365 data via the Microsoft Graph.  During the auth flow, end users will be required to sign in and consent to the permissions of the application, and in some cases may require an admin to consent to the app.  The majority of the logic in this sample shows how to auth an end user and make a basic call to the Microsoft Graph.

![Topology](./images/topology.PNG)

## Example

```Java
// Initialize your app with MSAL
AuthenticationContext mAuthContext = new AuthenticationContext(
        MainActivity.this, 
        AUTHORITY, 
        false);


// Perform authentication requests
mAuthContext.acquireToken(
    getActivity(), 
    RESOURCE_ID, 
    CLIENT_ID, 
    REDIRECT_URI,  
    PromptBehavior.Auto, 
    getAuthInteractiveCallback());

// ...

// Get tokens to call APIs like the Microsoft Graph
mAuthResult.getAccessToken()
```

## Steps to Run

### Register & Configure your app

You will need to have a native client application registered with Microsoft using the 
[Azure portal](https://portal.azure.com). 

1. Getting to app registration
    - Navigate to the [Azure portal](https://aad.portal.azure.com).  
    - Click on ***Azure Active Directory*** > ***App Registrations***. 

2. Create the app
    - Click ***New application registration***.  
    - Enter an app name in the ***Name*** field. 
    - In ***Application type***, select `Native`. 
    - In ***Redirect URI***, enter `http://localhost`.  

3. Configure Microsoft Graph
    - Select ***Settings*** > ***Required Permissions***.
    - Click ***Add***, inside ***Select an API*** select ***Microsoft Graph***. 
    - Select the permission `Sign in and read user profile` > Hit `Select` to save. 
        - This permission maps to the `User.Read` scope. 

4. Congrats! Your app is successfully configured. In the next section, you'll need:
    - `Application ID`
    - `Redirect URI`

### Get the code

1. Clone the code.
    ```
    git clone https://github.com/Azure-Samples/active-directory-android
    ```
2. Open the sample in Android Studio.
    - Select ***Open an existing Android Studio project***.

### Configure the code

All the configuration for this code sample can be found in the ***src/main/java/com/azuresamples/azuresampleapp/MainActivity.java***.  

1. Replace the constant `CLIENT_ID` with the `ApplicationID`.

2. Replace the constant `REDIRECT URI` with the `Redirect URI` you configured earlier (`http://localhost`). 

### Run the sample

1. Select ***Build*** > ***Clean Project***. 

2. Select ***Run*** > ***Run app***. 

3. The app should build and show some basic UX. When you click the `Call Graph API` button, it will prompt for a sign in, and then silently call the Microsoft Graph API with the new token.  

## Important Info

1. Checkout the [ADAL Android Wiki](https://github.com/AzureAD/azure-activedirectory-library-for-android/wiki) for more info on the library mechanics and how to configure new scenarios and capabilities. 
2. In Native scenarios, the app will use an embedded Webview and will not leave the app. The `Redirect URI` can be arbitrary. 
3. Find any problems or have requests? Feel free to create an issue or post on Stackoverflow with 
tag `azure-active-directory`. 

## Feedback, Community Help, and Support

We use [Stack Overflow](http://stackoverflow.com/questions/tagged/msal) with the community to 
provide support. We highly recommend you ask your questions on Stack Overflow first and browse 
existing issues to see if someone has asked your question before. 

If you find and bug or have a feature request, please raise the issue 
on [GitHub Issues](../../issues). 

To provide a recommendation, visit 
our [User Voice page](https://feedback.azure.com/forums/169401-azure-active-directory).

## Contribute

We enthusiastically welcome contributions and feedback. You can clone the repo and start 
contributing now. Read our [Contribution Guide](Contributing.md) for more information.

This project has adopted the 
[Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/). 
For more information see 
the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or contact 
[opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.

## Security Library

This library controls how users sign-in and access services. We recommend you always take the 
latest version of our library in your app when possible. We 
use [semantic versioning](http://semver.org) so you can control the risk associated with updating 
your app. As an example, always downloading the latest minor version number (e.g. x.*y*.x) ensures 
you get the latest security and feature enhanements but our API surface remains the same. You 
can always see the latest version and release notes under the Releases tab of GitHub.

## Security Reporting

If you find a security issue with our libraries or services please report it 
to [secure@microsoft.com](mailto:secure@microsoft.com) with as much detail as possible. Your 
submission may be eligible for a bounty through the [Microsoft Bounty](http://aka.ms/bugbounty) 
program. Please do not post security issues to GitHub Issues or any other public site. We will 
contact you shortly upon receiving the information. We encourage you to get notifications of when 
security incidents occur by 
visiting [this page](https://technet.microsoft.com/en-us/security/dd252948) and subscribing 
to Security Advisory Alerts.
