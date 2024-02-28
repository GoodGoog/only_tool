package com.example.more.bean;

import java.util.List;

public class StudentData {

    //{student : [{id:1,name:小明,sex:男,age:18,height:175},{id:2,name:小红,sex:女,age:19,height:165},{id:3,name:小强,sex:男,age:20,height:185}]}

    private List<StudentBean> student;

    public List<StudentBean> getStudent() {
        return student;
    }

    public void setStudent(List<StudentBean> student) {
        this.student = student;
    }

    public static class StudentBean {
        /**
         * id : 1
         * name : 小明
         * sex : 男
         * age : 18
         * height : 175
         */

        private int id;
        private String name;
        private String sex;
        private int age;
        private int height;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
