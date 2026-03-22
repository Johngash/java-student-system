package com.university.system.model;

/**
 * Represents a person in the university system.
 * This is a base class for Student and Lecturer.
 */
public abstract class Person {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private boolean isActive;

    public Person() {}

    public Person(int id, String firstName, String lastName, String email, String phone, String address, boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.isActive = isActive;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public String getName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + firstName + " " + lastName + '\'' +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
