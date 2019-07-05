package com.example.springclouduser;

import javax.annotation.sql.DataSourceDefinition;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springclouddatasourceapi.ClientDatasourceRouter;

@ConditionalOnClass({ClientDatasourceRouter.class})
public interface UserRepository extends JpaRepository<User, Long> {

}
