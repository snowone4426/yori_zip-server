package dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class RecipeObj {
	private String recipe_id;
	private String user_id;
	private String title;
	private String thumbnail;
	private String level;
	private String time;
	private String subtitle;
	private String discription;
	private Date create_at;
	private Date update_at;
	private String state;
	private String star_score;
	private List<Map<String,String>> detail;
	private Map<String,String> category;
	private Map<String,Map<String,String>> ingredient;
	private String reple_count;
	private String num;

	public RecipeObj () {}

  public String getRecipe_id() {
    return recipe_id;
  }
  
  public String getNum() {
    return num;
  }

  public void setNum(String num) {
    this.num = num;
  }

  public String getReple_count() {
    return reple_count;
  }

  public void setReple_count(String reple_count) {
    this.reple_count = reple_count;
  }

  public void setRecipe_id(String recipe_id) {
    this.recipe_id = recipe_id;
  }

  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public String getDiscription() {
    return discription;
  }

  public void setDiscription(String discription) {
    this.discription = discription;
  }

  public Date getCreate_at() {
    return create_at;
  }

  public void setCreate_at(Date create_at) {
    this.create_at = create_at;
  }

  public Date getUpdate_at() {
    return update_at;
  }

  public void setUpdate_at(Date update_at) {
    this.update_at = update_at;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getStar_score() {
    return star_score;
  }

  public void setStar_score(String star_score) {
    this.star_score = star_score;
  }

  public List<Map<String, String>> getDetail() {
    return detail;
  }

  public void setDetail(List<Map<String, String>> detail) {
    this.detail = detail;
  }

  public Map<String, String> getCategory() {
    return category;
  }

  public void setCategory(Map<String, String> category) {
    this.category = category;
  }

  public Map<String, Map<String, String>> getIngredient() {
    return ingredient;
  }

  public void setIngredient(Map<String, Map<String, String>> ingredient) {
    this.ingredient = ingredient;
  }
	
	
}