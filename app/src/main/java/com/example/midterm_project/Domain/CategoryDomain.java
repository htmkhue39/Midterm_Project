package com.example.midterm_project.Domain;

public class CategoryDomain {
    private String title;
    private int pic;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public CategoryDomain(String title, int pic){
        this.title = title;
        this.pic = pic;
    }
}
