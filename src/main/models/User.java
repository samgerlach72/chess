package models;

import java.util.Objects;

public class User {
    private String username;
    private String password;
    private String email;
    public User(String username, String password, String email){}
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email);
    }
    @Override
    public int hashCode() {
        return Objects.hash(username, password, email);
    }
}
