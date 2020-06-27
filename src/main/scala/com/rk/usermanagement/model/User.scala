package com.rk.usermanagement.model

import scala.beans.BeanProperty

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class User extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @BeanProperty
  var id: Long = _

  @Column
  @BeanProperty
  var name: String = _

  @Column
  @BeanProperty
  var email: String = _

  @Column
  var role: String = _

  def setRole(s: Array[String]) {
    role = s.mkString(",")
  }

  def getRole: Array[String] = {
    role.split(",")
  }

  override def toString(): String = {
    s"user [id = ${id}, name = ${name}, email = ${email}, role = ${role}]"
  }
}