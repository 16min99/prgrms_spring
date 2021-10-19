package com.kdt.lecture.domain.order;

import org.aspectj.lang.annotation.Before;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class MyTest {

    private AcademyRepository academyRepository;
    private SubjectRepository subjectRepository;

    @Autowired
    public MyTest(AcademyRepository academyRepository, SubjectRepository subjectRepository) {
        this.academyRepository = academyRepository;
        this.subjectRepository = subjectRepository;
    }

    @Test
    public void setup(){
        Academy academy = new Academy();
        academy.setName("New aca");
        Academy savedAcademy = academyRepository.save(academy);

//        Academy academy2 = new Academy();
//        academy.setName("New aca2");
//        Academy savedAcademy2 = academyRepository.save(academy2);

        Subject subject = new Subject();
        subject.setName("aa");
        subject.setAcademy(savedAcademy);
        subjectRepository.save(subject);

        Subject subject2 = new Subject();
        subject2.setName("aabb");
        subject2.setAcademy(savedAcademy);
        subjectRepository.save(subject);
    }
}
