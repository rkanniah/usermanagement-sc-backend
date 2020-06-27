package com.rk.usermanagement

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class UserManagementApp

object UserManagementApp extends App {
  SpringApplication.run(classOf[UserManagementApp])
}