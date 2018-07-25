package com.Dnevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.Dnevnik.entities.Admin;

public interface AdminRep extends CrudRepository<Admin, Integer> {

}
