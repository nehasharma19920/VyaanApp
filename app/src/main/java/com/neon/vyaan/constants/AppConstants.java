package com.neon.vyaan.constants;

/**
 * Created by Mayank on 27/04/2016.
 */
public interface AppConstants {


    /*alert type*/
    public static final int ALERT_TYPE_NO_NETWORK = 0x01;
    public static final int ALERT_TYPE_LOGOUT = 0x02;
    public static final int ALERT_TYPE_DELETE_USER = 0x03;

    public static final int ALERT_TYPE_CANCEL_SUBSCRIPTION = 0x04;
    public static final int ALERT_TYPE_CART_ITEM_REMOVE = 0x05;
    public static final int ALERT_TYPE_CONFIRM_ORDER = 0x06;
    public static final int ALERT_TYPE_HOLD_SUBSCRIPTION = 0x07;
    public static final int ALERT_TYPE_UNHOLD_SUBSCRIPTION = 0x08;
    public static final int ALERT_TYPE_CANCEL_TRANSCATION = 0x09;


    /* animation type*/
    public static final int ANIMATION_SLIDE_UP = 0x01;
    public static final int ANIMATION_SLIDE_LEFT = 0x02;


    /* splash screen*/
    public static final int SPLASH_TIME = 3000;


    /* App Tag*/
    public static final String APP_NAME = "Vyaan";

    public static final String IS_LOGIN = "isLogin";

    /* Request Tag*/
    public static final int REQUEST_TAG_NO_RESULT = 0x01;
    public static final int REQUEST_TAG_FORGOT_PASSWORD = 0x02;
    public static final int REQUEST_TAG_SIGN_UP_ACTIVITY = 0x03;
    public static final int REQUEST_TAG_SIGN_IN_ACTIVITY = 0x04;
    public static int REQUEST_TAG_PICK_IMAGE = 0x05;
    public static int REQUEST_TAG_Image_Capture = 0x06;
    public static final int REQUEST_TAG_EDIT_ADDRESS_ACTIVITY = 0x07;
    public static final int REQUEST_TAG_NEW_ADDRESS_ACTIVITY = 0x08;
    public static final int REQUEST_TAG_LOCALITY_ACTIVITY = 0x09;
    public static final int REQUEST_TAG_CITY_ACTIVITY = 0x10;
    public static final int REQUEST_TAG_PLAN_EXPLORE_ACTIVITY = 0x11;
    public static final int REQUEST_TAG_MY_PLANS = 0x12;
    public static final int REQUEST_TAG_NUMBER_VERIFICATION = 0x12;

    /* Request Tag*/
    public static final String BASE_URL_API = "http://vyaandairy.com/Vyaan/UserAppApi/v1/index.php";
    public static final String BASE_URL_IMAGES = "http://vyaandairy.com/Vyaan/UserAppApi/images";
    public static final String URL_LOGIN = BASE_URL_API + "/login";
    public static final String URL_FACEBOOK_LOGIN = BASE_URL_API + "/facebookLogin";
    public static final String URL_GOOGLE_LOGIN = BASE_URL_API + "/googleLogin";
    public static final String URL_SIGNUP = BASE_URL_API + "/signUp";
    public static final String URL_GET_ALL_PRODUCTS = BASE_URL_API + "/getAllProducts";
    public static final String URL_GET_PROFILE = BASE_URL_API + "/getUserDetails";
    public static final String UPDATE_USER_PROFILE = BASE_URL_API + "/updateUserDetails";
    public static final String UPLOAD_PROFILE_PIC_NAME = BASE_URL_API + "/updateProfilePicUrl";
    public static final String URL_CHANGE_PASSWORD = BASE_URL_API + "/changePwd";
    public static final String UPLOAD_PROFILE_IMAGE = BASE_URL_API + "/uploadProfileImage";
    public static final String URL_CURRENT_SUBSCRIPTION = BASE_URL_API + "/getAllCurrentSubscription";
    public static final String URL_CHANGE_SUBSCRIPTION_QUANTITY = BASE_URL_API + "/changeCompleteSubscriptionQuantity";
    public static final String URL_CANCEL_SUBSCRIPTION = BASE_URL_API + "/cancelSubscription";
    public static final String URL_GET_CART_ITEMS = BASE_URL_API + "/getCartItems";
    public static final String URL_UPDATE_CART_ITEMS = BASE_URL_API + "/updateCartItems";
    public static final String URL_REMOVE_ITEMS_FROM_CART = BASE_URL_API + "/removeItemFromCart";
    public static final String URL_MY_ORDERS = BASE_URL_API + "/getAllOrders";
    public static final String URL_MY_BILLS = BASE_URL_API + "/getAllBills";
    public static final String URL_ALL_TRANSACTIONS = BASE_URL_API + "/getTransactionHistory";
    public static final String URL_ALL_ADDRESSES = BASE_URL_API + "/getAllAddress";
    public static final String URL_ALL_CITIES = BASE_URL_API + "/getAllCities";
    public static final String URL_ALL_LOCALITES = BASE_URL_API + "/getAllLocalities";
    public static final String URL_ADD_ADDRESS = BASE_URL_API + "/addAddress";
    public static final String URL_UPDATE_ADDRESS = BASE_URL_API + "/updateAddress";
    public static final String URL_PLACE_ORDER = BASE_URL_API + "/placeOrder";
    public static final String URL_ADD_TO_CART = BASE_URL_API + "/addItemToCart";
    public static final String URL_ADD_SUBSCRIPTION = BASE_URL_API + "/addSubscription";
    public static final String URL_INCREASE_SUBSCRIPTION = BASE_URL_API + "/increaseSubscription";
    public static final String URL_SUBSCRIPTION_DETAILS = BASE_URL_API + "/getSubscriptionDayWiseInformation";
    public static final String URL_HOLD_SUBSCRIPTION = BASE_URL_API + "/holdSubscription";
    public static final String URL_UNHOLD_SUBSCRIPTION = BASE_URL_API + "/unholdSubscription";
    public static final String URL_NO_OF_ITEMS_IN_CART = BASE_URL_API + "/getNumberOfItemInCart";
    public static final String UPDATE_GCM_TOKEN = BASE_URL_API + "/updateGcmToken";
    public static final String URL_PAYMENT_SUCCESS = "http://vyaandairy.com/success.php";
    public static final String URL_PAYMENT_FAIL = "http://vyaandairy.com/fail.php";
    public static final String FORGOT_PASSWORD_URL = BASE_URL_API + "/forgotPassword";
    public static final String URL_DECREASE_SUBSCRIPTION = BASE_URL_API + "/decreaseSubscription";
    public static final String URL_REORDER_SUBSCRIPTION = BASE_URL_API + "/reorderSubscription";

    public static final String RESPONCE_MESSAGE = "message";
    public static final String RESPONCE_ERROR = "error";

    /* FORGOT_PASSWORD_REQUET_KEYS*/
    public static final String KEY_EMAIL = "email";

    public static final String STATUS_CODE = "status_code";

    public static final String RESPONSE_FORGOT_PASSWORD = "Forgot Password Response";
    public static final String ERROR_FORGOT_PASSWORD = "Forgot Password Response Er";

    public static final String KEY_ERROR = "error";
    public static final String KEY_NO_OF_ITEMS = "no_of_items";
    public static final String KEY_USER_IDS = "user_id";
    public static final String KEY_IS_ITEM = "isItem";
    public static final String KEY_IS_USER_LOGIN = "isUserLogin";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_EMAIL = "user_email";

    public static final String DASHBOARD_FRAGMENT_TYPE = "fragmentType";


    public static final String USER_IMAGE = "user_image";
    public static final String USER_PROFILE_NAME = "user_name";


    public static final int MAINBOARD = 0;
    public static final int MY_PLANS = 1;
    public static final int VIEW_ORDERS = 2;
    public static final int VIEW_BILLS = 3;
    //public static final int TRANSACTION_HISTORY = 4;
    public static final int ABOUT_US = 4;
    public static final int CONTACT_US = 5;
    public static final int RATE_US = 6;
    public static final int LOGOUT = 7;


    /* Dashboard*/
    public static final int APP_EXIT_TIME = 2000;
    public static final String MESSAGE = "message";
    public static final String PRODUCT_DETAILS = "productDetails";
    public static final String KEY_PIC_URL = "picUrl";

    public static final int ALERT_TYPE_IMAGE_UPLOAD = 0x05;
    public static final String INVALID_OLD_PWD = "INVALID_OLD_PWD";
    public static final String ADDRESS_MODEL = "ADDRESS_MODEL";
    public static final String CITY_MODEL = "CITY_MODEL";
    public static final String LOCALITY_MODEL = "LOCALITY_MODEL";
    public static final String CART_MODEL_LIST = "CART_MODEL_LIST";
    public static final String ADDRESS_ID = "ADRESS_ID";
    public static final String SUBSCRIPTION_MODEL = "SUBSCRIPTION_MODEL";
    public static final String SUBSCRIPTION = "SUBSCRIPTION";
    public static final String PLAN_EXPLORE = "PLAN_EXPLORE";


    public static String keyName = "name";
    public static String keyEmail = "email";
    public static String keyFbId = "id";
    public static String keyGoogleId = "google_id";
    public static String keyProfilePicUrl = "profilePicUrl";
    public static String KEY_STARTING_ACTIVITY = "startingActivity";
    public static String KEY_PAY_FOR_ORDER= "payForOrder";
    public static String KEY_PAY_FOR_SUBSCRIPTION = "payForSubscription";

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";


    public static final String BASE_GCM_URL = "http://dreamandplay.asia/Gcm";
    public static final String GCM_TOKEN = "GCM_TOKEN";

    public static final String KEY_APPLICATION_NAME = "Vyaan";

    public static final String keyOtp = "OtpIs";
    public static final int highPriority = 2147483647;
    public static final int limitReset = 5;

    public static final String keyMobileNo = "mobileNumber";
    public static final String keyIsNumberVerified = "isNumberVerified";


    public static final String nameSharedPreference = "Vyaan";
    public static final String entityNotPresent = "notPresent";
    public static final String entityNotPresentInt = "0";
    public static final String keyUserNumber = "Phone";

    public static final int REQUEST_TAG_CHANGE_NUMBER = 0x69;
    public static final int REQUEST_TAG_PAYU_PAYMENT_ACTIVITY = 0x90;
    public static final int REQUEST_TAG_PAYU_SUBSCRIPTION_PAYMENT_ACTIVITY = 0x91;

    public static final String eventSmsReceived = "android.provider.Telephony.SMS_RECEIVED";

}
