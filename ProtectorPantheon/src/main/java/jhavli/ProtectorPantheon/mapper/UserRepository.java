package jhavli.ProtectorPantheon.mapper;


import jhavli.ProtectorPantheon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
