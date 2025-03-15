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
            fetch("/load", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
            });
            console.log("Tours set in session: ", data);
        }).catch(error => {
        console.error("Error setting tours in session: ", error);
    });
}


function logout() {
    firebase.auth().signOut().then(() => {
        localStorage.removeItem("token");

        document.cookie = "JSESSIONID=; Max-Age=0; path=/";
        document.cookie = "JSESSIONID=; Max-Age=0; path=/; domain=localhost";

        fetch("/logout", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(() => {
            window.location.href = "http://localhost:8080/";
        }).catch((error) => {
            console.error("Error logging out from session:", error);
        });
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
function checkAuthCarbon() {
    firebase.auth().onAuthStateChanged(function(user) {
        if (user) {
            document.getElementById("userInfo").innerText = "Logged in as: " + user.email;
            document.getElementById("userInfo").style.display = "block";
        }
    });
}

function newLegCheck() {
    firebase.auth().onAuthStateChanged(function (user) {
        if (user) {
            document.getElementById("userInfo").innerText = "Logged in as: " + user.email;
            document.getElementById("userInfo").style.display = "block";
            document.getElementById("isConcert").value = "no";
            document.getElementById("isConcertPlane").value = "no";
            console.log("changed to no");
        }
    });
    fetch('/getCars', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Failed to fetch cars');
            }
        })
        .then(data => {
            const carDropdown = document.getElementById("carDropdown");
            carDropdown.innerHTML = '<option value="">-- Select a Car --</option>';

            for (const [nickname, documentId] of Object.entries(data)) {
                const option = document.createElement("option");
                option.value = documentId;
                option.textContent = nickname;
                carDropdown.appendChild(option);
                document.getElementById("selectedCar").value = documentId;
            }
        })
        .catch(error => {
            console.error('Error fetching cars:', error);
        });
}


