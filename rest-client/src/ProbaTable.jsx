import "./ProbaApp.css"
export function ProbaTable({ probeList, deleteFunc, startEditFunc }) {
    return (
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Nume</th>
                <th>Categorie Vârstă</th>
                <th>Număr Participanți</th>
                <th>Acțiuni</th>
            </tr>
            </thead>
            <tbody>
            { probeList.map((proba) => (
                <tr key={proba.id}>
                    <td>{proba.id}</td>
                    <td>{proba.nume}</td>
                    <td>{proba.categorieVarsta}</td>
                    <td>{proba.numarParticipanti}</td>
                    <td>
                        <button onClick={() => deleteFunc(proba.id)}>Șterge</button>
                        <button onClick={() => startEditFunc(proba)}>Editează</button>
                    </td>
                </tr>
            ))}
            </tbody>
        </table>
    );
}
