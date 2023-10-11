package com.ncsgroup.shipment.server.configuration;

import com.ncsgroup.shipment.server.constanst.Constants;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import static com.ncsgroup.shipment.server.constanst.Constants.CommonConstants.MESSAGE_SOURCE;

@Configuration
public class MessageSourceConfiguration {

  @Bean
  public MessageSource messageSource() {
    var messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename(MESSAGE_SOURCE);
    messageSource.setDefaultEncoding(Constants.CommonConstants.ENCODING_UTF_8);
    return messageSource;
  }

}
