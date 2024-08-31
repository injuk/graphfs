package ga.injuk.graphfs.infrastructure.web

import ga.injuk.graphfs.domain.exception.NoSuchResourceException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class Advice {
    companion object {
        private val logger = LoggerFactory.getLogger(Advice::class.java)
    }

    @ExceptionHandler(NoSuchResourceException::class)
    fun handleNotFoundException(e: NoSuchResourceException): ResponseEntity<ErrorDetail> {
        logger.error("NoSuchResourceException occurred", e)
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                ErrorDetail(
                    message = e.message,
                ),
            )
    }

    @ExceptionHandler(IllegalStateException::class, IllegalArgumentException::class)
    fun handleBadRequestException(e: RuntimeException): ResponseEntity<ErrorDetail> {
        logger.error("BadRequestException occurred", e)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorDetail(
                    message = e.message!!,
                ),
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleInternalServerError(e: Exception): ResponseEntity<ErrorDetail> {
        logger.error("InternalServerError occurred", e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorDetail(
                    message = e.message ?: "internal server error occurred",
                ),
            )
    }

    data class ErrorDetail(
        val message: String,
    )
}