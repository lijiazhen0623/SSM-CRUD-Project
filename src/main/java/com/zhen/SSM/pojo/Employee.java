package com.zhen.SSM.pojo;
import javax.validation.constraints.Pattern;

public class Employee {
    private Integer id;
    //数据校验
    @Pattern(regexp = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFFa-zA-Z0-9_-]{2,5})",
            message = "用户名格式错误，用户名是否由3-6字母数字下划线组成或者由2-5个汉字组成")
    private String name;
    //m-男  f-女
    private String gender;
//    @Email  可以用这个注解进行校验，或者用@pattern自定义校验标准
    @Pattern(regexp = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$",
            message = "邮箱格式错误")
    private String email;

    private Integer dId;

    private Department department;

    public Employee() {
    }

    public Employee(Integer id, String name, String gender, String email, Integer dId) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.dId = dId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getdId() {
        return dId;
    }

    public void setdId(Integer dId) {
        this.dId = dId;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", dId=" + dId +
                ", department=" + department +
                '}';
    }
}