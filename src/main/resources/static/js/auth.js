const BASE_URL = "http://localhost:8080";

function login() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    fetch(`${BASE_URL}/auth/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: email,
            password: password
        })
    })
    .then(res => res.json())
    .then(data => {
        console.log("FULL LOGIN RESPONSE:", data);

        // IMPORTANT: backend returns id (not userId)
        localStorage.setItem("userId", data.id);
        localStorage.setItem("role", data.role);

        // If DRIVER → fetch driverId
        if (data.role === "DRIVER") {

            fetch(`${BASE_URL}/driver/by-user/${data.id}`)
                .then(res => res.json())
                .then(driver => {
                    localStorage.setItem("driverId", driver.id);
                    console.log("DriverId stored:", driver.id);

                    window.location.href = "driver.html";
                })
                .catch(err => console.error("Driver fetch error", err));

        } else {
            window.location.href = "rider.html";
        }
    })
    .catch(err => console.error("Login error", err));
}
function register() {
    const role = document.getElementById("role").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    fetch("http://localhost:8080/auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: email,
            password: password,
            role: role
        })
    })
    .then(res => res.json())
    .then(() => {
        alert("Registered successfully");
    })
    .catch(err => console.error("Register error:", err));
}