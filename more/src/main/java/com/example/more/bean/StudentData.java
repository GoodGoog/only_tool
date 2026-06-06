package com.example.more.bean;

import java.util.List;

public class StudentData {

    //Json 转换至类对象
    //1.依次点击Android studio 的标题栏工具选项：Android studio ->> Prefernce ->> Plugins ->>搜索下载JsonFormat
    //2.在类文件中，按快捷键 Alt + Insert (Windows) 或 Command + N (Mac)。
    //选择 GsonFormat，弹出对话框。
    //将 JSON 数据粘贴到对话框中，点击 OK。

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
