
import { BASE_URL, PROBE_BASE_URL } from './consts';

// Helper to check response status
function status(response) {
    console.log('Response status ' + response.status);
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response);
    } else {
        return Promise.reject(new Error(response.statusText));
    }
}

// Helper to parse JSON
function json(response) {
    return response.json();
}

// Authenticated fetch helper
function getAuthHeaders(extraHeaders = {}) {
    const token = localStorage.getItem('authToken');
    return {
        ...extraHeaders,
        'Authorization': `Bearer ${token}`
    };
}

export async function loginRequest(body) {
    const url = new URL('concurs/login', BASE_URL);
    try {
        const response = await fetch(url.toString(), {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body),
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Login failed');
        }

        const data = await response.json();
        if (data.token) {
            localStorage.setItem('authToken', data.token);
        }

        return data;
    } catch (error) {
        console.error('Login error:', error);
        throw error;
    }
}

export function logoutRequest() {

    localStorage.removeItem('authToken');

    const url = new URL('logout', BASE_URL);
    return fetch(url.toString(), {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
        }
    });
}

export async function getAllProbe() {
    try {
        const response = await fetch(PROBE_BASE_URL, {
            method: 'GET',
            headers: getAuthHeaders({ 'Accept': 'application/json' }),
            mode: 'cors'
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Request failed: ${errorText}`);
        }

        return await response.json();
    } catch (error) {
        console.error('Error fetching probe:', error);
        throw error;
    }
}

export function getProbaById(id) {
    const url = `${PROBE_BASE_URL}/${id}`;
    const headers = getAuthHeaders({ 'Accept': 'application/json' });

    return fetch(url, {
        method: 'GET',
        headers,
        mode: 'cors'
    })
        .then(status)
        .then(json)
        .then(data => {
            console.log('GET proba by ID success:', data);
            return data;
        })
        .catch(error => {
            console.error('GET proba by ID failed', error);
            return Promise.reject(error);
        });
}

export function createProba(proba) {
    const headers = getAuthHeaders({
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    });

    return fetch(PROBE_BASE_URL, {
        method: 'POST',
        headers,
        mode: 'cors',
        body: JSON.stringify(proba)
    })
        .then(status)
        .then(response => response.text())
        .catch(error => {
            console.error('POST createProba failed', error);
            return Promise.reject(error);
        });
}

export function updateProba(id, proba) {
    const url = `${PROBE_BASE_URL}/${id}`;
    const headers = getAuthHeaders({
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    });

    return fetch(url, {
        method: 'PUT',
        headers,
        mode: 'cors',
        body: JSON.stringify(proba)
    })
        .then(status)
        .then(response => response.text())
        .catch(error => {
            console.error('PUT updateProba failed', error);
            return Promise.reject(error);
        });
}

export function deleteProba(id) {
    const url = `${PROBE_BASE_URL}/${id}`;
    const headers = getAuthHeaders({ 'Accept': 'application/json' });

    return fetch(url, {
        method: 'DELETE',
        headers,
        mode: 'cors'
    })
        .then(status)
        .then(response => response.text())
        .catch(error => {
            console.error('DELETE deleteProba failed', error);
            return Promise.reject(error);
        });
}


// import {BASE_URL, PROBE_BASE_URL} from './consts';
//
// // Helper to check response status
// function status(response) {
//     console.log('Response status ' + response.status);
//     if (response.status >= 200 && response.status < 300) {
//         return Promise.resolve(response);
//     } else {
//         return Promise.reject(new Error(response.statusText));
//     }
// }
//
// // Helper to parse JSON
// function json(response) {
//     return response.json();
// }
//
//
// export async function loginRequest(body) {
//     const url = new URL('concurs/login', BASE_URL);
//     try {
//         const response = await fetch(url.toString(), {
//             method: 'POST',
//             headers: { 'Content-Type': 'application/json' },
//             body: JSON.stringify(body),
//         });
//
//         if (!response.ok) {
//             const errorText = await response.text();
//             throw new Error(errorText || 'Login failed');
//         }
//
//         const data = await response.json();
//         if (data.token) {
//             localStorage.setItem('authToken', data.token);
//         }
//
//         return data;
//     } catch (error) {
//         console.error('Login error:', error);
//         throw error;
//     }
// }
//
//
// export function logoutRequest() {
//
//     localStorage.removeItem('authToken');
//
//     // const url = new URL('logout', BASE_URL);
//     // return fetch(url.toString(), {
//     //     method: 'POST',
//     //     headers: {
//     //         'Authorization': `Bearer ${localStorage.getItem('authToken')}`,
//     //     }
//     // });
// }
//
//
//
//
// export async function getAllProbe() {
//     try {
//         const token = localStorage.getItem('token');
//         const response = await fetch(PROBE_BASE_URL, {
//             method: 'GET',
//             headers: {
//                 'Accept': 'application/json',
//                 'Authorization': `Bearer ${token}`
//             },
//             mode: 'cors'
//         });
//
//         if (!response.ok) {
//             const errorText = await response.text();
//             throw new Error(`Request failed: ${errorText}`);
//         }
//
//         return await response.json();
//     } catch (error) {
//         console.error('Error fetching probe:', error);
//         throw error;
//     }
// }
//
//
//
// // GET by ID
// export function getProbaById(id) {
//     const url = `${PROBE_BASE_URL}/${id}`;
//     const headers = new Headers({ 'Accept': 'application/json' });
//
//     return fetch(url, {
//         method: 'GET',
//         headers,
//         mode: 'cors'
//     })
//         .then(status)
//         .then(json)
//         .then(data => {
//             console.log('GET proba by ID success:', data);
//             return data;
//         })
//         .catch(error => {
//             console.error('GET proba by ID failed', error);
//             return Promise.reject(error);
//         });
// }
//
// // POST - create
// export function createProba(proba) {
//     const headers = new Headers({
//         'Accept': 'application/json',
//         'Content-Type': 'application/json'
//     });
//
//     return fetch(PROBE_BASE_URL, {
//         method: 'POST',
//         headers,
//         mode: 'cors',
//         body: JSON.stringify(proba)
//     })
//         .then(status)
//         .then(response => response.text())
//         .catch(error => {
//             console.error('POST createProba failed', error);
//             return Promise.reject(error);
//         });
// }
//
// // PUT - update
// export function updateProba(id, proba) {
//     const url = `${PROBE_BASE_URL}/${id}`;
//     const headers = new Headers({
//         'Accept': 'application/json',
//         'Content-Type': 'application/json'
//     });
//
//     return fetch(url, {
//         method: 'PUT',
//         headers,
//         mode: 'cors',
//         body: JSON.stringify(proba)
//     })
//         .then(status)
//         .then(response => response.text())
//         .catch(error => {
//             console.error('PUT updateProba failed', error);
//             return Promise.reject(error);
//         });
// }
//
// // DELETE
// export function deleteProba(id) {
//     const url = `${PROBE_BASE_URL}/${id}`;
//     const headers = new Headers({ 'Accept': 'application/json' });
//
//     return fetch(url, {
//         method: 'DELETE',
//         headers,
//         mode: 'cors'
//     })
//         .then(status)
//         .then(response => response.text())
//         .catch(error => {
//             console.error('DELETE deleteProba failed', error);
//             return Promise.reject(error);
//         });
// }
