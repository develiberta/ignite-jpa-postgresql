package codesample.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import codesample.ignite.entity.Person;

@SpringBootApplication
public class SpringbootIgnitePostgresRunner {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootIgnitePostgresRunner.class, args);
    }

    @Bean
    public CommandLineRunner runAtStart() {
        return new CommandLineRunner() {

            @Autowired
            private Ignite igniteClient;

            public void run(String... args)  {
                runIgnite(igniteClient);
            }
        };
    }

    private void runIgnite(Ignite igniteClient) {
//        IgniteCache<Long, Person> cache = igniteClient.getOrCreateCache("person");
//        cache.loadCache(null);
//        Person p = cache.get(1L);
//        System.out.println("get person 1: " + p.getId() + " name: " + p.getName());
//        p = cache.get(2L);
//        System.out.println("get person 2: " + p.getId() + " name: " + p.getName());
    }
}
