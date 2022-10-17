package sit.int221.integratedprojectbe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.int221.integratedprojectbe.entities.File;

@Repository
public interface FileRepository extends JpaRepository<File, String> {

}
