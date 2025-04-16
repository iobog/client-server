import { PROBE_BASE_URL } from './consts.ts';

function status(response: Response) {
    console.log('Response status ' + response.status);
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response);
    } else {
        return Promise.reject(new Error(response.statusText));
    }
}

function json(response: Response) {
    return response.json();
}

export interface Proba {
    id?: number;
    nume: string;
    categorieVarsta: string;
    numarParticipanti: number;
}

// GET all
export function getAllProbe(): Promise<Proba[]> {
    const headers = new Headers();
    headers.append('Accept', 'application/json');

    console.log("Headers: ", headers);
    const request = new Request(PROBE_BASE_URL, {
        method: 'GET',
        headers,
        mode: 'cors'
    });

    console.log('Fetching all probe from ' + PROBE_BASE_URL);
    return fetch(request)
        .then(status)
        .then(json)
        .then(data => {
            console.log('GET all probe success:', data);
            return data;
        })
        .catch(error => {
            console.error('GET all probe failed', error);
            return Promise.reject(error);
        });
}

// GET by ID
export function getProbaById(id: number): Promise<Proba> {
    const url = `${PROBE_BASE_URL}/${id}`;
    const headers = new Headers({ 'Accept': 'application/json' });

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

// POST - create
export function createProba(proba: Proba): Promise<string> {
    const headers = new Headers({
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

// PUT - update
export function updateProba(id: number, proba: Proba): Promise<string> {
    const url = `${PROBE_BASE_URL}/${id}`;
    const headers = new Headers({
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

// DELETE
export function deleteProba(id: number): Promise<string> {
    const url = `${PROBE_BASE_URL}/${id}`;
    const headers = new Headers({ 'Accept': 'application/json' });

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
