package kdtspring.mybatis.repository.domain;

import org.apache.ibatis.type.Alias;

@Alias("customers") // customers라고 table명을 명시했기때문에 맵핑이 안되므로 Alias를 이용하여 맵핑해줌
public class Customer {
    //yml파일에서 포조 객체는 camel-case로 동작하게 설정하였음
    private long id;
    private String firstName;
    private String lastName;

    public Customer(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

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
