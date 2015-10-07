package com.su.model;

import java.util.ArrayList;
import java.util.List;

public class PlantListModel {
		private int id;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getCatId() {
			return catId;
		}
		public void setCatId(int catId) {
			this.catId = catId;
		}
		public String getPeriod() {
			return period;
		}
		public void setPeriod(String period) {
			this.period = period;
		}
		public int getDisplay() {
			return display;
		}
		public void setDisplay(int display) {
			this.display = display;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getNameEn() {
			return nameEn;
		}
		public void setNameEn(String nameEn) {
			this.nameEn = nameEn;
		}
		public String getNameAlias() {
			return nameAlias;
		}
		public void setNameAlias(String nameAlias) {
			this.nameAlias = nameAlias;
		}
		public String getKeshu() {
			return keshu;
		}
		public void setKeshu(String keshu) {
			this.keshu = keshu;
		}
		public String getCover() {
			return cover;
		}
		public void setCover(String cover) {
			this.cover = cover;
		}
		public String getXixing() {
			return xixing;
		}
		public void setXixing(String xixing) {
			this.xixing = xixing;
		}
		public String getYongTu() {
			return yongTu;
		}
		public void setYongTu(String yongTu) {
			this.yongTu = yongTu;
		}
		private int catId;
		private String period;
		private int  display;
		private String time;
		private String name;
		private String nameEn;
		private String nameAlias;
		private String keshu;
		private String cover;
		private String xixing;
		private String yongTu;
		private List<String> imageList;
		public List<String> getImageList() {
			return imageList;
		}
		public void setImageList(ArrayList<String> imageList) {
			this.imageList = (List<String>)imageList.clone();
		}



}
