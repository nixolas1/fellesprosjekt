package calendar;

public class UserModel {

    private String username = "";
    private String firstName = "";
    private String lastName = "";
    private String phone = "";
    private String password = "";
    private String domain = "";
    private String email = "";

    public UserModel(){};

    public UserModel(String email, String password){
        setEmail(email);
        setPassword(password);
    }

    public UserModel(String email, String password, String username, String domain, String firstName, String lastName, String phone){
        setEmail(email);
        setPassword(password);
        setUsername(username);
        setDomain(domain);
        setFirstName(firstName);
        setLastName(lastName);
        setPhone(phone);
    }

    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getPhone() {
        return phone;
    }
    public String getPassword() {
        return password;
    }
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
        if(!this.username.equals(""))this.email=this.username+this.domain;
    }

    public void setEmail(String email) {
        this.email = email;
        this.username = email.split("@")[0];
        this.domain = email.split("@")[1];
    }

    public void setUsername(String username) {
        this.username = username;
        if(!this.domain.equals(""))this.email=this.username+this.domain;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
