package client.newUser;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserModel {

    private StringProperty usernameProperty = new SimpleStringProperty();
    private StringProperty firstNameProperty = new SimpleStringProperty();
    private StringProperty lastNameProperty = new SimpleStringProperty();
    private StringProperty phoneProperty = new SimpleStringProperty();
    private StringProperty passwordProperty = new SimpleStringProperty();
    private StringProperty domainProperty = new SimpleStringProperty();

    public Object getBean() {
        return this;
    }


    public String getUsername() {
        return usernameProperty.getValue();
    }
    public String getFirstName() {
        return firstNameProperty.getValue();
    }
    public String getLastName() {
        return lastNameProperty.getValue();
    }
    public String getPhone() {
        return phoneProperty.getValue();
    }
    public String getPassword() {
        return passwordProperty.getValue();
    }
    public String getDomain() {
        return domainProperty.getValue();
    }
    public void setDomain(String domain) {
        this.domainProperty.setValue(domain);
    }

    public void setUsername(String username) {
        this.usernameProperty.setValue(username);
    }

    public void setFirstName(String firstName) {
        this.firstNameProperty.setValue(firstName);
    }

    public void setLastName(String lastName) {
        this.lastNameProperty.setValue(lastName);
    }

    public void setPhone(String phone) {
        this.phoneProperty.setValue(phone);
    }

    public void setPassword(String password) {
        this.phoneProperty.setValue(password);
    }

}
