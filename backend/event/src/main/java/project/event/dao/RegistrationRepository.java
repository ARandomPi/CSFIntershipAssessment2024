package project.event.dao;

import project.event.model.Registration;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RegistrationRepository extends CrudRepository<Registration, Integer>{
    public Registration findRegistrationByRegistrationId(int registrationId);
    public List<Registration> findAll();
}
