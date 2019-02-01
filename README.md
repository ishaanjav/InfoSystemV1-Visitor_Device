# Info System V1 - Visitor Device

## Purpose/Background
<img align="right" src="https://github.com/ishaanjav/InfoSystemV1_-_Visitor_Device/blob/master/Login%20Page.gif" width =360>

**The purpose of this Android application is to serve as part of a system of 3 apps that collects information about visits to the house. The system is intended to serve at the homes of those with declining cognitive abilities such as patients with Alzheimer's or other types of dementias because these patients have difficulty recognizing faces and therefore have trouble identifying the visitor that has come to see them.** This app, in particular, is used to validate visitors who are logging in to the system through facial recognition, or a username and password. Once a visitor has signed into the app, the Alzheimer's patient and their caretaker are notified of the visitor's arrival. Furthermore, the visitor's data is stored in Firebase so that when they sign in, the patient gets to see the name of the visitor as well as their information, which includes their name, picture, contact details, relation to the patient, and extended description. The patient also receives a picure so that they can better identify the visitor.

Before being able to use their account to sign in to the system, the visitor must create an account whereupon their information will be sent to the patient's caretaker for review. Once the caretaker has approved the account, the visitor can use their account to sign in. Additionally, there is also a sign in for workers who may only visit the house once or a few times for a particular service. **By using their account to sign in, the visitor is helping the patient by not only alerting them that a visitor is at the door but by also helping them remember who they are.**

**Furthermore, the caretaker is able to view a record of all events that have taken place, empowering them with this information.** For those in the later stages of Alzheimer's, the patient can also view the information about the visitor including useful details like their picture, relation to the patient (family member, friend, etc.), and extended description (a well-explained description of who the visitor is). This gives the patient the ability to understand who is visiting them and be able to respond and act in accordance rather than not know who is visiting them.

###### This app is a part of a system of 3 other apps that function together to accomplish the processes and purposes described above. It is **not** a stand-alone app and is meant to be used in collaboration with the two other apps that can be found at these repositories: CARETAKER APP REPOSITORY, RESIDENT APP RESPOSITORY.

-----

# Usage
Because this app has an important purpose of helping those with Alzheimer's and dementia, it is not as simple as some of [my other apps](https://github.com/ishaanjav). The app has multiple pages:

- 1 for setup
- 1 for logging into the system
- 1 for "About This App"
- 2 for creating an account
- 1 for confirming that the account has been sent for approval.
###### The process of [setting up](#setup) the app can be found [here](#setup)
## Features of this App
Features and purposes of this app include, but are not limited to:
- New visitors creating accounts for the caretaker to approve.
- Visitors receiving their account information if they forget their usernames and passwords.
- Visitor sign-ins through usernames and passwords.
- Visitor sign-ins through facial recognition.
- Worker sign-ins through a special section at the bottom.
- Visitor logouts.
- Notification to caretaker and resident of:
   * sign-ins
   * account creations by new visitors
   * failed logins
**The two most important pages can be considered the Login Page and Create Account pages.*Information about those pages can be found below.***
## Login Page
<img src = "https://github.com/ishaanjav/InfoSystemV1_-_Visitor_Device/blob/master/Sign%20In%20Process.gif" align="right" width="330">

The image on the right shows the Login Page. There is an option for new visitors to **create an account** by clicking on the text. Additionally, if a user has **forgotten their account details**, they can click on the "Forgot Your Password" text to enter their username and their account info will be sent to them with the email address/phone number they provided when creating an account. 

For visitors who want to sign in, there are two options, one is through the **username and password** `EditTexts` and the other is through **Facial Recognition**. *Facial Recognition was implemented using Weka's Machine Learning Library, Firebase's ML Kit, and the Microsoft Face API.* The facial data of users is stored on the device and is not sent to the cloud. 

Underneath the sign-in section for frequent visitors is the **log out** section. Visitors simply have to tap on their name in the "Log Out" Section to log out. Their name is added to the `ListView` after signing in.

Finally, there is a sign-in section at the bottom for workers who may not be visiting frequently or more than once. This section is useful for the caretaker because workers sign in with their company's name, service they're providing, and their own name (optional). The caretaker can view this information on their own app in a page called "Log of Events". *The Caretaker app's [repository]() can be found [here]().*
## Link ABOVE

## Create Account Page

  <img src="https://github.com/ishaanjav/InfoSystemV1_-_Visitor_Device/blob/master/Create%20Account%20Page1.jpeg" align="right" width="250">
  <img src="https://github.com/ishaanjav/InfoSystemV1_-_Visitor_Device/blob/master/Create%20Account%20Page2.jpeg" align="right" width="250">

There are 2 pages where visitors can create pages by pressing on "New Visitor? Create a new account here" in the Login Page. The first page **requires their name, username, password, confirmation of password, and either one of a phone number or email address**. Visitors cannot go to the next page without filling in this information. *Usernames and passwords must be 6 or more characters.*

On the second page, users enter a **description** of themselves, *which will be read to the resident*, as well as their relation to the resident such as friend, family, etc. This is also where users choose the **facial recognition option** for signing in, *which is optional*. What is mandatory, however, is taking a picture of the visitor for the resident to view each time the visitor signs in.

<img src="https://github.com/ishaanjav/InfoSystemV1_-_Visitor_Device/blob/master/Creating%20An%20Account.gif" align="left" width="260">

After filling in the information on the second page, users go to a **confirmation page** which states that their account has been sent for approval by the caretaker. If they choose one of the options to have their account information sent to them, at this time it will be sent.

<br/>

***The gif on the left shows the full process of creating an account.***

<br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/>

----- 
# Setup
The app makes use of software such as the [**Microsoft Face API**](https://azure.microsoft.com/en-us/services/cognitive-services/face/), [**Firebase**](https://firebase.google.com/), and [**Weka's Machine Learning Library for Java**](https://sourceforge.net/projects/weka/).:
- Firebase: 
   * Storing user's account details and pictures.
   * Storing events such as visitors signing in or failed logins.
- Microsoft Face API and Weka
   * Estimating the age and gender of visitors.
   * Using the KNN, SVC, and Decision Tree Classification Algorithms as well as a Neural Network to train on visitors' facial data in order to implement facial recognition.
   
Weka's jar files are included in this repository and Firebase is used through the `google-service.json` file. **However, you must use your own Microsoft Face API Key in this app in order to use the facial recognition.** [Below](#making-the-azure-account), you can find steps about getting the Face API Key for free and using it in the app by changing 2 lines of code.

## Making the Azure Account
In order to run the face dectection and analysis, you must get an API Subscription Key from the Azure Portal. [This page](https://azure.microsoft.com/en-us/services/cognitive-services/face/) by Microsoft provides the features and capabilities of the Face API. **You can create a free Azure account that doesn't expire at [this link here](https://azure.microsoft.com/en-us/try/cognitive-services/?api=face-api) by clicking on the "Get API Key" button and choosing the option to create an Azure account**. 
## Getting the Face API Key from Azure Portal
Once you have created your account, head to the [Azure Portal](https://portal.azure.com/#home). Follow these steps:
1. Click on **"Create a resource"** on the left side of the portal.
2. Underneath **"Azure Marketplace"**, click on the **"AI + Machine Learning"** section. 
3. Now, under **"Featured"** you should see **"Face"**. Click on that.
4. You should now be at [this page](https://portal.azure.com/#create/Microsoft.CognitiveServicesFace). **Fill in the required information and press "Create" when done**.
5. Now, click on **"All resources"** on the left hand side of the Portal.
6. Click on the **name you gave the API**.
7. Underneath **"Resource Management"**, click on **"Manage Keys"**.

<p align="center">
  <img width="900" src="https://github.com/ishaanjav/Face_Analyzer/blob/master/Azure-FaceAPI%20Key.PNG">
  <td>Hi</td>
</p>

You should now be able to see two different subscription keys that you can use. Follow the [additional instructions](#using-the-api-key-in-the-app) to see how to [use the API Key in the app.](#using-the-api-key-in-the-app)

## Using the API Key in the App
To use the API Key in the app, you need to only change 2 lines of code, one in [`MainActivity.java`](https://github.com/ishaanjav/InfoSystemV1_-_Visitor_Device/blob/master/app/src/main/java/com/example/anany/informationsystemv1/MainActivity.java) and the other in [`CreateAccount2.java`](https://github.com/ishaanjav/InfoSystemV1_-_Visitor_Device/blob/master/app/src/main/java/com/example/anany/informationsystemv1/CreateAccount2.java)
**On lines 176 and 145 of `MainActivity.java` and `CreateAccount2.java`**, respectively, you should see the following line:
    
    faceServiceClient = new FaceServiceRestClient("<YOUR API ENDPOINT HERE>", "<YOUR API KEY HERE>");
Replace `<YOUR API SUBSCRIPTION KEY>` with one of your 2 keys from the [Azure Portal](https://portal.azure.com/#home). *(If you haven't gotten your API Key yet, read [this section](#making-the-azure-account))*. `<YOUR ENDPOINT HERE>` should be replaced with one of the following examples from [this API Documentation link](https://westus.dev.cognitive.microsoft.com/docs/services/563879b61984550e40cbbe8d/operations/563879b61984550f30395236). The format should be similar to: 
  
    "https://<LOCATION>/face/v1.0"
  
where `<LOCATION>` should be replaced with something like `uksouth.api.cognitive.microsoft.com`.

#### With these changes made, the app should function as intended. However, it is not meant to be a stand-alone app and is intended to be used with the following apps: [Caretaker App Repository](). [Resident App Repository]().
## Links ABOVE
------
## Conclusion
This app has many applications whether it be in the homes of individuals with Alzheimer's or dementia, or in residential homes for the elderly. Because of its ability to not only notify a patient and their caretaker of a visitor, but to also provide information about that visitor and maintain a log of events, this app can be of great assistance. 

It is part of my project that I call Info System V1, *(Information System Version 1)*, and it functions alongside 2 other apps:

- [**Caretaker App Repository**]()
- [**Resident App Repository**]()

## Links ABOVE

