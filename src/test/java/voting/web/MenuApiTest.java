package voting.web;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class MenuApiTest extends AbstractRestApiPermissionsTest {
    @Override
    protected Map<String, Object> getNewItem() {
        return
                Map.of("date", LocalDate.now(),
                        "items", List.of(Map.of("price", 40, "dish", "/dishes/3")),
                        "restaurant", "/restaurants/42");
    }

    protected String getCollectionUrl() {
        return "/menus";
    }

    protected String getItemUrl() {
        return "/menus/46";
    }
}
