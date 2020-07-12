package com.rk.usermanagement.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.scheduling.annotation.Async

import com.rk.usermanagement.model.User
import com.rk.usermanagement.repository.UserRepository

import scala.collection.JavaConverters._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

@Service
@Transactional
class UserService(@Autowired private val userRepository: UserRepository) {

  def addUser(user: User): User = {
    userRepository.save(user)
  }

  def updateUser(userToUpdate: User): User = {
    userRepository.save(userToUpdate)
  }

  def deleteUser(user: User): Unit = {
    userRepository.delete(user)
  }

  def getAllUsers(): Array[User] = {
    userRepository.findAll().asScala.toArray
  }

  def findUser(email: String): User = {
    userRepository.findByEmail(email)
  }
}