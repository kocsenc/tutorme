package edu.rit.se.tutorme.api;

/**
 * Enumeration that declares the type of a user.
 */
public enum UserType {
    Student,
    Tutor;

    /**
     * UserType toString method.
     * @return 1 if student, 2 if tutor
     */
    public String toString() {
        switch (this) {
            case Student: return "0";
            case Tutor: return "1";
        }

        return "";
    }
}
