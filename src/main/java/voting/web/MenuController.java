package voting.web;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import voting.repository.MenuRepository;
import voting.repository.RestaurantRepository;

@RepositoryRestController
public class MenuController {
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    public MenuController(RestaurantRepository restaurantRepository, MenuRepository menuRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
    }

//    @PostMapping(value = "/restaurants/{restaurantId}/update-menu/", consumes = "application/json")
//    public ResponseEntity<?> updateMenu(@PathVariable Long restaurantId, @RequestBody EntityModel<Menu> menu1){
//        System.out.println("restaurantId='" + restaurantId + "'");
//        Menu menu = menu1.getContent();
//        System.out.println("menu='" + menu + "'");
//        menu.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow(()->new IllegalArgumentException("restaurantId")));
//        return ResponseEntity.accepted().body(menuRepository.update(menu));
//    }
}
