package menu;

import java.util.ArrayList;
import java.util.List;

public class Menu extends MenuItem{

    private List<MenuItem> menuItems = new ArrayList<>();
    private Menu parent;

    public Menu(String text, Menu parent) {
        super(text);
    }

    public void show(){
        int index = 1;
        System.out.println(text + ":");

        for (MenuItem menuItem : menuItems){
            System.out.println(index + ". " + menuItem.text);
            index++;
        }
    }

    public Menu addMenuItem(MenuItem newItem){
        menuItems.add(newItem);
        if(newItem instanceof Menu){
            ((Menu) newItem).parent = this;
        }
        return this;
    }

    public MenuItem getMenuItem(int selection){
        return menuItems.get(selection);
    }
}
