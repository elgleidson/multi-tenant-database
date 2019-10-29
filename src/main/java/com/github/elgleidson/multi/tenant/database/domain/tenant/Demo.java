package com.github.elgleidson.multi.tenant.database.domain.tenant;

import javax.persistence.*;

import com.github.elgleidson.multi.tenant.database.audit.AbstractAuditableEntity;

@Entity
@Table(name = "demo")
public class Demo extends AbstractAuditableEntity {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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
