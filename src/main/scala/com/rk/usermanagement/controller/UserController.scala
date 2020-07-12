package com.rk.usermanagement.controller

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

import java.util.concurrent.TimeUnit

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.Assert
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import scala.concurrent.duration.FiniteDuration

import com.rk.usermanagement.config.AkkaStreamConfiguration
import com.rk.usermanagement.model.User
import com.rk.usermanagement.service.UserService

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Source
import javax.annotation.PostConstruct

@RestController
class UserController(
  @Autowired private val userService: UserService,

  @Autowired private val system: ActorSystem,

  @Autowired private val materializer: Materializer) {

  val THREAD_SLEEP_TIME: Long = 2000 //2 seconds
  val BACK_PRESSURE_TIME: Long = 2000 //2 seconds

  @PostMapping(path = Array("/add"))
  def addUser(@RequestBody user: User): ResponseEntity[Any] = {

    val future = Source.single(userService.addUser(user)).async.runWith(Sink.head)(materializer)

    var response: ResponseEntity[Any] = null
    future.onComplete {
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

    val future = Source.single(userService.updateUser(user)).async.runWith(Sink.head)(materializer)

    var response: ResponseEntity[Any] = null
    future.onComplete {
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

    val future = Source.single(userService.deleteUser(user)).async.runWith(Sink.ignore)(materializer)

    var response: ResponseEntity[Any] = null
    future.onComplete {
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

    val future = Source.fromIterator(() => userService.getAllUsers().toIterator)
      .backpressureTimeout(FiniteDuration(BACK_PRESSURE_TIME, TimeUnit.SECONDS))
      .runWith(Sink.seq)(materializer)

    var response: ResponseEntity[Any] = null
    future.onComplete {
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

    val future = Source.single(userService.findUser(email)).async.runWith(Sink.head)(materializer)

    var response: ResponseEntity[Any] = null
    future.onComplete {
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

  @PostConstruct
  def setup(): Unit = {
    println(s"Injected ActorSystem Name -> ${system.name}")
    Assert.isTrue(AkkaStreamConfiguration.DEFAULT_ACTORY_SYSTEM_NAME == system.name, "Validating ActorSystem name")
  }
}