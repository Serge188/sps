package ru.sps.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sps.model.MessengerUser;

@Repository
public interface MessengerUsersRepository extends CrudRepository<MessengerUser, Long>, JpaSpecificationExecutor<MessengerUser> {
}
