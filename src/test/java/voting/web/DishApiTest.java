package voting.web;

import voting.domain.Dish;

public class DishApiTest extends AbstractRestApiPermissionsTest {
    @Override
    protected Dish getNewItem() {
        return new Dish("new dish");
    }

    protected String getCollectionUrl() {
        return "/dishes";
    }

    protected String getItemUrl() {
        return "/dishes/12";
    }
}
