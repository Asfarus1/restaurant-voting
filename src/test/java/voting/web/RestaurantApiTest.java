package voting.web;

import voting.domain.Restaurant;

public class RestaurantApiTest extends AbstractRestApiPermissionsTest {
    @Override
    protected Restaurant getNewItem() {
        return new Restaurant("new restaurant");
    }

    protected String getCollectionUrl() {
        return "/restaurants";
    }

    protected String getItemUrl() {
        return "/restaurants/42";
    }
}
