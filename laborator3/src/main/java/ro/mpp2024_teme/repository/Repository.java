package ro.mpp2024_teme.repository;

public interface Repository <ID ,T >{
    void add(T elem);
    void update(ID id, T elem);
    Iterable<T> findAll();
}
