package concurs.repository;

public interface Repository <ID ,T >{

    T add(T elem);
    T delete(ID id);
    T update(ID id, T elem);
    T findOne(ID id);
    Iterable<T> findAll();
}