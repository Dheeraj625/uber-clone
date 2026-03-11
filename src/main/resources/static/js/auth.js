//const BASE_URL = "http://localhost:8080";
//const BASE_URL = "http://192.168.29.7:8080";
const BASE_URL = "http://10.92.217.250:8080";

function login() {
    localStorage.clear();
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
    .then(res => {
        if (!res.ok) throw new Error("Login failed");
        return res.json();
    })
    .then(data => {

        console.log("FULL LOGIN RESPONSE:", data);

        localStorage.setItem("userId", data.id);
        localStorage.setItem("role", data.role);

        // DRIVER FLOW
        if (data.role && data.role.toUpperCase() === "DRIVER") {

            fetch(`${BASE_URL}/driver/by-user/${data.id}`)
            .then(res => {
                if (!res.ok) throw new Error("Driver not found");
                return res.json();
            })
            .then(driver => {

                console.log("Driver object:", driver);

                if (!driver || !driver.id) {
                    throw new Error("Driver record missing");
                }

                localStorage.setItem("driverId", driver.id);

                console.log("DriverId stored:", driver.id);

                window.location.href = "driver.html";
            })
            .catch(err => {
                console.error("Driver fetch error:", err);
                alert("Driver account not properly created");
            });

        }

        // RIDER FLOW
        else {

            window.location.href = "rider.html";
        }

    })
    .catch(err => {
        console.error("Login error:", err);
        alert("Login failed");
    });
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