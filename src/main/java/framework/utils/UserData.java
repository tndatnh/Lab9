package framework.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserData {

    // @JsonProperty: map đúng tên key trong JSON sang field Java
    @JsonProperty("username")
    public String username;

    @JsonProperty("password")
    public String password;

    @JsonProperty("role")
    public String role;

    @JsonProperty("expectSuccess")
    public boolean expectSuccess;

    @JsonProperty("description")
    public String description;

    // toString() để dễ debug — hiện thông tin khi in ra console
    @Override
    public String toString() {
        return String.format("UserData{username='%s', role='%s', expectSuccess=%b}",
                username, role, expectSuccess);
    }
}