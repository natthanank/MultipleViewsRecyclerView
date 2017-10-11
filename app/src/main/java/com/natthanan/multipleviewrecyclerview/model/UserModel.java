package com.natthanan.multipleviewrecyclerview.model;

/**
 * Created by natthanan on 9/22/2017.
 */

public class UserModel {
    private String name;
    private String surname;
    private int age;
    private UsernamePassword usernamePassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UsernamePassword getUsernamePassword() {
        return usernamePassword;
    }

    public void setUsernamePassword(UsernamePassword usernamePassword) {
        this.usernamePassword = usernamePassword;
    }

    public static class UsernamePassword {

        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
