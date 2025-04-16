import React, { useState } from 'react';
import type {Proba} from './utils/rest-calls';

interface Props {
    addFunc: (proba: Proba) => void;
}

export default function ProbaForm({ addFunc }: Props) {
    const [nume, setNume] = useState('');
    const [categorieVarsta, setCategorieVarsta] = useState('');
    const [numarParticipanti, setNumarParticipanti] = useState(0);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        const newProba: Proba = {
            nume,
            categorieVarsta,
            numarParticipanti,
        };
        addFunc(newProba);
        setNume('');
        setCategorieVarsta('');
        setNumarParticipanti(0);
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                placeholder="Nume"
                value={nume}
                onChange={(e) => setNume(e.target.value)}
                required
            />
            <input
                type="text"
                placeholder="Categorie vârstă"
                value={categorieVarsta}
                onChange={(e) => setCategorieVarsta(e.target.value)}
                required
            />
            <input
                type="number"
                placeholder="Număr participanți"
                value={numarParticipanti}
                onChange={(e) => setNumarParticipanti(parseInt(e.target.value))}
                required
            />
            <button type="submit">Adaugă Proba</button>
        </form>
    );
}
