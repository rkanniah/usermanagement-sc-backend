package com.rk.usermanagement.controller

import org.assertj.core.api.BDDAssertions

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.util.UriComponentsBuilder

import com.rk.usermanagement.model.User

@RunWith(classOf[SpringRunner])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

  @LocalServerPort
  var port: Int = _

  @Autowired
  var testRestTemplate: TestRestTemplate = _

  def addUser(): Unit = {

    var headers: HttpHeaders = new HttpHeaders
    headers.setContentType(MediaType.APPLICATION_JSON)

    var user = new User {
      {
        setEmail("dagon@opec.com")
        setName("Dagon")
        setRole(Array("user", "superuser"))
      }
    }

    val request = new HttpEntity(user, headers)
    val url = "http://localhost:" + port + "/add"
    var response = testRestTemplate.postForEntity(url, request, classOf[String])

    BDDAssertions.then(response.getStatusCode).isEqualTo(HttpStatus.CREATED)
  }

  def findUserAfterAddUser(): Unit = {

    val builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/find")
      .queryParam("email", "dagon@opec.com")

    val response = testRestTemplate.getForEntity(builder.toUriString(), classOf[User])

    BDDAssertions.then(response.getStatusCode).isEqualTo(HttpStatus.OK)
    Assert.assertTrue(response.getBody.name.equals("Dagon"))

    Assert.assertFalse(response.getBody.role.equals("user,network"))
    Assert.assertTrue(response.getBody.role.equals("user,superuser"))
  }

  def updateUser(): Unit = {

    val headers = new HttpHeaders
    headers.setContentType(MediaType.APPLICATION_JSON)

    var user = new User {
      {
        setId(4L)
        setEmail("dagon@opec.com")
        setName("Dagon")
        setRole(Array("oss", "superuser"))
      }
    }

    val request = new HttpEntity(user, headers)
    val response = testRestTemplate.exchange(
      "http://localhost:" + port + "/update",
      HttpMethod.PUT, request, classOf[User])

    BDDAssertions.then(response.getStatusCode).isEqualTo(HttpStatus.ACCEPTED)
    Assert.assertFalse(response.getBody.role.equals("user,superuser"))
    Assert.assertTrue(response.getBody.role.equals("oss,superuser"))
  }

  def findUserAfterUpdateUser(): Unit = {

    val builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/find")
      .queryParam("email", "dagon@opec.com")

    val response = testRestTemplate.getForEntity(builder.toUriString(), classOf[User])

    BDDAssertions.then(response.getStatusCode).isEqualTo(HttpStatus.OK)
    Assert.assertTrue(response.getBody.name.equals("Dagon"))
    Assert.assertTrue(response.getBody.role.equals("oss,superuser"))
  }

  def deleteUser(): Unit = {

    val headers = new HttpHeaders
    headers.setContentType(MediaType.APPLICATION_JSON)

    var user = new User {
      {
        setId(4L)
        setEmail("dagon@opec.com")
        setName("Dagon")
        setRole(Array("oss", "superuser"))
      }
    }

    val request = new HttpEntity(user, headers)
    val response = testRestTemplate.exchange(
      "http://localhost:" + port + "/delete",
      HttpMethod.DELETE, request, classOf[String])

    BDDAssertions.then(response.getStatusCode).isEqualTo(HttpStatus.ACCEPTED)
  }

  def findUserAfterDeleteUser(): Unit = {

    val builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/find")
      .queryParam("email", "dagon@opec.com")

    val response = testRestTemplate.getForEntity(builder.toUriString(), classOf[String])

    BDDAssertions.then(response.getBody).isNullOrEmpty()

  }

  def getAllUsers(): Unit = {

    val response = testRestTemplate.getForEntity(
      "http://localhost:" + port + "/displayAll",
      classOf[Array[User]])

    BDDAssertions.then(response.getStatusCode()).isEqualTo(HttpStatus.OK)
    Assert.assertFalse(response.getBody.size == 0)
  }

  @Test
  def mainTest(): Unit = {

    addUser()
    findUserAfterAddUser()
    updateUser()
    findUserAfterUpdateUser()
    deleteUser()
    findUserAfterDeleteUser()
    getAllUsers()
  }

}