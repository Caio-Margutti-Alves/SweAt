package usp.each.si.ach2006.codesport.models.user;

import java.util.Calendar;
import java.util.Date;

public abstract class User {

	private static String id;
	private static String login;
    private static String password;
	private static String firstName;
	private static String lastName;
	private static String email;
    private static Date dob;
    private static int age;


    private static String facebookId = "100002560657734"; //Tirar
    private static String mobileToken;


	public User() {

	}

    public static boolean newUser(String facebookId, String login, String password,
                                  String mobileToken, String firstName, String lastName,
                                  String email, String gender, String dob) {

        return JsonUser.newUser(facebookId, login, password, mobileToken, firstName,
                lastName, email, gender, dob);

    }
	
	//SOMENTE PARA TESTES - RETIRAR NO FINAL
	public static boolean getUserById(String id) {
		return JsonUser.getUserById(id);
	}

    public static boolean getUserByFacebookId(String facebookId) {
        return JsonUser.getUserByFacebookId(facebookId);
    }

    public static boolean getUserByLoginPassword(String login, String password) {
        return JsonUser.getUserByLoginPassword(login, password);

    }

    //--------------------------------------------------------//

    public static String getFacebookId() {
        return facebookId;
    }

    public static void setFacebookId(String facebookId) {
        User.facebookId = facebookId;
    }

    public static String getMobileToken() {
        return mobileToken;
    }

    public static String getEmail() {
		return email;
	}

	public static void setEmail(String email) {
		User.email = email;
	}

	public static String getLogin() {
		return login;
	}

	public static void setLogin(String login) {
		User.login = login;
	}
	
	public static String getId() {
		return id;
	}

	public static void setId(String id) {
		User.id = id;
	}

	public static String getFirstName() {
		return firstName;
	}

	public static void setFirstName(String firstName) {
		User.firstName = firstName;
	}

	public static String getLastName() {
		return lastName;
	}

	public static void setLastName(String lastName) {
		User.lastName = lastName;
	}

    public static Date getDob() {
        return dob;
    }

    public static void setDob(String dob) {
        User.dob = new Date(dob);
        calculateAge();
    }

    public static int getAge() {
        return age;
    }

    public static void setAge(int age) {
        User.age = age;
    }

    public static void calculateAge() {

        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        int age = 0;

        birthDate.setTime(getDob());
        if (birthDate.after(today)) {
            throw new IllegalArgumentException("Can't be born in the future");
        }

        age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        // If birth date is greater than todays date (after 2 days adjustment of
        // leap year) then decrement age one year
        if ((birthDate.get(Calendar.DAY_OF_YEAR)
                - today.get(Calendar.DAY_OF_YEAR) > 3)
                || (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH))) {
            age--;

            // If birth date and todays date are of same month and birth day of
            // month is greater than todays day of month then decrement age
        } else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH))
                && (birthDate.get(Calendar.DAY_OF_MONTH) > today
                .get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        setAge(age);
    }


//    public static boolean updateUser(String id, String login, String password,
//                                  String firstName, String lastName,
//                                  String email) {
//
//        return JsonUser.updateUser(id, login, password, firstName,
//                lastName, email);
//
//    }

}
