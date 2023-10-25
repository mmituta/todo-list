package com.example.todo.controller.validation;

import com.example.todo.items.controller.validation.NullOrNotBlankValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class NullOrNotBlankValidatorTest {

    private NullOrNotBlankValidator validator;

    @BeforeEach
    void setUpValidator(){
        this.validator = new NullOrNotBlankValidator();
    }

    @Test
    void shouldBeValidIfValueIsNull(){
        assertThat(this.validator.isValid(null, null)).isTrue();
    }

    @Test
    void shouldBeValidIfValueIsNotEmptyOrBlank(){
        assertThat(this.validator.isValid("value", null)).isTrue();
    }

    @Test
    void shouldNotBeValidIfValueIsEmpty(){
        assertThat(this.validator.isValid("", null)).isFalse();
    }

    @Test
    void shouldNotBeValidIfValueIsBlank(){
        assertThat(this.validator.isValid(" ", null)).isFalse();
    }
}
