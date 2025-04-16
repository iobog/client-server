
import  { useEffect, useState } from 'react';
import {type Proba, getAllProbe, createProba, deleteProba } from './utils/rest-calls';
import ProbaForm from './ProbaForm';
import ProbaTable from './ProbaTable';
import './ProbaApp.css';

export default function App() {
    const [probe, setProbe] = useState<Proba[]>([{ id: 0,nume: 'alergare', categorieVarsta: '9-19', numarParticipanti: 1}]);

    function addFunc(proba: Proba) {
        console.log('Adding proba:', proba);
        createProba(proba)
            .then(() => getAllProbe())
            .then((data) => setProbe(data))
            .catch((error) => console.error('Error adding proba:', error));
    }

    function deleteFunc(id: number) {
        console.log('Deleting proba with id:', id);
        deleteProba(id)
            .then(() => getAllProbe())
            .then((data) => setProbe(data))
            .catch((error) => console.error('Error deleting proba:', error));
    }

    useEffect(() => {
        console.log('Fetching all probe...');
        getAllProbe().then((data) => setProbe(data));
    }, []);

    return (
        <div className="ProbaApp">
            <h1>Proba Management</h1>
            <ProbaForm addFunc={addFunc} />
            <br />
            <ProbaTable probeList={probe} deleteFunc={deleteFunc} />
        </div>
    );
}
