package com.ead.authuser.repositories;

import com.ead.authuser.entity.UserCourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourseEntity, UUID>, JpaSpecificationExecutor<UserCourseEntity> {

}