package codesample.ignite.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.cache.Cache;
import javax.cache.Cache.Entry;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.internal.processors.cache.CacheEntryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import codesample.ignite.cache.PersonCacheStore;
import codesample.ignite.entitry.Person;
import codesample.ignite.repository.PersonRepository;

@RestController
@RequestMapping("/person")
public class PersonController {

	@Autowired
    private Ignite igniteClient;
	
//	@Autowired
//	PersonCacheStore personCacheStore;
	
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
		//IgniteCache<Long, Person> cache = igniteClient.cache("person");
		IgniteCache<Long, Person> cache = igniteClient.getOrCreateCache("person");
		return cache.get(key);
	}
	
	/* Create Cache */
	@PostMapping(path="/cache", produces = "application/json;charset=UTF-8")
	public Person cacheCreate(@RequestBody Person person) {
				 
//		personCacheStore.write(new CacheEntryImpl<>(3L, person));
		
		IgniteCache<Long, Person> cache = igniteClient.getOrCreateCache("person");
//        cache.loadCache(null);
        cache.put(person.getId(), person);
        
		return person;
	}
	
	/* TO-DO : Update Cache */
	
	/* Delete Cache */
	@DeleteMapping(path="/cache/{key}", produces = "application/json;charset=UTF-8")
	public Person cacheDelete(@PathVariable Long key) {

		IgniteCache<Long, Person> cache = igniteClient.getOrCreateCache("person");
//        cache.loadCache(null);
        cache.remove(key);
        
		return null;
	}
	
	/* Query Cache 1 */
	@GetMapping(path="/cachequery/1", produces = "application/json;charset=UTF-8")
	public Map<Long, Person> cacheQuery1() {
		
		IgniteCache<Long, Person> cache = igniteClient.getOrCreateCache("person");

		Set<Long> keys = new TreeSet<>();
		
		cache.query(new ScanQuery<Long, Person>((k, p) -> p.getId() < 10)).forEach(entry -> keys.add((Long) entry.getKey()));
		
		return cache.getAll(keys);
	}
	
	/* Query Cache 2 */
	@GetMapping(path="/cachequery/2", produces = "application/json;charset=UTF-8")
	public Map<Long, Person> cacheQuery2() {
		
		IgniteCache<Long, Person> cache = igniteClient.getOrCreateCache("person");

		// Creating City table.
//		cache.query(new SqlFieldsQuery("CREATE TABLE Person " +
//		    "(id long primary key, name varchar)").setSchema("PUBLIC")).getAll();
		
		SqlFieldsQuery sql = new SqlFieldsQuery("select name from Person where name = 'hi'").setSchema("PUBLIC");
		
		try (QueryCursor<List<?>> cursor = cache.query(sql)) {
			  for (List<?> row : cursor)
			    System.out.println("personName=" + row.get(0));
			}
		
		return null;
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
