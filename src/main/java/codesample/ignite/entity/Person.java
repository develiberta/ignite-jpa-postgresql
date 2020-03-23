package codesample.ignite.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.cache.query.annotations.QueryTextField;

@Data
@Entity
@SequenceGenerator(name = "person_seq", sequenceName = "person_seq", allocationSize = 1)
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "name")
public class Person {
    @Id
    @QuerySqlField(index = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_seq")
    private Long id;

    @QueryTextField
    @QuerySqlField(index = true)
    private String name;
    
    @QuerySqlField(index = true)
    private Integer age;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}
	
	/* */
	public String toString() {
		return String.format("id: %d, name: %s\n", id, name);
	}
   
}
