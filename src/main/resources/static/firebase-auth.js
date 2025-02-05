const firebaseConfig = {
    apiKey: "AIzaSyBVDgwGXSawlZWOLbSHwg_BN9RNy_4RG2I",
    authDomain: "carbon-calculator-music-tours.firebaseapp.com",
    projectId: "carbon-calculator-music-tours",
    storageBucket: "carbon-calculator-music-tours.firebasestorage.app",
    messagingSenderId: "10100607066",
    appId: "1:101006070661:web:b3c219fd6f6ae5b18deaec"
};

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
