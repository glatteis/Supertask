package me.glatteis.supertask.items;

/**
 * Created by Linus on 08.01.2016.
 */
public class ItemDatabase {

    public static Item newItem(int index) {
        switch (index) {
            case 0:
                return new SpringShoes();
            default:
                return null;
        }
    }

    public static Item newRandomItem() {
        return newItem(0);
    }

}
