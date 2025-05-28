package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionTest {

    @Test
    void badRequestException_ShouldHaveCorrectAnnotation() {
        // Given & When
        BadRequestException exception = new BadRequestException();
        ResponseStatus annotation = exception.getClass().getAnnotation(ResponseStatus.class);

        // Then
        assertThat(annotation).isNotNull();
        assertThat(annotation.value()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    void notFoundException_ShouldHaveCorrectAnnotation() {
        // Given & When
        NotFoundException exception = new NotFoundException();
        ResponseStatus annotation = exception.getClass().getAnnotation(ResponseStatus.class);

        // Then
        assertThat(annotation).isNotNull();
        assertThat(annotation.value()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
