

firebase.initializeApp(firebaseConfig);

function login() {
    var email = document.getElementById("email").value;
    var password = document.getElementById("password").value;

    firebase.auth().signInWithEmailAndPassword(email, password).then(function(userCredential) {
        var user = userCredential.user;
        console.log('Logged in as:', user.email);
        document.getElementById("loginForm").style.display = "none";
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
            document.getElementById("videoSection").style.visibility = "visible";
            var video = document.getElementById("introVideo");
            video.autoplay = true;
            video.play(); // Start playing the video
            document.getElementById("loginForm").style.display = "block";
            document.getElementById("videoSection").style.visibility = "hidden";
        }
    });
}
