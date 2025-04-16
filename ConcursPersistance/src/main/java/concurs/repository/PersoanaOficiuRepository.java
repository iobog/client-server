package concurs.repository;

import concurs.model.PersoanaOficiu;

public interface PersoanaOficiuRepository extends Repository<Integer, PersoanaOficiu> {
    PersoanaOficiu findByUsernameAndPassword(String username, String password);
}
