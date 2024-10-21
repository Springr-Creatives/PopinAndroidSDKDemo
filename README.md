# **How to Integrate PopinAndroidSDK for Video Calls in Your Android Application**

## **Introduction**
This article explains how to integrate and use the `PopinAndroidSDK` library to add video calling functionality in your Android application. The SDK is easy to integrate and comes with pre-built classes like `PopinConnectingDialog` to handle video call connection states.

## **Step 1: Add PopinAndroidSDK Dependency**

First, ensure that your project has the correct dependency for the `PopinAndroidSDK`.

1. Open your project’s `settings.gradle` and add the JitPack repository to resolve the Popin library.
   
   ```gradle
   dependencyResolutionManagement {
       repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
       repositories {
           mavenCentral()
           maven { url 'https://jitpack.io' }
       }
   }
   ```

2. Add the following dependency to your `build.gradle` file:

   ```gradle
   dependencies {
       implementation 'com.github.Springr-Creatives:PopinAndroidSDK:1.5.9'
   }
   ```

## **Step 2: Initialize PopinAndroidSDK**

Once the SDK is added, initialize the Popin service in your activity. This is typically done in the `onCreate` method of your main activity.

```java
Popin.init(MainActivity.this);
```

This method prepares the SDK to handle video calls.

## **Step 3: Create a Call Button**

Next, you can create a button in your layout XML file (e.g., `activity_main.xml`) to trigger a video call:

```xml
<Button
    android:id="@+id/buttonCall"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Start Call"/>
```

## **Step 4: Implement Call Button Functionality**

In your `MainActivity.java`, locate the `Button` and add an `OnClickListener` to initiate the call:

```java
Button buttonCall = findViewById(R.id.buttonCall);
buttonCall.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        // Show "Connecting..." dialog
        PopinConnectingDialog cdd = new PopinConnectingDialog(MainActivity.this);
        cdd.show();

        // Start the video call and handle call events
        Popin.getInstance().startCall(new PopinEventsListener() {
            @Override
            public void onCallStart() {
                // The call has started
            }

            @Override
            public void onQueuePositionChanged(int i) {
                // Update UI based on queue position (if applicable)
            }

            @Override
            public void onAllExpertsBusy() {
                // Dismiss the dialog if all experts are busy
                cdd.dismiss();
            }

            @Override
            public void onCallConnected() {
                // Dismiss the dialog when the call connects
                cdd.dismiss();
            }

            @Override
            public void onCallFailed() {
                // Dismiss the dialog if the call fails
                cdd.dismiss();
            }

            @Override
            public void onCallDisconnected() {
                // Handle call disconnection logic here
            }
        });
    }
});
```

### **Explanation of Call Events:**

- **onCallStart()**: Triggered when the call starts connecting.
- **onQueuePositionChanged(int i)**: Provides updates if the call is in a queue.
- **onAllExpertsBusy()**: Called when all call participants (experts) are busy.
- **onCallConnected()**: Called when the call successfully connects.
- **onCallFailed()**: Called when the call fails to connect.
- **onCallDisconnected()**: Called when the call disconnects.

## **Step 5: PopinConnectingDialog**

`PopinConnectingDialog` is a custom dialog that appears while the call is connecting. It shows a "Please Wait" message to the user. You can customize this dialog by editing the layout or message as needed.

```java
PopinConnectingDialog cdd = new PopinConnectingDialog(MainActivity.this);
cdd.show();
```

This ensures the user has visual feedback while the call is being connected.

---
