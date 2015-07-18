package model.inventory;

public class Category {

    private int categoryId;
    private int departmentId;

    private String name;
    private String description;

    private boolean discounted;
    private CategoryDiscount categoryDiscount;

    public Category(int categoryId, int departmentId, String name, String description, boolean discounted) {
        this.categoryId = categoryId;
        this.departmentId = departmentId;
        this.name = name;
        this.description = description;
        this.discounted = discounted;
        this.categoryDiscount = null;
    }

    /**
     * @return the categoryId
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the departmentId
     */
    public int getDepartmentId() {
        return departmentId;
    }

    /**
     * @param departmentId the departmentId to set
     */
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the discounted
     */
    public boolean isDiscounted() {
        return discounted;
    }

    /**
     * @param discounted the discounted to set
     */
    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
    }

    /**
     * @return the categoryDiscount
     */
    public CategoryDiscount getCategoryDiscount() {
        return categoryDiscount;
    }

    /**
     * @param categoryDiscount the categoryDiscount to set
     */
    public void setCategoryDiscount(CategoryDiscount categoryDiscount) {
        this.categoryDiscount = categoryDiscount;
    }

}
