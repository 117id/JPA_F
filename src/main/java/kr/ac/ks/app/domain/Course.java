package kr.ac.ks.app.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    // LAZY : default 값
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lesson lesson;
//    private List<Lesson> lessons = new ArrayList<>();

//    http://localhost:8080/h2-console
//    yaml 에서  console 의 enabled를 true 설정 요구

    public void setStudent(Student student) {
        this.student = student;
        student.getCourses().add(this);
    }
//  jpa one to many 관계에서 null 발생했을때의 처리.

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
        lesson.getCourses().add(this);
    }

    public static Course createCourse(Student student, Lesson... lessons) {
        Course course = new Course();
        course.setStudent(student);
        Arrays.stream(lessons).forEach(course::setLesson);
        return course;
    }
}

// [Student] -- [Course]
// 관계 : 학생이 수강신청
// 1->N | N -> 1
// db 관계