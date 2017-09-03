package com.sens.baseapplication.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zl on 2014/12/2.
 */
public class StringUtil {
    /**
     * 判断给定字符串是否是数字。
     *
     * @return boolean
     */
    public static boolean isNumeric(String str) {
        if (str == null) return false;
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static boolean isInteger(String str) {
        if (str == null) return false;
        Pattern pattern = Pattern.compile("^[1-9]\\d+$");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static boolean isFloat(String str) {
        return !str.isEmpty() && str.matches("^\\d+(\\.\\d+)?$");
    }

    /**
     * 判断给定字符串是否空白串。
     *
     * @param input 字符串
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        return input == null || 0 == TextUtils.getTrimmedLength(input);
    }

    /**
     * 特殊符号转译
     */
    public static String replaceSpechars(String str) {
        if (null != str) {
            str = str.replace("&", "&amp;");
            str = str.replace("<", "&lt;");
            str = str.replace(">", "&gt;");
            return str;
        } else {
            return null;
        }
    }

    /**
     * 16进制字符转byte数组 并检验合法性 含有非十六进制会返回null
     */
    public static byte[] hexToByteArray(String messagePart) {
        if (messagePart == null) return null;
        String message = messagePart.replaceAll(" ", "").toUpperCase();
        byte[] messageBytes = new byte[message.length() / 2];
        for (int i = 0; i < message.length(); i++) {
            //与'0'和'9'比较，不是0,9.
            char c = message.charAt(i);
            if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'F')) {
                return null;
            }
        }
        if ((message.length() & 1) == 1) return null;
        int length = message.length() >> 1;
        for (int i = 0; i < length; i++) {
            messageBytes[i] = (byte) Integer.parseInt(message.substring(i * 2, i * 2 + 2), 16);
        }
        return messageBytes;
    }

    /**
     * byte数组转16进制字符
     */
    public static String byteArrayToHex(byte[] bytes) {
        if (bytes == null) return "";
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String bs = String.format("%02X", b);
            sb.append(bs);
        }
        return sb.toString();
    }

    /**
     * byte数组转ASCII字符
     */
    @SuppressLint("DefaultLocale")
    public static String byteArrayToASCII(byte[] array) {
        if (array == null) return "";
        StringBuilder sb = new StringBuilder();
        for (byte byteChar : array) {
            if (byteChar >= 32 && byteChar < 127) {
                sb.append(String.format("%c", byteChar));
            } else {
                sb.append(String.format("%d ", byteChar & 0xFF));
            }
        }
        return sb.toString();
    }

    /**
     * 是否包含中文
     */
    public static boolean isContainsChinese(String str) {
        if (str == null || str.length() == 0) return false;
        String regex = "[\u4e00-\u9fa5]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public static boolean isChineseName(String str) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA5]{2,4}$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 6~12位字母或数字
     */
    public static boolean isValidUserName(String paramString) {
        if (paramString == null) return false;
        String regex = "^[a-z0-9A-Z]{6,12}$";
        return paramString.matches(regex);
    }

    /**
     * 检验密码是否合法
     */
    public static boolean isPassword(String password) {
        if (isEmpty(password) || password.length() < 6) return false;
        String regex = "^[a-z0-9A-Z]{6,12}$";//
        return password.matches(regex);
    }

    /**
     * 检查昵称是否合法
     */
    public static boolean isValidNickName(String nickname) {
        return isContainsChinese(nickname) && nickname.length() < 8;
    }

}
