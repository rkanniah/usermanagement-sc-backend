package com.rk.usermanagement.controller

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import com.rk.usermanagement.model.User
import com.rk.usermanagement.service.UserService

@RestController
class UserController(@Autowired private val userService: UserService) {

  val THREAD_SLEEP_TIME: Long = 2000 //2 seconds

  @PostMapping(path = Array("/add"))
  def addUser(@RequestBody user: User): ResponseEntity[Any] = {

    val newUser = userService.addUser(user)

    var response: ResponseEntity[Any] = null
    newUser.onComplete {
      case Success(value) => {
        response = ResponseEntity.status(HttpStatus.CREATED).body(value)
      }
      case Failure(e) => {
        response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)
      }
    }

    Thread.sleep(THREAD_SLEEP_TIME)
    response
  }

  @PutMapping(path = Array("/update"))
  def updateUser(@RequestBody user: User): ResponseEntity[Any] = {
    val updatedUser = userService.updateUser(user)

    var response: ResponseEntity[Any] = null
    updatedUser.onComplete {
      case Success(value) => {
        response = ResponseEntity.status(HttpStatus.ACCEPTED).body(value)
      }
      case Failure(e) => {
        response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)
      }
    }

    Thread.sleep(THREAD_SLEEP_TIME)
    response
  }

  @DeleteMapping(path = Array("/delete"))
  def deleteUser(@RequestBody user: User): ResponseEntity[Any] = {

    val deleteOperation: Future[Unit] = userService.deleteUser(user)

    var response: ResponseEntity[Any] = null
    deleteOperation.onComplete {
      case Success(value) => {
        response = ResponseEntity.status(HttpStatus.ACCEPTED).body(null)
      }
      case Failure(e) => {
        response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)
      }
    }

    Thread.sleep(THREAD_SLEEP_TIME)
    response
  }

  @GetMapping(path = Array("/displayAll"))
  def getAllUsers(): ResponseEntity[Any] = {

    val users = userService.getAllUsers()

    var response: ResponseEntity[Any] = null
    users.onComplete {
      case Success(value) => {
        response = ResponseEntity.status(HttpStatus.OK).body(value.toArray)
      }
      case Failure(e) => {
        response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)
      }
    }

    Thread.sleep(THREAD_SLEEP_TIME)
    response
  }

  @GetMapping(path = Array("/find"))
  def findUser(@RequestParam(name = "email") email:String): ResponseEntity[Any] = {

    val user = userService.findUser(email)

    var response: ResponseEntity[Any] = null
    user.onComplete {
      case Success(value) => {
        response = ResponseEntity.status(HttpStatus.OK).body(value)
      }
      case Failure(e) => {
        response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e)
      }
    }

    Thread.sleep(THREAD_SLEEP_TIME)
    response
  }
}