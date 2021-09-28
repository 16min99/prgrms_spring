package kdt.spring.jpa_crud.repository.domain;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity //JAVA의 객체가 DB테이블과 맵핑이 되는 엔티티 객체
@Table(name = "customers")
public class Customer {

    @Id //pk 명시
    private long id;
    private String firstName;
    private String lastName;

    // default 생성자, getter setter를 통해 동작함

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
