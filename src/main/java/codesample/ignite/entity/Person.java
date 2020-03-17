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

import com.querydsl.core.annotations.QueryEntity;

@Data
@Entity
//@QueryEntity
@SequenceGenerator(name = "person_seq", sequenceName = "person_seq", allocationSize = 1)
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "name")
public class Person {
    @Id
    @QuerySqlField(index = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_seq")
    private Long id;

    @QuerySqlField(index = true)
    private String name;

	public Long getPid() {
		return id;
	}

	public void setId(Long pid) {
		this.id = pid;
	}

	public String getPname() {
		return name;
	}

	public void setName(String pname) {
		this.name = pname;
	}
	
	/* */
	public String toString() {
		return String.format("id: %d, name: %s\n", id, name);
	}
   
}
