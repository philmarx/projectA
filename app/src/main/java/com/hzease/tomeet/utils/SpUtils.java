package com.hzease.tomeet.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import com.hzease.tomeet.AppConstants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SpUtils {
    /**
     * SharedPreference名称
     */
    private static final String PREFERENCE_FILE_NAME = AppConstants.TOMMET_SHARED_PREFERENCE;

    public static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 添加String串到SharedPreference??
     *
     * @param context Context
     * @param key
     * @param value
     */
    public static void saveString(Context context, String key,
                                  String value) {
        if (value == null) {
            value = "";
        }
        SharedPreferences preference = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor = preference.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 添加int到SharedPreference??
     *
     * @param context Context
     * @param key     ??
     * @param value   ??
     */
    public static void saveInt(final Context context, String key, int value) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor = preference.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 添加boolean到SharedPreference??
     *
     * @param context Context
     * @param key     ??
     * @param value   ??
     */
    public static void saveBoolean(final Context context, String key,
                                   Boolean value) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 获取String
     *
     * @param context
     * @param key     名称
     * @return 键对应的值，如果找不到对应的值， 则返??"
     */
    public static String getStringValue(final Context context, final String key) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return preference.getString(key, "");
    }

    /**
     * 获取Map
     *
     * @param context
     * @param key     名称
     * @return 键对应的值，如果找不到对应的值， 则返??"
     */
    public static Map<String, String> getStringValues(final Context context, final String key) {
        Map<String, String> map = new HashMap<String, String>();
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        map.put("key", key);
        map.put("value", preference.getString(key, ""));
        return map;
    }

    /**
     * 获取Boolean
     *
     * @param context
     * @param key     名称
     * @return 键对应的值，如果找不到对应的值， 则返回false
     */
    public static boolean getBooleanValue(final Context context,
                                          final String key) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return preference.getBoolean(key, false);
    }

    /**
     * 获取String
     *
     * @param context
     * @param key     名称
     * @return 键对应的值，如果找不到对应的值， 则返??1
     */
    public static int getIntValue(final Context context, final String key) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return preference.getInt(key, 0);
    }

    /**
     * 添加值（String，Boolean，Float，Long）串到SharedPreference??
     *
     * @param context
     * @param key
     * @param value   值（String，Boolean，Float，Long??
     */
    public static void saveOBj(final Context context, final String key,
                               final Object value) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor = preference.edit();
        if (value instanceof String) {
            editor.putString(key, value.toString());
        } else if (value instanceof Integer) {
            editor.putInt(key, ((Integer) value).intValue());
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) value).booleanValue());
        } else if (value instanceof Float) {
            editor.putFloat(key, ((Float) value).floatValue());
        } else if (value instanceof Long) {
            editor.putLong(key, ((Long) value).longValue());
        }
        editor.apply();
    }


    /**
     * 添加long到SharedPreference??
     *
     * @param context Context
     * @param key     ??
     * @param value   ??
     */
    public static void saveLong(final Context context, String key, long value) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor = preference.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * 获取String
     *
     * @param context
     * @param key     名称
     * @return 键对应的值，如果找不到对应的值， 则返0
     */
    public static long getLongValue(final Context context, final String key) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return preference.getLong(key, 0);
    }

    /**
     * 获取集合
     *
     * @return
     */
    public static Set<String> getStringSet(Context context, String key) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        Set<String> set = new HashSet<String>();
        Set<String> str = preference.getStringSet(key, set);
        return str;
    }

    /**
     * 存数集合
     *
     * @param context
     * @param key
     */

    public static void saveSet(final Context context, String key, Set<String> list) {
        SharedPreferences preference = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor = preference.edit();
        editor.putStringSet(key, list);
        editor.apply();
    }

    public static String SceneList2String(List SceneList)
            throws IOException {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(SceneList);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String SceneListString = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close();
        return SceneListString;
    }

    /**
     * 获取object
     *
     * @param context
     * @param key
     * @return
     */
    public static Object getObject(Context context, String key, Object o) {
        Object showSceneList = null;
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String liststr = sharedPreferences.getString(key, "");
        try {
            if (liststr == null || liststr.equals("")) {
                return o;
            } else {
                showSceneList = StringObject(liststr);
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return showSceneList;
    }

    /**
     * 保存object
     *
     * @param context
     * @param object
     */
    public static boolean setObject(Context context, String key, Object object) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = mySharedPreferences.edit();
        try {
            if (object != null) {
                String liststr = SceneObject(object);
                edit.putString(key, liststr);
                edit.apply();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private static Object StringObject(String SceneListString)
            throws StreamCorruptedException, IOException,
            ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        Object SceneList = objectInputStream
                .readObject();
        objectInputStream.close();
        return SceneList;
    }

    private static String SceneObject(Object object)
            throws IOException {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(object);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String SceneListString = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close();
        return SceneListString;
    }

    @SuppressWarnings("unchecked")
    public static List String2SceneList(String SceneListString)
            throws StreamCorruptedException, IOException,
            ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        List SceneList = (List) objectInputStream
                .readObject();
        objectInputStream.close();
        return SceneList;
    }

    /**
     * 保存list集合
     *
     * @param context
     * @param list
     */
    public static boolean setList(Context context, List list, String key) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = mySharedPreferences.edit();
        try {
            if (list != null) {
                String liststr = SceneList2String(list);
                edit.putString(key, liststr);
                edit.apply();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
/*
    *//**
     * 获取list集合
     *
     * @param context
     * @param key
     * @return
     *//*
    public static List getList(Context context, String key) {
        List showSceneList = null;
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String liststr = sharedPreferences.getString(key, "");
        try {
            if (liststr == null || liststr.equals("")) {
                return new ArrayList();
            } else {
                showSceneList = String2SceneList(liststr);
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return showSceneList;
    }*/

    /**
     * 时间戳转日期
     *
     * @param date
     * @return
     */
    public static String getDateTime(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(Long.parseLong(date)));
        return sd;
    }

    // 默认存放文件下载的路径
    public final static String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "dyxp"
            + File.separator + "files" + File.separator;

    /**
     * 保存文件
     *
     * @param ctx
     * @param roleLists
     */
    public static void savaData(Context ctx, String preference, Object roleLists) throws IOException {
        File path = new File(DEFAULT_SAVE_FILE_PATH);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(DEFAULT_SAVE_FILE_PATH, preference);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(roleLists);
        oos.close();
    }

    /**
     * 存储List集合
     * @param context 上下文
     * @param key 存储的键
     * @param list 存储的集合
     */
    public static void putList(Context context, String key, List<? extends Serializable> list) {
        try {
            put(context, key, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取List集合
     * @param context 上下文
     * @param key 键
     * @param <E> 指定泛型
     * @return List集合
     */
    public static <E extends Serializable> List<E> getList(Context context, String key) {
        try {
            return (List<E>) get(context, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**存储对象*/
    private static void put(Context context, String key, Object obj)
            throws IOException
    {
        if (obj == null) {//判断对象是否为空
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream    oos  = null;
        oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        // 将对象放到OutputStream中
        // 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
        baos.close();
        oos.close();

        putString(context, key, objectStr);
    }

    /**
     * 存入字符串
     *
     * @param context 上下文
     * @param key     字符串的键
     * @param value   字符串的值
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences preferences = getSP(context);
        //存入数据
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取字符串
     *
     * @param context 上下文
     * @param key     字符串的键
     * @return 得到的字符串
     */
    public static String getString(Context context, String key) {
        SharedPreferences preferences = getSP(context);
        return preferences.getString(key, "");
    }

    /**
     * 获取字符串
     *
     * @param context 上下文
     * @param key     字符串的键
     * @param value   字符串的默认值
     * @return 得到的字符串
     */
    public static String getString(Context context, String key, String value) {
        SharedPreferences preferences = getSP(context);
        return preferences.getString(key, value);
    }

    /**获取对象*/
    private static Object get(Context context, String key)
            throws IOException, ClassNotFoundException
    {
        String wordBase64 = getString(context, key);
        // 将base64格式字符串还原成byte数组
        if (TextUtils.isEmpty(wordBase64)) { //不可少，否则在下面会报java.io.StreamCorruptedException
            return null;
        }
        byte[]               objBytes = Base64.decode(wordBase64.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais     = new ByteArrayInputStream(objBytes);
        ObjectInputStream    ois      = new ObjectInputStream(bais);
        // 将byte数组转换成product对象
        Object obj = ois.readObject();
        bais.close();
        ois.close();
        return obj;
    }


    /**
     * 保存文件
     *
     * @param ctx
     * @param roleLists
     */
    public static void savaData(Context ctx, String preference, Object roleLists, String way) throws IOException {
        File path = new File(way);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(way, preference);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(roleLists);
        oos.close();
    }

    /**
     * 获取文件
     *
     * @param ctx
     * @return
     */
    public static Object getData(Context ctx, String preference, Object object) throws IOException, ClassNotFoundException {
        File file = new File(DEFAULT_SAVE_FILE_PATH, preference);
        if (file != null && file.exists()) {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
            object = is.readObject();
            is.close();
        }
        return object;
    }

    /**
     * 获取文件
     *
     * @param ctx
     * @return
     */
    public static Object getData(Context ctx, String preference, Object object, String way) throws IOException, ClassNotFoundException {
        File file = new File(way, preference);
        if (file != null && file.exists()) {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
            object = is.readObject();
            is.close();
        }
        return object;
    }
}
