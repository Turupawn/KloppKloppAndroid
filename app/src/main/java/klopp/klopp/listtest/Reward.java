package klopp.klopp.listtest;

/**
 * Created by turupawn on 1/19/17.
 */
public class Reward {
    int id;
    String name;
    int business_id;
    int klopps;
    String image_url;
    boolean is_active;

    Reward(int id,
            String name,
            int business_id,
            int klopps,
            String image_url,
            boolean is_active)
    {
        this.id = id;
        this.name = name;
        this.business_id = business_id;
        this.klopps = klopps;
        this.image_url = image_url;
        this.is_active = is_active;
    }
}
