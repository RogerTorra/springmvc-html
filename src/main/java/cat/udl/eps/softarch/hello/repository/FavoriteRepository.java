package cat.udl.eps.softarch.hello.repository;

import cat.udl.eps.softarch.hello.model.Favorite;
import cat.udl.eps.softarch.hello.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by http://rhizomik.net/~roberto/
 */

public interface FavoriteRepository extends PagingAndSortingRepository<Favorite, Long> {

    Iterable<Favorite> findFavoritesByUser(@Param("user") User user);

    // PagingAndSortingRepository provides:
    // exists(ID id), delete(T entity), findAll(Pageable), findAll(Sort), findOne(ID id), save(T entity),...
    // http://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html
}
