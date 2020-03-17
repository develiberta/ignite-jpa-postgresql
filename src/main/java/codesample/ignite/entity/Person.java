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
@EqualsAndHashCode(of = "pname")
public class Person {
    @Id
    @QuerySqlField(index = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_seq")
    private Long pid;

    @QuerySqlField(index = true)
    private String pname;

	public Long getPid() {
		return pid;
	}

	public void setId(Long pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setName(String pname) {
		this.pname = pname;
	}
	
	/* */
	public String toString() {
		return String.format("id: %d, name: %s\n", pid, pname);
	}
   
}
