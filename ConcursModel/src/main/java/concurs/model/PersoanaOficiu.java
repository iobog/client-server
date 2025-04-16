package concurs.model;

public class PersoanaOficiu extends Entity<Integer>{
    public String oras;
    public String username;
    public String parola;

    public PersoanaOficiu(String oras, String username, String parola) {
        this.oras = oras;
        this.username = username;
        this.parola = parola;
    }
    public PersoanaOficiu() {

    }


    public PersoanaOficiu(String username, String parola) {
        this.username = username;
        this.parola = parola;
    }

    public String getOras() {
        return oras;
    }

    public String getUsername() {
        return username;
    }

    public String getParola() {
        return parola;
    }

    public void setOras(String oras) {
        this.oras = oras;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public String toString() {
        return "PersoanaOficiu{" +
                "oras=" + oras +
                ", username='" + username + '\'' +
                ", parola=" + parola +
                '}';
    }
}




//
//
//package concurs.model;
//
//import jakarta.persistence.*;
//import jakarta.persistence.Entity;
//
//@Entity
//@Table(name="PersoaneOficiu")
//public class PersoanaOficiu {
//
//    private Integer id;
//    @Column(name="oras")
//    private String oras;
//    @Column(name = "username")
//    private String username;
//    @Column(name = "parola")
//    private String parola;
//
//    public PersoanaOficiu(String oras, String username, String parola) {
//        this.oras = oras;
//        this.username = username;
//        this.parola = parola;
//    }
//
//    public PersoanaOficiu(String username, String parola) {
//        this.username = username;
//        this.parola = parola;
//    }
//
//    public PersoanaOficiu() {
//    }
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    @Column(nullable = false)
//    public String getOras() {
//        return oras;
//    }
//
//    @Column(nullable = false, unique = true)
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    @Column(nullable = false)
//    public String getParola() {
//        return parola;
//    }
//
//    public void setParola(String parola) {
//        this.parola = parola;
//    }
//
//    @Override
//    public String toString() {
//        return "PersoanaOficiu{" +
//                "id=" + id +
//                ", oras='" + oras + '\'' +
//                ", username='" + username + '\'' +
//                ", parola='" + parola + '\'' +
//                '}';
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof PersoanaOficiu that)) return false;
//
//        if (!oras.equals(that.oras)) return false;
//        if (!username.equals(that.username)) return false;
//        return parola.equals(that.parola);
//    }
//
//    @Override
//    public int hashCode() {
//        int result = oras.hashCode();
//        result = 31 * result + username.hashCode();
//        result = 31 * result + parola.hashCode();
//        return result;
//    }
//
//
//}
