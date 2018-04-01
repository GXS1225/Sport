package edu.bzu.project.domain;

import java.util.List;
/**
 * 图片的文件夹实体类
 *
 */
public class ImageBucket {
	public int count = 0;//文件夹下的图片的数量
	public String bucketName;//文件夹的路径
	public List<ImageItem> imageList;//文件夹下的 图片集合

}
