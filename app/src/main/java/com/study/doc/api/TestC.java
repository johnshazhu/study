package com.study.doc.api;

import android.util.Log;
import com.study.lib.api.base.BaseBody;
import com.study.lib.api.base.NetworkBase;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestC {
    public static void test(BaseBody body) {
        APIService service = NetworkBase.Companion.getService(APIService.class);
        if (service != null) {
            service.startup(body)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(startUpDataAPIBaseResponse -> {
                        if (startUpDataAPIBaseResponse != null && startUpDataAPIBaseResponse.isSuccess()) {
                            Log.i("xdebug", "accept rsp msg : " + startUpDataAPIBaseResponse.getMsg());
                        }
                    });
        }
    }

    public static void testFlat() {
        List<Student> studentList = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            List<Course> courseList = new ArrayList<>();
            for (int j = 0; j < 3; ++j) {
                Course course = new Course("course_" + i + "_" + j, new Random().nextInt(100));
                courseList.add(course);
            }
            Student student = new Student("student_" + (i + 1), courseList);
            studentList.add(student);
        }

        ResourceSubscriber<Course> subscriber = new ResourceSubscriber<Course>() {
            @Override
            public void onNext(Course course) {
                Log.i("xdebug", "onNext course name : " + course.name + ", score : " + course.score);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };
        Student[] array = new Student[studentList.size()];
        studentList.toArray(array);
        Disposable disposable = Observable.fromArray(array).flatMap(student -> {
            Course[] courses = new Course[student.courseList.size()];
            student.courseList.toArray(courses);
            return Observable.fromArray(courses);
        }).subscribe(course -> Log.i("xdebug", "course name : " + course.name + ", score : " + course.score));
        if (disposable.isDisposed()) {
            Log.i("xdebug", "isDisposed");
        } else {
            disposable.dispose();
        }
    }

    static class Student {
        private String name;
        private List<Course> courseList;

        public Student(String name, List<Course> courseList) {
            this.name = name;
            this.courseList = courseList;
        }
    }

    static class Course {
        private String name;
        private int score;

        public Course(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }
}
