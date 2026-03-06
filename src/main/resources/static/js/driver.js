//const BASE_URL = "http://localhost:8080";
//const BASE_URL = "http://192.168.29.7:8080";
const BASE_URL = "http://10.50.21.250:8080";

const driverId = localStorage.getItem("driverId");
let trackingInterval = null;
console.log("Driver ID:", driverId);
let stompClient = null;
//let driverId = null;

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
    connectWebSocket();
    if (!navigator.geolocation) {
        alert("Geolocation not supported");
        return;
    }
    if (!driverId) {
        alert("Driver not logged in");
        return;
    }

    console.log("Starting live tracking for driver:", driverId);

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
            }).then(res => {
                if (!res.ok) throw new Error("Location update failed");
                return res.json();
            })
            .then(data => {
                console.log("Location updated in backend");
            })
            .catch(err => console.error(err));
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

function connectWebSocket() {

    const socket = new SockJS("http://localhost:8080/ws/location");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log("Connected to WebSocket");
    });
}
function sendLocation(lat, lng) {

    if (!stompClient || !stompClient.connected) {
        console.log("WebSocket not connected");
        return;
    }

    const message = {
        driverId: driverId,
        latitude: lat,
        longitude: lng
    };

    stompClient.send(
        "/app/driver/location",
        {},
        JSON.stringify(message)
    );
}