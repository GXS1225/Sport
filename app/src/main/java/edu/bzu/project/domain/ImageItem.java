package edu.bzu.project.domain;

import java.io.IOException;
import java.io.Serializable;


import edu.bzu.project.utils.ImageUtils;

import android.graphics.Bitmap;

/**
 * 图片实体类
 *
 */
public class ImageItem implements Serializable {
	public String imageId;//id号
	public String thumbnailPath;//缩略图路径
	public String imagePath;//图片路径
	private Bitmap bitmap;//相册中的图片
	public boolean isSelected = false;//标志位
	
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getThumbnailPath() {
		return thumbnailPath;
	}
	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public Bitmap getBitmap() {		
		if(bitmap == null){//没有从相册选择时
			try {
				bitmap = ImageUtils.revitionImageSize(imagePath);//从sd卡中读取图片
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	
	
}
