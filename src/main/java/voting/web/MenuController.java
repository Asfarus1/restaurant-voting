package voting.web;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import voting.domain.Menu;

import javax.validation.Valid;

@RepositoryRestController
public class MenuController {

//    @PostMapping("/restaurants/{restaurantId}/menus/")
//    public ResponseEntity<?> updateMenu(@PathVariable Long restaurantId, @Valid Menu menu){
//
//    }
}
