package com.github.elgleidson.multi.tenant.database.domain.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.github.elgleidson.multi.tenant.database.audit.AbstractAuditableEntity;

@Entity
public class Demo extends AbstractAuditableEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sq_demo")
	@SequenceGenerator(name="sq_demo", sequenceName="sq_demo", allocationSize=1)
    private Long id;

    @Column
    private String description;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	@Override
	public String toString() {
		return "Demo [id=" + id + ", description=" + description + "]";
	}
	
}
