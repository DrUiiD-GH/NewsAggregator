package ru.course.aggregator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.course.aggregator.domain.ParseRule;

public interface ParseRuleRepository extends JpaRepository<ParseRule, Long> {
}
