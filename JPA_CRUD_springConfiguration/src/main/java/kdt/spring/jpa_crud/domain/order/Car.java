package kdt.spring.jpa_crud.domain.order;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
//@DiscriminatorValue("CAR")

public class Car extends Item{
    private int power;
}
