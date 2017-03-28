package com.customview.kang.facebooksdk;

import android.util.Log;

/**
 * @author namkyu
 * @version 1.0
 * @since 2017
 *
 */

public class Logger {
    public final static boolean DEBUG_MODE = true;

    //BuildConfig.DEBUG;-요거는 동작이 제대로 안되서 강제로 세팅했다 true  - 디버그 모드로 빌드, false - 릴리즈모드로 빌드
    /**디버그 모드일 때 로그 내용을 콘솔에 출력
     *
     * @param str
     * @param className
     */
    public static void print(String className, String str){
        if(DEBUG_MODE)  //디버그 모드일때만 로그 찍히게 할라고 사용한다.
            Log.d(className,str);

        //로그내용을 로그파일에 저장...
        //File.append...():
    }
}
