package lv.norkudev.financingaggregator.banks.solid;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SolidBankConfiguration {

    @Bean
    @Qualifier("solidBankClient")
    public WebClient solidBankClient(@Value("${banks.solidbank.url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

}
