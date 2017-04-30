package com.amazon.aws.demo;

public class CheckableItem {
    
    private boolean checked;
    private String name;
    
    public CheckableItem(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }
    
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public boolean isChecked() {
        return checked;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    
    public void toggle() {
        this.checked = !this.checked;
    }
}