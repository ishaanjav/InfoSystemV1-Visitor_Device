# Info System V1 - Visitor Device

## Purpose/Background
<img align="right" src="https://github.com/ishaanjav/InfoSystemV1_-_Visitor_Device/blob/master/Login%20Page.gif" width =360>

**The purpose of this Android application is to serve as part of a system of 3 apps that collects information about visits to the house. The system is intended to serve at the homes of those with declining cognitive abilities such as patients with Alzheimer's or other types of dementias because these patients have difficulty recognizing faces and therefore have trouble identifying the visitor that has come to see them.** This app, in particular, is used to validate visitors who are logging in to the system through facial recognition, or a username and password. Once a visitor has signed into the app, the Alzheimer's patient and their caretaker are notified of the visitor's arrival. Furthermore, the visitor's data is stored in Firebase so that when they sign in, the patient gets to see the name of the visitor as well as their information, which includes their name, picture, contact details, relation to the patient, and extended description. The patient also receives a picure so that they can better identify the visitor.

Before being able to use their account to sign in to the system, the visitor must create an account whereupon their information will be sent to the patient's caretaker for review. Once the caretaker has approved the account, the visitor can use their account to sign in. Additionally, there is also a sign in for workers who may only visit the house once or a few times for a particular service. **By using their account to sign in, the visitor is helping the patient by not only alerting them that a visitor is at the door but by also helping them remember who they are.**

**Furthermore, the caretaker is able to view a record of all events that have taken place, empowering them with this information.** For those in the later stages of Alzheimer's, the patient can also view the information about the visitor including useful details like their picture, relation to the patient (family member, friend, etc.), and extended description (a well-explained description of who the visitor is). This gives the patient the ability to understand who is visiting them and be able to respond and act in accordance rather than not know who is visiting them.

###### This app is a part of a system of 3 other apps that function together to accomplish the processes and purposes described above. It is **not** a stand-alone app and is meant to be used in collaboration with the two other apps that can be found at these repositories: CARETAKER APP REPOSITORY, RESIDENT APP RESPOSITORY.

-----

## Usage
Because this app has an important purpose of helping those with Alzheimer's and dementia, it is not as simple as some of [my other apps](https://github.com/ishaanjav). The app has multiple pages:

- 1 for setup
- 1 for logging into the system
- 1 for "About This App"
- 2 for creating an account
- 1 for confirming that the account has been sent for approval.

### Features of This App
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
 
### Login Page
<img src = "https://github.com/ishaanjav/InfoSystemV1_-_Visitor_Device/blob/master/Login%20Page%20Image.jpeg" align="right" width="330">

The image on the right shows the Login Page. There is an option for new visitors to **create an account** by clicking on the text. Additionally, if a user has **forgotten their account details**, they can click on the "Forgot Your Password" text to enter their username and their account info will be sent to them with the email address/phone number they provided when creating an account. 

For visitors who want to sign in, there are two options, one is through the **username and password** `EditTexts` and the other is through **Facial Recognition**. *Facial Recognition was implemented using Weka's Machine Learning Library, Firebase's ML Kit, and the Microsoft Face API.* The facial data of users is stored on the device and is not sent to the cloud. 

Underneath the sign-in section for frequent visitors is the **log out** section. Visitors simply have to tap on their name in the "Log Out" Section to log out. Their name is added to the `ListView` after signing in.

Finally, there is a sign-in section at the bottom for workers who may not be visiting frequently or more than once. This section is useful for the caretaker because workers sign in with their company's name, service they're providing, and their own name (optional). The caretaker can view this information on their own app in a page called "Log of Events". *The Caretaker app's [repository]() can be found [here]().*
### Link ABOVE

<img src="https://github.com/ishaanjav/InfoSystemV1_-_Visitor_Device/blob/master/Create%20Account%20Page1.jpeg" align="left" width="250">
<img src="https://github.com/ishaanjav/InfoSystemV1_-_Visitor_Device/blob/master/Create%20Account%20Page2.jpeg" align="right" width="250">


### Create Account Page


There are 2 pages where visitors can create pages by pressing on "New Visitor? Create a new account here" in the Login Page. The first page **requires their name, username, password, confirmation of password, and either one of a phone number or email address**. Visitors cannot go to the next page without filling in this information. *Usernames and passwords must be 6 or more characters.*

On the second page, users enter a **description** of themselves, *which will be read to the resident*, as well as their relation to the resident such as friend, family, etc. This is also where users choose the **facial recognition option** for signing in, *which is optional*. What is mandatory, however, is taking a picture of the visitor for the resident to view each time the visitor signs in.

After filling in the information on the second page, users go to a confirmation page which states that their account has been sent for approval.


