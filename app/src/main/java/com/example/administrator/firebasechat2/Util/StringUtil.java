package com.example.administrator.firebasechat2.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017-11-03.
 */

public class StringUtil {

    /**
     * 이메일 유효값 체크
     */
    public static boolean isValidEmail(String email){
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            err = true;
        }
        return err;
    }

    /**
     * 비밀번호 유효값 체크
     */
    public static boolean isValidPassword(String password){
        boolean err = false;
        String regex = "^[a-zA-Z0-9]{6,}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        if (m.matches()) {
            err = true;
        }
        return err;
    }

    /**
     * 2차 비밀번호 확인
     */
    public static boolean isValidSecondPassword(String first, String second){
        return first.equals(second);
    }

    /**
     * 이름 체크
     */
    public static boolean isValidName(String name){

        boolean err = false;
        String regex = "^[가-힣a-zA-Z0-9]{2,12}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(name);
        if (m.matches()) {
            err = true;
        }
        return err;
    }

    /**
     * 이메일 변경
     */
    public static String replaceEmailComma(String email){
        return email.replace(".", "_comma_");
    }

    /**
     * 이메일 변경
     */
    public static String recoverEmailComma(String convertedEmail){
        return convertedEmail.replace("_comma_", ".");
    }

}
