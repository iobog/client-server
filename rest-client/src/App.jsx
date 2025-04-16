import React, { useState } from 'react';
import { useAuth } from './context/AuthContext';
import { loginRequest } from './utils/rest-calls';
import ProbaManager from "./ProbaManager.jsx";

export default function App() {
    const { token, email, login, logout } = useAuth();
    const [formUsername, setFormUsername] = useState('');
    const [formPassword, setFormPassword] = useState('');
    const [error, setError] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            console.log("username:", formUsername);
            console.log("parola: ", formPassword);
            const response = await loginRequest({
                username: formUsername,
                parola: formPassword
            });
            console.log("username:", formUsername);
            console.log("parola: ", formPassword);

            login(response.token, formUsername);
            setError('');
        } catch (err) {
            setError('Login esuat');
            console.error(err);
        }
    };

    const handleLogout = () => {
        logout();
    };

    if (!token) {
        return (
            <div>
                <h2>Autentificare</h2>
                <form onSubmit={handleLogin}>
                    <input
                        type="text"
                        placeholder="username"
                        value={formUsername}
                        onChange={(e) => setFormUsername(e.target.value)}
                        required
                    />
                    <input
                        type="password"
                        placeholder="parola"
                        value={formPassword}
                        onChange={(e) => setFormPassword(e.target.value)}
                        required
                    />
                    <button type="submit">Login</button>
                </form>
                {error && <p style={{ color: 'red' }}>{error}</p>}
            </div>
        );
    }

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <h2>Bine ai venit, {email}</h2>
                <button onClick={handleLogout}>Logout</button>
            </div>
            <ProbaManager />
        </div>
    );
}
