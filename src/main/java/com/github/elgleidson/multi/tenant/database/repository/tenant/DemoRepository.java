package com.github.elgleidson.multi.tenant.database.repository.tenant;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.github.elgleidson.multi.tenant.database.domain.tenant.Demo;

@Repository
public interface DemoRepository extends PagingAndSortingRepository<Demo, Long>, JpaSpecificationExecutor<Demo> {

}
