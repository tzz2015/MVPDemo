package com.example.commonlibary.pickerview.takephoto.Model;

import java.io.Serializable;

public class PhotoItem implements Serializable {
	/** serialVersionUID */
    private static final long serialVersionUID = 1L;
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	public boolean isSelected = false;
	public String bucketName;
}
