package ua.nure.kn.yavorovenko.usermanagment;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class User implements Serializable {

    private static final long serialVersionUID = 8331052594370558575L;

    private Long id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;


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
        int ageCounter = 0;

        while (dateOfBirthday.before(today)) {
            dateOfBirthday.add(Calendar.YEAR, 1);
            ageCounter += 1;
        }

        if (today.get(Calendar.DAY_OF_YEAR) > dateOfBirthday.get(Calendar.DAY_OF_YEAR)) {
            ageCounter--;
        }
        return ageCounter;
    }
}
