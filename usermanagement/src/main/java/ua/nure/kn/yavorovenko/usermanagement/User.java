package ua.nure.kn.yavorovenko.usermanagement;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class User implements Serializable {

    private static final long serialVersionUID = 8331052594370558575L;

    private Long id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;

    public User() {
    }

    public User(Long id, String firstName, String lastName, Date dateOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBir) {
        this.dateOfBirth = dateOfBir;
    }

    public String getFullName() {
        return new StringBuilder(getLastName())
                .append(", ")
                .append(getFirstName())
                .toString();
    }

    public int getAge() {
        Calendar dateOfBirthday = Calendar.getInstance();
        dateOfBirthday.setTime(getDateOfBirth());

        Calendar today = Calendar.getInstance();

        if (dateOfBirthday.after(today)) {
            throw new IllegalArgumentException("The age can not be negative!");
        }

        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        int d1 = Integer.parseInt(formatter.format(getDateOfBirth()));
        int d2 = Integer.parseInt(formatter.format(today.getTime()));
        int ageCounter = (d2 - d1) / 10000;

        return ageCounter;
    }
}
