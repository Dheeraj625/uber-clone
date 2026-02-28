const BASE_URL = "http://localhost:8080";

function setAvailable(status) {
    const driverId = localStorage.getItem("userId");

    fetch(`${BASE_URL}/driver/availability`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            driverId: driverId,
            available: status
        })
    });
}

function updateLocation() {
    const driverId = localStorage.getItem("userId");

    fetch(`${BASE_URL}/driver/location`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            driverId: driverId,
            lat: document.getElementById("lat").value,
            lng: document.getElementById("lng").value
        })
    });
}