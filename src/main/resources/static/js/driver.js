const BASE_URL = "http://localhost:8080";

// Go Online / Offline
function setAvailable(status) {
    const userId = localStorage.getItem("userId");

    fetch(`http://localhost:8080/driver/availability?userId=${userId}&available=${status}`, {
        method: "POST"
    })
    .then(res => {
        if (!res.ok) throw new Error("Availability update failed");
        return res.json();
    })
    .then(() => {
        alert(status ? "Driver Online" : "Driver Offline");
    })
    .catch(err => console.error(err));
}

// Update Driver Location
function updateLocation() {
    const driverId = localStorage.getItem("driverId");

    const latitude = document.getElementById("lat").value;
    const longitude = document.getElementById("lng").value;

    fetch(`${BASE_URL}/driver/${driverId}/location`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            latitude: parseFloat(latitude),
            longitude: parseFloat(longitude)
        })
    })
    .then(res => {
        if (!res.ok) throw new Error("Location update failed");
        return res.json();
    })
    .then(() => {
        alert("Location Updated");
    })
    .catch(err => console.error(err));
}
function startTracking() {
    if (!navigator.geolocation) {
        alert("Geolocation not supported");
        return;
    }

    navigator.geolocation.watchPosition(
        function(position) {
            const latitude = position.coords.latitude;
            const longitude = position.coords.longitude;

            console.log("Live Location:", latitude, longitude);

            const driverId = localStorage.getItem("driverId");

            fetch(`http://localhost:8080/driver/${driverId}/location`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    latitude: latitude,
                    longitude: longitude
                })
            });
        },
        function(error) {
            console.error("Location error:", error);
        },
        {
            enableHighAccuracy: true,
            maximumAge: 0,
            timeout: 5000
        }
    );
}