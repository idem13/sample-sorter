package com.recruitment.task.samplesorter.config;

import com.recruitment.task.samplesorter.service.DefaultPolicyChecker;
import com.recruitment.task.samplesorter.service.policies.NoPatientOfTheSameAgePolicy;
import com.recruitment.task.samplesorter.service.policies.NoPatientOfTheSameCityDistrictPolicy;
import com.recruitment.task.samplesorter.service.policies.NoPatientOfTheSameCompanyPolicy;
import com.recruitment.task.samplesorter.service.policies.NoPatientOfTheSameVisionDefectPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class ApplicationConfig {

    @Bean
    public DefaultPolicyChecker defaultPolicyChecker() {
        return new DefaultPolicyChecker(Set.of(
                new NoPatientOfTheSameAgePolicy(),
                new NoPatientOfTheSameCompanyPolicy(),
                new NoPatientOfTheSameCityDistrictPolicy(),
                new NoPatientOfTheSameVisionDefectPolicy()
        ));
    }
}
