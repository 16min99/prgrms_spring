package com.kdt.lecture.domain.order;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Subject {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    Academy academy ;
}