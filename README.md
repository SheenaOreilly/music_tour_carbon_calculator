# 🎸 Music Tour Carbon Calculator

The carbon emissions from touring bands are one factor in anthropogenic climate change, which needs to be mitigated. 

An application for small musician's and bands to **calculate the carbon emissions** of their tours and allow them to calculate an offset.

---

### 🔥 1. Firebase Setup

To connect the app to Firebase:

1. **Create a Firebase project** in the [Firebase Console](https://console.firebase.google.com/).
   
3. **Generate a service account key**:
   - Go to **Project Settings > Service accounts**
   - Click **"Generate new private key"**
   - Download the `.json` file
     
4. **Place the key file** in your project at: src/main/resources/
   
5. **Update this line** in `FirebaseConfig.java` to match the file path:

```java
InputStream serviceAccount = FirebaseConfig.class
    .getClassLoader()
    .getResourceAsStream("carbon-calculator-music-tours-firebase-adminsdk-fbsvc-0538dad743.json");
```

### 🌐 2. Frontend Firebase Configuration

1. **In Firebase, go to **Project Settings > General**.
2. Scroll to **"Your apps"** and copy the config object:
   
```javascript
const firebaseConfig = {
  apiKey: "YOUR_API_KEY",
  authDomain: "YOUR_AUTH_DOMAIN",
  projectId: "YOUR_PROJECT_ID",
  storageBucket: "YOUR_STORAGE_BUCKET",
  messagingSenderId: "YOUR_SENDER_ID",
  appId: "YOUR_APP_ID"
};
```

3. Paste this at the top of the **firebase-auth.js** file

### ⚙️ 3. Build the Project

Run the following command in the root directory:

```bash
mvn clean package
```
## ☁️ Deploy to Azure

1. ** Create an Azure Spring Apps project and a resource group via your Azure portal.

2. ** Deploy the application using the Maven wrapper:

```bash
./mvnw azure-spring-apps:deploy
```
