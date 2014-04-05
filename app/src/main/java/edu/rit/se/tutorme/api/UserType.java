package edu.rit.se.tutorme.api;

/**
 * Enumeration that declares the type of a user.
 */
public enum UserType {
    Student,
    Tutor;

    /**
     * Convert String integer to UserType.
     * @param type string integer that represents the UserType
     * @return UserType represented by the passed type
     */
    public static UserType toUserType(String type) {
        switch(type) {
            case "0": return UserType.Student;
            case "1": return UserType.Tutor;
        }

        return null;
    }

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
