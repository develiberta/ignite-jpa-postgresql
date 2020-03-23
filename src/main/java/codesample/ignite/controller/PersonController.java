package codesample.ignite.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.cache.Cache;
import javax.cache.Cache.Entry;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.Query;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.TextQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import codesample.ignite.entity.Person;
import codesample.ignite.repository.PersonRepository;

@RestController
@RequestMapping("/person")
public class PersonController {

	@Autowired
    private Ignite igniteClient;
	
	@Autowired
	PersonRepository personRepository;
	
	/* Connection Check */
	@GetMapping(produces = "application/json;charset=UTF-8")
	public String connChk() {
		return "hello world!";
	}
	
	/* List of Cache */
	@GetMapping(path="/cache", produces = "application/json;charset=UTF-8")
	public Map<Long, Person> cacheList() {
		
		IgniteCache<Long, Person> cache = igniteClient.getOrCreateCache("person");
		
		Set<Long> keys = new TreeSet<>();
		cache.query(new ScanQuery<>(null)).forEach(entry -> keys.add((Long) entry.getKey()));
		
		return cache.getAll(keys);
	}
	
	/* View of Cache */
	@GetMapping(path="/cache/{key}", produces = "application/json;charset=UTF-8")
	public Person cacheView(@PathVariable Long key) {

		IgniteCache<Long, Person> cache = igniteClient.getOrCreateCache("person");
		return cache.get(key);
	}
	
	/* Create Cache */
	@PostMapping(path="/cache", produces = "application/json;charset=UTF-8")
	public Person cacheCreate(@RequestBody Person person) {
				 
		IgniteCache<Long, Person> cache = igniteClient.getOrCreateCache("person");
//        cache.loadCache(null);
        cache.put(person.getId(), person);
        
		return person;
	}
	
	/* to-do : Update Cache */
	
	/* Delete Cache */
	@DeleteMapping(path="/cache/{key}", produces = "application/json;charset=UTF-8")
	public Person cacheDelete(@PathVariable Long key) {

		IgniteCache<Long, Person> cache = igniteClient.getOrCreateCache("person");
//        cache.loadCache(null);
        cache.remove(key);
        
		return null;
	}
	
	/* Query Cache 1 (predicated-based scan query) */
	@PostMapping(path="/querycache1", produces = "application/json;charset=UTF-8")
	public Map<Long, Person> cacheQuery1(@RequestBody Person person) {
		
		IgniteCache<Long, Person> cache = igniteClient.getOrCreateCache("person");

		Set<Long> keys = new TreeSet<>();
		cache.query(new ScanQuery<Long, Person>((k, p) -> p.getAge() == person.getAge())).forEach(entry -> keys.add((Long) entry.getKey()));
		
		return cache.getAll(keys);
	}
	
	/* Query Cache 2 (SQL query) (index 등 기존 database에 있는 기능을 활용해서 상대적으로 성능 우수) */
	@PostMapping(path="/querycache2", produces = "application/json;charset=UTF-8")
	public List<List<?>> cacheQuery2(@RequestBody Person person) {
		
		IgniteCache<Long, Person> cache = igniteClient.getOrCreateCache("person");

		SqlFieldsQuery sql = new SqlFieldsQuery("select * from person where name = '" + person.getName() + "'");
		
		return cache.query(sql).getAll();
	}
	
	/* Query Cache 3 (text query) */
	@PostMapping(path="/querycache3", produces = "application/json;charset=UTF-8")
	public List<Entry<Long, Person>> cacheQuery3(@RequestBody Person person) {
		
		IgniteCache<Long, Person> cache = igniteClient.getOrCreateCache("person");

		// Query for all people with "person.getName()" in their Name.
		Query<Cache.Entry<Long, Person>> txtQuery = new TextQuery(Person.class, person.getName());
	
		return cache.query(txtQuery).getAll();
	}

	/* List of DB */
	@GetMapping(path="/postgre", produces = "application/json;charset=UTF-8")
	public List<Person> ListPerson() {
		return personRepository.findAll();
	}
	
	/* View of DB */
	@GetMapping(path="/postgre/{id}", produces = "application/json;charset=UTF-8")
	public Person dbView(@PathVariable Long id) {
		return personRepository.findById(id).get();
	}

}
