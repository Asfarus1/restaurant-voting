package voting.repository.internal;

import org.springframework.dao.support.DataAccessUtils;
import voting.domain.Menu;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class MenuCustomSaveRepositoryImpl implements MenuCustomSaveRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public <S extends Menu> S save(S menu) {
        TypedQuery<Menu> query = em.createNamedQuery(Menu.BY_DATE_AND_RESTAURANT, Menu.class)
                .setParameter(1, menu.getDate())
                .setParameter(2, menu.getRestaurant());
        Menu origin = DataAccessUtils.singleResult(query.getResultList());
        if (origin == null) {
            em.persist(menu);
        } else {
            origin.setItems(menu.getItems());
            menu = (S) em.merge(origin);
            em.createNamedQuery(Menu.REMOVE_ITEMS).setParameter(1, origin.getId()).executeUpdate();
        }
        S finalMenu = menu;
        menu.getItems().stream().peek(i -> i.setMenu(finalMenu)).forEach(em::persist);
        return menu;
    }
}
