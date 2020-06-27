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

  def addUser(user: User): Future[User] = {
    Future(userRepository.save(user))
  }

  def updateUser(userToUpdate: User): Future[User] = {
    Future(userRepository.save(userToUpdate))
  }

  def deleteUser(user: User): Future[Unit] = {
    Future(userRepository.delete(user))
  }

  def getAllUsers(): Future[Array[User]] = {
    Future(userRepository.findAll().asScala.toArray)
  }

  def findUser(email: String): Future[User] = {
    Future(userRepository.findByEmail(email))
  }
}