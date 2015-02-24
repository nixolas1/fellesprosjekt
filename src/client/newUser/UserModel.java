package client.newUser;


import javafx.beans.property.StringProperty;

public class UserModel {

    private StringProperty username;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty phone;
    private StringProperty password;
    private StringProperty domain;


    public String getUsername() {
        return username.getValue();
    }
    public String getFirstName() {
        return firstName.getValue();
    }
    public String getLastName() {
        return lastName.getValue();
    }
    public String getPhone() {
        return phone.getValue();
    }
    public String getPassword() {
        return password.getValue();
    }
    public String getDomain() {
        return domain.getValue();
    }
    public void setDomain(String domain) {
        this.domain.setValue(domain);
    }

    public void setUsername(String username) {
        this.username.setValue(username);
    }

    public void setFirstName(String firstName) {
        this.firstName.setValue(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName.setValue(lastName);
    }

    public void setPhone(String phone) {
        this.phone.setValue(phone);
    }

    public void setPassword(String password) {
        this.phone.setValue(password);
    }

}
