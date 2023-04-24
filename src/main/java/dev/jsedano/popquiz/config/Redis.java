package dev.jsedano.popquiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

@Configuration
public class Redis {

  @Bean
  public JedisPooled getJedisPooled() {
    return new JedisPooled("localhost", 6379);
  }
}
