// Function to toggle like status for a post
function toggleLike(postId) {
    // Send AJAX request to the server
    fetch(`/post/${postId}/like`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            // Add CSRF token if using Spring Security's CSRF protection
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
        }
    })
    .then(response => response.json())
    .then(data => {
        // Update the UI based on the response
        const likeBtn = document.getElementById(`likeBtn-${postId}`);
        const likeText = document.getElementById(`likeText-${postId}`);
        const likeCount = document.getElementById(`likeCount-${postId}`);

        if (data.liked) {
            // User liked the post
            likeBtn.classList.remove('btn-outline-primary');
            likeBtn.classList.add('btn-primary');
            likeBtn.querySelector('i').classList.remove('far');
            likeBtn.querySelector('i').classList.add('fas');
            likeText.textContent = 'Unlike';
        } else {
            // User unliked the post
            likeBtn.classList.remove('btn-primary');
            likeBtn.classList.add('btn-outline-primary');
            likeBtn.querySelector('i').classList.remove('fas');
            likeBtn.querySelector('i').classList.add('far');
            likeText.textContent = 'Like';
        }

        // Update the like count
        likeCount.textContent = data.likeCount;
    })
    .catch(error => {
        console.error('Error toggling like:', error);
    });
}