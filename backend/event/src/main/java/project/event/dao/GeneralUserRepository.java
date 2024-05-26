package project.event.dao;

import org.springframework.data.repository.CrudRepository;
import project.event.model.GeneralUser;

import java.util.List;

public interface GeneralUserRepository extends CrudRepository<GeneralUser, Integer> {
    GeneralUser findGeneralUserByGeneralUserId(int generalUserId);
    List<GeneralUser> findAll();
}
