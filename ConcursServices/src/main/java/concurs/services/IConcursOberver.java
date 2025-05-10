package concurs.services;

import concurs.model.Inscriere;

public interface IConcursOberver {
    void inscriereConcurs(Inscriere inscriere) throws ConcursException;
}
