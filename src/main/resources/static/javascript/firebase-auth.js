firebase.initializeApp(firebaseConfig);
const db= firebase.firestore();

function login() {
    var email = document.getElementById("email").value;
    var password = document.getElementById("password").value;

    firebase.auth().signInWithEmailAndPassword(email, password)
        .then(function(userCredential) {
            var user = userCredential.user;
            console.log('Logged in as:', user.email);

            document.getElementById("loginForm").style.display = "none";
            document.getElementById("signupForm").style.display = "none";
            document.getElementById("box").style.display = "none";
            document.getElementById("videoSection").style.visibility = "visible";
            var video = document.getElementById("introVideo");
            video.autoplay = true;
            video.play();

            fetch("/setUserEmail", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ email: user.email })
            });
            console.log("Email set in session: ", data);
        }).catch(error => {
        console.error("Error setting email in session: ", error);
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
            document.getElementById("box").style.display = "none";
            document.getElementById("videoSection").style.visibility = "visible";
            var video = document.getElementById("introVideo");
            video.autoplay = true;
            video.play();

            fetch("/setUserEmail", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ email: user.email })
            });
            console.log("Email set in session: ", data);
        }).catch(error => {
        console.error("Error setting email in session: ", error);
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
            document.getElementById("box").style.display = "none";
            document.getElementById("videoSection").style.visibility = "visible";
            var video = document.getElementById("introVideo");
            video.autoplay = true;
            video.play();
            document.getElementById("loginForm").style.display = "block";
            document.getElementById("signupForm").style.display = "block";
            document.getElementById("box").style.display = "block";
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
        const email = user.email;

        db.collection(email).doc('Tours').collection(tourName).add(data)
            .then((docRef) => {
                console.log('User data added successfully with ID:', docRef.id);
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
    const consumption = document.getElementById("consumptionText")?.innerText.trim();
    const distance = document.getElementById("distanceText")?.innerText.trim();
    const vehicleFuel = document.getElementById("vehicleFuelText")?.innerText.trim();
    const carbonEmissions = document.getElementById("carbonEmissionsText")?.innerText.trim();
    const isConcert = document.getElementById("concertText")?.innerText.trim();
    const seats = document.getElementById("seatsText")?.innerText.trim();


    firebase.auth().onAuthStateChanged((user) => {
        if (user) {
            let data = {};
            if(vehicleFuel === "N/A")
            {
                data = {
                    Departure: departure,
                    Arrival: arrival,
                    Distance: distance,
                    Carbon_Emissions: carbonEmissions,
                    isConcert: isConcert,
                    seats: seats
                };
            }
            else{
                data = {
                    Departure: departure,
                    Arrival: arrival,
                    Distance: distance,
                    Consumption: consumption,
                    Fuel: vehicleFuel,
                    Carbon_Emissions: carbonEmissions,
                    Concert: isConcert,
                    Seats: seats
                };
            }
            addUserData(data, tourName, departure, arrival);
        } else {
            console.log('User is not signed in.');
        }
    });
}

