const BASE_URL = "http://localhost:8080";

function login() {
    const role = document.getElementById("role").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    fetch(`${BASE_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password, role })
    })
    .then(res => res.json())
    .then(data => {
        localStorage.setItem("userId", data.id);
        localStorage.setItem("role", role);

        if (role === "RIDER") {
            window.location.href = "rider.html";
        } else {
            window.location.href = "driver.html";
        }
    });
}

function register() {
    const role = document.getElementById("role").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    fetch(`${BASE_URL}/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password, role })
    })
    .then(res => res.json())
    .then(data => alert("Registered Successfully"));
}