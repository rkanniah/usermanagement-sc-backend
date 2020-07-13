package com.rk.usermanagement.config

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.Materializer
import akka.stream.alpakka.spring.web.AkkaStreamsRegistrar

import org.springframework.core.ReactiveAdapterRegistry

import org.springframework.context.annotation.Configuration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import akka.stream.scaladsl.Source

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

@Configuration
@ConditionalOnClass(name = Array("akka.stream.scaladsl.Source"))
class AkkaStreamConfiguration {

  val registry = ReactiveAdapterRegistry.getSharedInstance()
  val system = ActorSystem.create(AkkaStreamConfiguration.DEFAULT_ACTORY_SYSTEM_NAME)
  val materializer = ActorMaterializer.create(system)

  new AkkaStreamsRegistrar(materializer).registerAdapters(registry)

  @Bean(destroyMethod = "terminate")
  @ConditionalOnMissingBean(name = Array("getActorSystem"))
  def getActorSystem(): ActorSystem = {
    system
  }

  @Bean
  @ConditionalOnMissingBean(name = Array("getMaterializer"))
  def getMaterializer(): ActorMaterializer = {
    materializer
  }
}

object AkkaStreamConfiguration {
  val DEFAULT_ACTORY_SYSTEM_NAME = "UserManagementApp-ActorSystem"
}