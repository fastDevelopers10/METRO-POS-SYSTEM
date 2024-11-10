package Controller;

public class SuperAdmin
{
    private static int idCounter = 1;  //count for increment in id over each object creation
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;

    public SuperAdmin(String name, String address, String phoneNumber, String email, String username, String password) {
        this.id = idCounter++;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
