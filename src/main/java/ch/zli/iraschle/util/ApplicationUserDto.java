package ch.zli.iraschle.util;

import ch.zli.iraschle.model.user.ApplicationUserEntity;

import javax.validation.constraints.NotBlank;

import java.util.Collections;

import static ch.zli.iraschle.util.PasswortHashing.hashPassword;

public class ApplicationUserDto {

    private long id;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ApplicationUserEntity toApplicationUser() {
        ApplicationUserEntity applicationUser = new ApplicationUserEntity();
        applicationUser.setId(id);
        applicationUser.setFirstname(firstname);
        applicationUser.setLastname(lastname);
        applicationUser.setEmail(email);
        applicationUser.setPassword(hashPassword(password));
        applicationUser.setBookings(Collections.emptySet());
        return applicationUser;
    }

    public ApplicationUserEntity toNewApplicationUser() {
        ApplicationUserEntity applicationUser = new ApplicationUserEntity();
        applicationUser.setFirstname(firstname);
        applicationUser.setLastname(lastname);
        applicationUser.setEmail(email);
        applicationUser.setPassword(hashPassword(password));
        applicationUser.setBookings(Collections.emptySet());
        return applicationUser;
    }
}
