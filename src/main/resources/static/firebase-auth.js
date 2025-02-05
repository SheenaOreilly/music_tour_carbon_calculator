
firebase.initializeApp(firebaseConfig);

function login() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    firebase.auth().signInWithEmailAndPassword(email, password)
        .then((userCredential) => {
            userCredential.user.getIdToken().then(token => {
                localStorage.setItem("token", token);
                document.getElementById("loginForm").style.display = "none";
                document.getElementById("videoSection").style.display = "block";
            });
        })
        .catch(error => alert("Login Failed: " + error.message));
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
    firebase.auth().onAuthStateChanged((user) => {
        if (user) {
            user.getIdToken().then(token => {
                localStorage.setItem("token", token);
                document.getElementById("loginForm").style.display = "none";
                document.getElementById("videoSection").style.display = "block";
                document.getElementById("logoutSection").style.display = "block";
            });
        } else {
            document.getElementById("loginForm").style.display = "block";
            document.getElementById("videoSection").style.display = "none";
            document.getElementById("logoutSection").style.display = "none";
        }
    });
}
