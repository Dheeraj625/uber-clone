console.log("RIDER JS LOADED");
//const BASE_URL = "http://localhost:8080";
//const BASE_URL = "http://192.168.29.7:8080";
const BASE_URL = "http://10.50.21.250:8080";

let currentRideId = null;
let ridePollingInterval = null;
let stompClient = null;
let driverMarker = null;

function requestRide() {
    document.getElementById("loadingSpinner").style.display = "block";
    console.log("Request button clicked");
    document.getElementById("requestBtn").disabled = true;

    // Enable cancel button
    document.getElementById("cancelBtn").disabled = false;
    
    const pickupLatEl = document.getElementById("pickupLatitude");
    const pickupLngEl = document.getElementById("pickupLongitude");
    const dropLatEl = document.getElementById("dropLatitude");
    const dropLngEl = document.getElementById("dropLongitude");

    console.log("pickupLatitude:", pickupLatEl);
    console.log("pickupLongitude:", pickupLngEl);
    console.log("dropLatitude:", dropLatEl);
    console.log("dropLongitude:", dropLngEl);

    if (!pickupLatEl || !pickupLngEl || !dropLatEl || !dropLngEl) {
        console.error("One or more input fields not found. ID mismatch!");
        return;
    }

    const riderId = localStorage.getItem("userId");

    if (
        !pickupLatEl.value || !pickupLngEl.value ||
        !dropLatEl.value || !dropLngEl.value
    ) {
        alert("Please select pickup and drop locations.");
        return;
    }

    fetch(`${BASE_URL}/ride/request`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            riderId: parseInt(riderId),
            pickupLatitude: parseFloat(pickupLatEl.value),
            pickupLongitude: parseFloat(pickupLngEl.value),
            dropLatitude: parseFloat(dropLatEl.value),
            dropLongitude: parseFloat(dropLngEl.value)
        })
    })
    .then(res => res.json())
    .then(data => {
        console.log("Ride Response:", data);
        currentRideId = data.id;
        startRidePolling();

        document.getElementById("rideStatus").innerText = data.status;

        document.getElementById("driverName").innerText =
            data.driver?.name || "Assigned";

        document.getElementById("vehicleNumber").innerText =
            data.driver?.vehicleNumber || "-";

        document.getElementById("fareAmount").innerText =
            data.fare ? "₹" + data.fare.toFixed(2) : "-";
        if (data.driver) {
            startDriverTracking(data.driver.id);
        }
    })
    .catch(err => {
        console.error("Ride request failed:", err);
    })
    .finally(() => {
    document.getElementById("loadingSpinner").style.display = "none";
    });
}

//polling fn 
function startRidePolling() {

    if (!currentRideId) return;

    // Prevent duplicate intervals
    if (ridePollingInterval) clearInterval(ridePollingInterval);

    ridePollingInterval = setInterval(() => {

        fetch(`${BASE_URL}/ride/${currentRideId}`)
            .then(res => res.json())
            .then(data => {

                console.log("Polling update:", data.status);

                document.getElementById("rideStatus").innerText = data.status;

                document.getElementById("driverName").innerText =
                    data.driver?.name || "Assigned";

                document.getElementById("vehicleNumber").innerText =
                    data.driver?.vehicleNumber || "-";

                document.getElementById("fareAmount").innerText =
                    data.fare ? "₹" + data.fare.toFixed(2) : "-";

                // Stop polling if ride finished
                if (data.status === "COMPLETED" || data.status === "CANCELLED") {
                    clearInterval(ridePollingInterval);
                    // 🔥 RE-ENABLE BUTTON HERE
                    document.getElementById("requestBtn").disabled = false;
                    document.getElementById("cancelBtn").disabled = true;
                }

            })
            .catch(err => console.error("Polling error:", err));

    }, 5000);
}
function cancelRide() {

    if (!currentRideId) {
        console.log("No active ride"); return;
    }

    fetch(`${BASE_URL}/ride/cancel/${currentRideId}`, {
        method: "POST"
    })
    .then(res => res.json())
    .then(data => {
        console.log("Cancel response:", data);
        document.getElementById("rideStatus").innerText = data.status;

        document.getElementById("cancelBtn").disabled = true;
        document.getElementById("requestBtn").disabled = false;
        
        clearInterval(ridePollingInterval);
        currentRideId = null;
    })
    .catch(err => console.error("Cancel failed:", err));
}
//day 17
function startDriverTracking(driverId) {

    console.log("Starting driver tracking for driver:", driverId);

    const socket = new SockJS(`${BASE_URL}/ws/location`);
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {

        console.log("WebSocket connected:", frame);

        stompClient.subscribe("/topic/driver/location", function(message) {

            const location = JSON.parse(message.body);

            console.log("Driver location received:", location);

            updateDriverMarker(location.latitude, location.longitude);
        });

    });
}

function updateDriverMarker(lat, lng) {

    console.log("Updating driver marker:", lat, lng);

    if (!window.map) {
        console.error("Map not initialized yet");
        return;
    }

    if (!driverMarker) {

        driverMarker = L.marker([lat, lng]).addTo(window.map);

    } else {

        driverMarker.setLatLng([lat, lng]);

    }
}