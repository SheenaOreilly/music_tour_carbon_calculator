

firebase.initializeApp(firebaseConfig);
const db= firebase.firestore();

function login() {
    var email = document.getElementById("email").value;
    var password = document.getElementById("password").value;

    firebase.auth().signInWithEmailAndPassword(email, password).then(function(userCredential) {
        var user = userCredential.user;
        console.log('Logged in as:', user.email);
        document.getElementById("loginForm").style.display = "none";
        document.getElementById("signupForm").style.display = "none";
        document.getElementById("videoSection").style.visibility = "visible";
        var video = document.getElementById("introVideo");
        video.autoplay = true;
        video.play();
    }).catch(function(error) {
        var errorCode = error.code;
        var errorMessage = error.message;
        console.error('Login failed:', errorCode, errorMessage);
        alert("Login failed: " + errorMessage);
    });
}

function signup() {
    var email = document.getElementById("signupEmail").value;
    var password = document.getElementById("signupPassword").value;

    firebase.auth().createUserWithEmailAndPassword(email, password)
        .then((userCredential) => {
            var user = userCredential.user;
            console.log('User created:', user.email);
            alert("Account created successfully!");
            document.getElementById("loginForm").style.display = "none";
            document.getElementById("signupForm").style.display = "none";
            document.getElementById("videoSection").style.visibility = "visible";
            var video = document.getElementById("introVideo");
            video.autoplay = true;
            video.play();
        })
        .catch((error) => {
            console.error('Signup error:', error.code, error.message);
            alert("Signup failed: " + error.message);
        });
}


function logout() {
    firebase.auth().signOut().then(() => {
        localStorage.removeItem("token");
        window.location.href = "http://localhost:8080/";
    }).catch((error) => {
        console.error("Logout error: ", error);
    });
}

function checkAuth() {
    firebase.auth().onAuthStateChanged(function(user) {
        if (user) {
            document.getElementById("loginForm").style.display = "none";
            document.getElementById("signupForm").style.display = "none";
            document.getElementById("videoSection").style.visibility = "visible";
            var video = document.getElementById("introVideo");
            video.autoplay = true;
            video.play();
            document.getElementById("loginForm").style.display = "block";
            document.getElementById("signupForm").style.display = "block";
            document.getElementById("videoSection").style.visibility = "hidden";
        }
    });
}
function checkAuthCarbon(carbonEmissions, tourName, departure, arrival) {
    firebase.auth().onAuthStateChanged(function(user) {
        if (user) {
            document.getElementById("userInfo").innerText = "Logged in as: " + user.email;
            document.getElementById("userInfo").style.display = "block";
        }
    });

    if(carbonEmissions != null){
        addData(carbonEmissions, tourName, departure, arrival);
    }
}

function addUserData(data, tourName, departure, arrival) {
    const user = firebase.auth().currentUser;
    if (user) {
        const uid = user.uid;
        const email = user.email;
        const docName = departure + " -> " + arrival;
        console.log(tourName);
        console.log(docName);
        console.log(email);
        console.log(data);
        db.collection(email).doc('Tours').collection(tourName).doc(docName).set(data)
            .then(() => {
                console.log('User data added successfully.');
            })
            .catch((error) => {
                console.error('Error adding user data:', error);
            });
    } else {
        console.log('No user is authenticated.');
    }
}

function addData(){
    const tourName = document.getElementById("tourNameText")?.innerText.trim();
    const departure = document.getElementById("departureText")?.innerText.trim();
    const arrival = document.getElementById("arrivalText")?.innerText.trim();
    const carbonEmissions = document.getElementById("carbonEmissionsText")?.innerText.trim();

    firebase.auth().onAuthStateChanged((user) => {
        if (user) {
            const data = {
                carbon_emissions: carbonEmissions
            };
            addUserData(data, tourName, departure, arrival);
        } else {
            console.log('User is not signed in.');
        }
    });
}

