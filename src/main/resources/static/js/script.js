const API_BASE_URL = 'http://localhost:8080/api';

// Add event listeners when the page loads
document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('searchInput');
    const searchButton = document.querySelector('.search-box button');

    // Update button text when input changes
    searchInput.addEventListener('input', function() {
        const query = this.value.trim();
        searchButton.textContent = query ? query : 'Search';
    });

    // Add event listener for Enter key
    searchInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            search();
        }
    });

    // Add event listener for search button
    searchButton.addEventListener('click', function() {
        search();
    });
});

async function search() {
    const searchInput = document.getElementById('searchInput');
    const resultsContainer = document.getElementById('results');
    const query = searchInput.value.trim();

    if (!query) {
        alert('Please enter a search query');
        return;
    }

    resultsContainer.innerHTML = `
        <div class="loader">
            <span></span>
            <span></span>
            <span></span>
            <span></span>
            <span></span>
            <span></span>
        </div>
    `;

    try {
        console.log('Sending search request for query:', query);
        const response = await fetch(`${API_BASE_URL}/search?query=${encodeURIComponent(query)}`);
        console.log('Search response status:', response.status);
        
        if (!response.ok) {
            const errorText = await response.text();
            console.error('Search failed:', errorText);
            throw new Error('Search request failed');
        }
        
        const results = await response.json();
        console.log('Search results:', results);

        if (results.length === 0) {
            resultsContainer.innerHTML = `
                <div class="result-item">
                    <div class="description">No results found for "${query}". Try different keywords or check your spelling.</div>
                </div>`;
            return;
        }

        resultsContainer.innerHTML = `
            <div class="results-count">About ${results.length} results</div>
            ${results.map(result => `
                <div class="result-item">
                    <h2><a href="${result.url}" target="_blank">${sanitizeHTML(result.title || 'Untitled')}</a></h2>
                    <div class="url">${formatUrl(result.url)}</div>
                    <div class="description">${sanitizeHTML(result.description || 'No description available')}</div>
                </div>
            `).join('')}`;
    } catch (error) {
        console.error('Search error:', error);
        resultsContainer.innerHTML = `
            <div class="result-item">
                <div class="description">An error occurred while searching. Please try again.</div>
            </div>`;
    }
}

// Helper function to sanitize HTML and prevent XSS
function sanitizeHTML(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Helper function to format URLs nicely
function formatUrl(url) {
    try {
        const urlObj = new URL(url);
        let formattedUrl = urlObj.hostname + urlObj.pathname;
        if (formattedUrl.length > 60) {
            formattedUrl = formattedUrl.substring(0, 57) + '...';
        }
        return formattedUrl;
    } catch (e) {
        return url;
    }
}

async function handleLinkClick(event, url, title) {
    window.location.href = url;
} 