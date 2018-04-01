package edu.bzu.project.domain;

import java.io.IOException;
import java.io.Serializable;


import edu.bzu.project.utils.ImageUtils;

import android.graphics.Bitmap;

/**
 * ͼƬʵ����
 *
 */
public class ImageItem implements Serializable {
	public String imageId;//id��
	public String thumbnailPath;//����ͼ·��
	public String imagePath;//ͼƬ·��
	private Bitmap bitmap;//����е�ͼƬ
	public boolean isSelected = false;//��־λ
	
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
		if(bitmap == null){//û�д����ѡ��ʱ
			try {
				bitmap = ImageUtils.revitionImageSize(imagePath);//��sd���ж�ȡͼƬ
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
