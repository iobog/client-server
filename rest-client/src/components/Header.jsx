import { useAuth } from "../context/AuthContext";


export default function Header() {
    const { username, logout } = useAuth();
    return (
        <header>
            <h1 >App</h1>
            <div>
                {username}
                {!username && "Link login"}
                {username && <button onClick={logout}>Logout</button>}
            </div>
        </header>
    )
}