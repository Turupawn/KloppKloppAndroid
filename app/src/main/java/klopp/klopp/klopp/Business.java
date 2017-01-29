package klopp.klopp.klopp;

/**
 * Created by turupawn on 1/19/17.
 */
public class Business {
    int id;
    String name;
    String description;
    String image_url;
    int current_user_klopps;

    public Business(int id, String name, String description, String image_url, int current_user_klopps)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image_url = image_url;
        this.current_user_klopps = current_user_klopps;
    }
}
