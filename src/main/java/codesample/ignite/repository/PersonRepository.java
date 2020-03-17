package codesample.ignite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codesample.ignite.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
