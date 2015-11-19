package usp.each.si.ach2006.codesport.models.user;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class User {

	private String id;
	private String firstName;
	private String lastName;
	private String email;
    private Date dob;
    private int age;
    private String gender;

    //-----------------------Facebook--------------------------------//
    private String facebookPictureUrl;
    private String facebookId;
    private String facebookMobileToken;

    private String defaultFacebookPictureUrl = "https://graph.facebook.com/";
    private String defaultFacebookId = "100002560657734"; //Tirar


    //SOMENTE PARA TESTES - RETIRAR NO FINAL
	/*public static boolean getUserById(String id) {
		return JsonUser.getUserById(id);
	}

    public static boolean getUserByFacebookId(String facebookId) {
        return JsonUser.getUserByFacebookId(facebookId);
    }*/

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getFacebookMobileToken() {
        return facebookMobileToken;
    }

    public void setFacebookMobileToken(String facebookMobileToken) {
        this.facebookMobileToken = facebookMobileToken;
    }

    public String getFacebookPictureUrl() {
        return facebookPictureUrl;
    }

    public void setFacebookPictureUrl() {
        this.facebookPictureUrl = defaultFacebookPictureUrl + this.facebookId + "/picture?type=large";
    }


    //-----------------------GooglePlus--------------------------------//

    private String googlePlusId;
    private String googlePlusPictureUrl;
    private String googlePlusMobileToken;

    private String defaultGooglePlusPictureUrl = "https://lh3.googleusercontent.com/-EH91vYdCng4/AAAAAAAAAAI/AAAAAAAAAMw/7XmDfnMZTGM/s120-c/photo.jpg" ;

    /*public static boolean getUserByGoogleId(String facebookId) {
        return JsonUser.getUserByFacebookId(facebookId);
    }*/

    public String getGooglePlusPictureUrl() {
        return googlePlusPictureUrl;
    }

    public void setGooglePlusPictureUrl(String googlePlusPictureUrl) {
        this.googlePlusPictureUrl = googlePlusPictureUrl;
    }

    public String getGooglePlusMobileToken() {
        return googlePlusMobileToken;
    }

    public void setGooglePlusMobileToken(String googlePlusMobileToken) {
        this.googlePlusMobileToken = googlePlusMobileToken;
    }

    public String getGooglePlusId() {
        return googlePlusId;
    }

    public void setGooglePlusId(String googlePlusId) {
        this.googlePlusId = googlePlusId;
    }

    //-----------------------------------------------------------------

	public User() {

	}

    /*public static boolean newUser(String facebookId, String login, String password,
                                  String facebookMobileToken, String firstName, String lastName,
                                  String email, String gender, String dob) {

        //return JsonUser.newUser(facebookId, login, password, facebookMobileToken, firstName,
         //       lastName, email, gender, dob);

    }*/


//--------------------------------------------------------//

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = new Date(dob);
        calculateAge();
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void calculateAge() {
        try {
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
        catch(Exception e ){
            Log.e("Error", "Could not calculate age");
            e.printStackTrace();
        }
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
