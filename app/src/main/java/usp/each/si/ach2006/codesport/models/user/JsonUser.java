package usp.each.si.ach2006.codesport.models.user;

import org.json.JSONArray;
import org.json.JSONObject;

import usp.each.si.ach2006.codesport.codeUtils.Util;
import usp.each.si.ach2006.codesport.http.HttpUtil;

public class JsonUser {

	private static final String TAG_USERS = "users";
    private static final String TAG_USER = "user";
	private static final String TAG_USER_BY = "user-by-id";

	private static final String TAG_ID = "id";
	private static final String TAG_LOGIN = "login";
	private static final String TAG_PASSWORD = "password";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_FIRST_NAME = "firstname";
	private static final String TAG_LAST_NAME = "lastname";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_DOB = "dob";

    private static final String TAG_FACEBOOK_ID = "facebook";
    private static final String TAG_MOBILE_TOKEN = "mobileToken";
	
	//SOMENTE TESTE - NÃO DEIXAR NO FINAL
	public static boolean getUserById(String id) {
		HttpUtil client = new HttpUtil();

		// MEXER AQUI
		client.AddParam(TAG_USER_BY + TAG_ID, id);

		try {
			client.Execute(HttpUtil.RequestMethod.GET, HttpUtil.getUrlGetUserById());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		//System.out.println(client.getResponse());
		parseJsonUser(Util.fix(client.getResponse()));
		return true;
	}
	
	public static boolean getUserByLoginPassword(String login, String password) {
		HttpUtil client = new HttpUtil();

		// MEXER AQUI
		client.AddParam(TAG_LOGIN, login);
		client.AddParam(TAG_PASSWORD, password);

		try {
			client.Execute(HttpUtil.RequestMethod.GET, HttpUtil.getUrlGetUserByLoginPassword());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}


		System.out.println(client.getResponse());
        parseJsonUser(Util.fix(client.getResponse()));
		return true;
	}

    public static boolean getUserByFacebookId(String facebookId) {
        HttpUtil client = new HttpUtil();

        // MEXER AQUI
        client.AddParam(TAG_FACEBOOK_ID, facebookId);

        try {
            client.Execute(HttpUtil.RequestMethod.POST, HttpUtil.getUrlGetUserByFacebookId());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        System.out.println(client.getResponse());
        //parseJsonUser(client.getResponse());
        return true;
    }

	public static void parseJsonUser(String in) {
		try {
			JSONObject reader = new JSONObject(in);
            JSONArray jsonUsers = reader.getJSONArray(TAG_USERS);

            // looping through All Contacts
            for (int i = 0; i < jsonUsers.length(); i++) {
                JSONObject jsonCurUser = jsonUsers.getJSONObject(i);
                JSONObject jsonUser = jsonCurUser.getJSONObject(TAG_USER);

                System.out.println(jsonUser.toString());

                User.setId(jsonUser.getString(TAG_ID));
                User.setLogin(jsonUser.getString(TAG_LOGIN));
                User.setFirstName(jsonUser.getString(TAG_FIRST_NAME));
                User.setLastName(jsonUser.getString(TAG_LAST_NAME));
                User.setEmail(jsonUser.getString(TAG_EMAIL));
            }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static boolean newUser(String login, String password,
			String firstName, String lastName,
			String email) {

		HttpUtil client = new HttpUtil();

		// MEXER AQUI
		client.AddParam(TAG_LOGIN, login);
		client.AddParam(TAG_PASSWORD, password);
		client.AddParam(TAG_FIRST_NAME, firstName);
		client.AddParam(TAG_LAST_NAME, lastName);
		client.AddParam(TAG_EMAIL, email);


		try {
			client.Execute(HttpUtil.RequestMethod.GET, HttpUtil.getUrlNewUser());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		System.out.println(client.getResponse());
		return true;
		//parseJsonUser(client.getResponse());
	}


    public static boolean newUser(String facebookId, String login, String password,
                                  String mobileToken, String firstName, String lastName,
                                  String email, String gender, String dob) {

        HttpUtil client = new HttpUtil();

        // MEXER AQUI
        client.AddParam(TAG_FACEBOOK_ID, facebookId);
        client.AddParam(TAG_LOGIN, login);
        client.AddParam(TAG_PASSWORD, password);
        client.AddParam(TAG_MOBILE_TOKEN, mobileToken);
        client.AddParam(TAG_FIRST_NAME, firstName);
        client.AddParam(TAG_LAST_NAME, lastName);
        client.AddParam(TAG_GENDER, gender);
        client.AddParam(TAG_EMAIL, email);
        client.AddParam(TAG_DOB, dob);

        //client.AddHeader(TAG_USER, "2");

        try {
            client.Execute(HttpUtil.RequestMethod.GET, HttpUtil.getUrlNewUser());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        System.out.println(client.getResponse());
        return true;
        //parseJsonUser(client.getResponse());
    }
}