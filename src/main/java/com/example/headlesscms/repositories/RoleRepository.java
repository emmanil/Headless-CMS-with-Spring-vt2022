package com.example.headlesscms.repositories;

import com.example.headlesscms.models.EnumRole;
import com.example.headlesscms.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

Optional<Role> findByName(EnumRole nameOfRole);
}
