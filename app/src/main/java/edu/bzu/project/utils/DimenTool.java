package edu.bzu.project.utils;

/**
 * ������֤
 * 
 */
import android.content.Context;
/**
 * ��λת������
 *
 */
public class DimenTool {
	/**
	 * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	 
	/**
	 * �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	
	  //��pixelת����sp  
    public static int pixelToSp(Context context, float pixelValue) {  
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;  
        int sp=(int) (pixelValue / scaledDensity + 0.5f);  
        return sp;  
    }  
  
    //��spת����pixel  
    public static int spToPixel(Context context, float spValue) {  
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;  
        int pixelValue=(int) (spValue * scaledDensity + 0.5f);  
        return pixelValue;  
    }  

	/**
	 * ��ȡ�ֻ��Ŀ�(����)
	 */
	public static int getWidthPx(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}
	/**
	 * ��ȡ�ֻ��ĸ�(����)
	 */
	public static int getHeightPx(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}
	
}
