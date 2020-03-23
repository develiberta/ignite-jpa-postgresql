package codesample.ignite.cache;

import codesample.ignite.entity.Person;
import codesample.ignite.repository.PersonRepository;
import org.apache.ignite.cache.store.CacheStore;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Cache Store implementations shows Ignite how it should work with other storages. Postgresql database in this case
 */
/* CacheStore을 상속받아서 Cache를 읽고 쓸 때, 다른 저장소에도 적용되도록 함 (우리는 Postgresql을 사용할 예정) */
@Component
public class PersonCacheStore implements CacheStore<Long, Person>, BeanFactoryAware {

	/* JPA Repository 를 상속받은 personRepository 를 이용 */
    private static PersonRepository personRepository;

    /* 저장소에 있는 데이터를 모두 불러와서 Cache에 모두 Load */
    @Override
    public void loadCache(IgniteBiInClosure<Long, Person> igniteBiInClosure, @Nullable Object... objects) throws CacheLoaderException {
        List<Person> allPersons = personRepository.findAll();
        allPersons.forEach(
                person -> igniteBiInClosure.apply(person.getId(), person)
        );
    }

    /* 특정 id를 가진 데이터를 불러와서 Cache에 Load */
    @Override
    public Person load(Long id) throws CacheLoaderException {
        Person person = personRepository.findById(id).orElseThrow(() -> new CacheLoaderException("Entry not found"));
        return person;
    }

    /* 특정 id를 가진 데이터를 모두 불러와서 Cache에 모두 Load */
    @Override
    public Map<Long, Person> loadAll(Iterable<? extends Long> ids) throws CacheLoaderException {
        return personRepository.findAllById((Iterable<Long>) ids).stream()
            .collect(Collectors.toMap(Person::getId, person -> person));
    }

    /* Key-Value의 형태로 Cache에 저장할 때 저장소에도 저장 */
    @Override
    public void write(Cache.Entry<? extends Long, ? extends Person> entry) throws CacheWriterException {
        Person person = entry.getValue();
        person.setId(entry.getKey());
        personRepository.save(person);
    }

    /* Key-Value의 형태로 Cache에 모두 저장할 때 저장소에도 모두 저장 */
    @Override
    public void writeAll(Collection<Cache.Entry<? extends Long, ? extends Person>> collection) throws CacheWriterException {
        List<Person> persons = collection.stream().map(entry -> (Person) entry.getValue())
                .collect(Collectors.toList());
        personRepository.saveAll(persons);
    }

    /* Cache에서 삭제할 때 저장소에서도 삭제 */
    @Override
    public void delete(Object person) throws CacheWriterException {
        personRepository.delete((Person) person);
    }

    /* Cache에서 모두 삭제할 때 저장소에서도 모두 삭제 */
    @Override
    public void deleteAll(Collection<?> collection) throws CacheWriterException {
        personRepository.deleteAll((Collection<Person>) collection);
    }

    // 쓰이지 않는 메소드, 형식적인 오버라이딩
    @Override
    public void sessionEnd(boolean b) throws CacheWriterException {
        // deprecated
    }

    // Bean이 쓰이지 않는 Class에서도 Bean을 이 클래스 Bean을 이용하기 위함 */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        PersonCacheStore.personRepository = beanFactory.getBean(PersonRepository.class);
    }
}
