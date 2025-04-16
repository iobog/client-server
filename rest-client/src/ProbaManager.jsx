// src/ProbaManager.jsx
import React, { useEffect, useState } from 'react';
import ProbaForm from './ProbaForm';
import { ProbaTable } from './ProbaTable';
import { createProba, deleteProba, getAllProbe, updateProba } from './utils/rest-calls';

export default function ProbaManager() {
    const [probe, setProbe] = useState([]);
    const [probaToEdit, setProbaToEdit] = useState(null);

    useEffect(() => {
        refreshProbe();
    }, []);

    const refreshProbe = () => {
        getAllProbe()
            .then(data => setProbe(data))
            .catch(err => console.error('Eroare la încărcarea probelor:', err));
    };

    const handleAdd = (proba) => {
        createProba(proba)
            .then(() => refreshProbe())
            .catch(err => console.error('Eroare la adăugare:', err));
    };

    const handleEdit = (id, proba) => {
        updateProba(id, proba)
            .then(() => {
                setProbaToEdit(null);
                refreshProbe();
            })
            .catch(err => console.error('Eroare la editare:', err));
    };

    const handleDelete = (id) => {
        deleteProba(id)
            .then(() => refreshProbe())
            .catch(err => console.error('Eroare la ștergere:', err));
    };

    return (
        <div>
            <h2>{probaToEdit ? 'Editare Proba' : 'Adaugă Proba'}</h2>
            <ProbaForm
                addFunc={handleAdd}
                editFunc={handleEdit}
                probaToEdit={probaToEdit}
                cancelEdit={() => setProbaToEdit(null)}
            />
            <hr />
            <ProbaTable
                probeList={probe}
                deleteFunc={handleDelete}
                startEditFunc={setProbaToEdit}
            />
        </div>
    );
}
