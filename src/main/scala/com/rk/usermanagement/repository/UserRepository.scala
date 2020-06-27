package com.rk.usermanagement.repository

import org.springframework.data.repository.CrudRepository

import com.rk.usermanagement.model.User

trait UserRepository extends CrudRepository[User, Long] {

  def findByEmail(email: String): User
}