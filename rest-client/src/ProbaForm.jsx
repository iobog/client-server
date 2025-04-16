import React, { useState, useEffect } from 'react';

export default function ProbaForm({ addFunc, editFunc, probaToEdit, cancelEdit }) {
    const [nume, setNume] = useState('');
    const [categorieVarsta, setCategorieVarsta] = useState('');
    const [numarParticipanti, setNumarParticipanti] = useState(0);

    useEffect(() => {
        if (probaToEdit) {
            setNume(probaToEdit.nume || '');
            setCategorieVarsta(probaToEdit.categorieVarsta || '');
            setNumarParticipanti(probaToEdit.numarParticipanti || 0);
        }
    }, [probaToEdit]);

    const handleSubmit = (e) => {
        e.preventDefault();
        const newProba = { nume, categorieVarsta, numarParticipanti };
        if (probaToEdit) {
            editFunc(probaToEdit.id, newProba);
        } else {
            addFunc(newProba);
        }
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
                onChange={(e) => setNumarParticipanti(parseInt(e.target.value, 10) || 0)}
                required
            />
            <button type="submit">{probaToEdit ? 'Salvează modificările' : 'Adaugă Proba'}</button>
            {probaToEdit && <button type="button" onClick={cancelEdit}>Anulează</button>}
        </form>
    );
}
