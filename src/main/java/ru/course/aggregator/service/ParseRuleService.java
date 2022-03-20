package ru.course.aggregator.service;

import org.springframework.stereotype.Service;
import ru.course.aggregator.domain.ParseRule;
import ru.course.aggregator.repository.ParseRuleRepository;

@Service
public class ParseRuleService {

    private final ParseRuleRepository parseRuleRepository;

    public ParseRuleService(ParseRuleRepository parseRuleRepository) {
        this.parseRuleRepository = parseRuleRepository;
    }

    public ParseRule findById(Long ruleId) {
        return parseRuleRepository.findById(ruleId).orElse(null);
    }
}
