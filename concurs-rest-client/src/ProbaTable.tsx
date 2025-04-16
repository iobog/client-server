import type {Proba} from './utils/rest-calls';

interface Props {
    probeList: Proba[];
    deleteFunc: (id: number) => void;
}

export default function ProbaTable({ probeList, deleteFunc }: Props) {
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
            {probeList.map((proba) => (
                <tr key={proba.id}>
                    <td>{proba.id}</td>
                    <td>{proba.nume}</td>
                    <td>{proba.categorieVarsta}</td>
                    <td>{proba.numarParticipanti}</td>
                    <td>
                        <button onClick={() => proba.id && deleteFunc(proba.id)}>Șterge</button>
                    </td>
                </tr>
            ))}
            </tbody>
        </table>
    );
}
