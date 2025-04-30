document.addEventListener('DOMContentLoaded', function() {
    // Find all livestream videos on the page
    const videoElements = document.querySelectorAll('[id^="livestreamVideo-"]');
    console.log(videoElements);
    // If no video elements found, exit early
    if (videoElements.length === 0) return;

    videoElements.forEach(videoElement => {
        const postId = videoElement.id.split('-')[1];
        const livestreamEndedElement = document.getElementById('livestreamEnded-' + postId);

        if (!livestreamEndedElement) return;

        // Check if livestream has ended for this post
        const isEnded = livestreamEndedElement.getAttribute('data-ended') === 'true';

        if (isEnded) {
            videoElement.style.display = 'none';
            livestreamEndedElement.style.display = 'flex';
            return;
        }

        // Access user's camera for each livestream video
        navigator.mediaDevices.getUserMedia({ video: true, audio: false })
            .then(function(stream) {
                videoElement.srcObject = stream;

                alert(videoElement.id + "playing");
            })
            .catch(function(err) {
                console.error("Error_accessing_camera_", err);
                console.log("Could not access camera for livestream ID: " + postId);
                // Don't show alert for each failure as it could be annoying with multiple streams
                livestreamEndedElement.textContent = "Camera không khả dụng";
                livestreamEndedElement.style.display = 'flex';
                videoElement.style.display = 'none';
            });
    });
});