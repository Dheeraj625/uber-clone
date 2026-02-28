console.log("RIDER JS LOADED");
const BASE_URL = "http://localhost:8080";


function requestRide() {
    console.log("Request button clicked");

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
        document.getElementById("status").innerText = data.status;
    });
}

document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("pickupLatitude").value = 23.2599;
    document.getElementById("pickupLongitude").value = 77.4126;
    document.getElementById("dropLatitude").value = 23.1815;
    document.getElementById("dropLongitude").value = 77.3010;
});