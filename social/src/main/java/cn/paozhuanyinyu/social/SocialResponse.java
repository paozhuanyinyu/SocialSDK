package cn.paozhuanyinyu.social;

/**
 * Created by kay on 16/12/23.
 */

public abstract class SocialResponse<T> {
    public static final int ACTION_SUCCESS = 20;
    public static final int ACTION_CANCEL = 21;
    public static final int ACTION_FAILED = 22;
    public abstract void onResponse(int code , String msg , T extraData);
}
