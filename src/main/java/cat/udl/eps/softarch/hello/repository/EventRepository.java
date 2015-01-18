package cat.udl.eps.softarch.hello.repository;

import cat.udl.eps.softarch.hello.model.Event;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by http://rhizomik.net/~roberto/
 */

public interface EventRepository extends PagingAndSortingRepository<Event, Long> {

    // PagingAndSortingRepository provides:
    // exists(ID id), delete(T entity), findAll(Pageable), findAll(Sort), findOne(ID id), save(T entity),...
    // http://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html
}
