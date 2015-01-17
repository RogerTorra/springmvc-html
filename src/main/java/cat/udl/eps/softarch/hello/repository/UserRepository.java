package cat.udl.eps.softarch.hello.repository;

import cat.udl.eps.softarch.hello.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by roger on 17/01/2015.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(@Param("username") String username);
    User findUserByEmail(@Param("email") String email);
    User findUserById(@Param("id") Long id);
}