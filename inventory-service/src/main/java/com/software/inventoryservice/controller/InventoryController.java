package com.software.inventoryservice.controller;

import com.software.inventoryservice.constants.Constant;
import com.software.inventoryservice.service.RetryService;
import io.github.bucket4j.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final Logger log = LoggerFactory.getLogger(InventoryController.class);
    private final Bucket bucket;
    private final Environment environment;
    private final RetryService retryService;

    public InventoryController(Bucket bucket, Environment environment1, RetryService retryService) {
        this.bucket = bucket;
        this.environment = environment1;
        this.retryService = retryService;
    }

    @GetMapping("/retry")
    public ResponseEntity<String> retryCheck(@RequestParam String message) {
        retryService.retryMethod(message);
        return ResponseEntity.ok("consumed requests with message: " + message);
    }
    @GetMapping("/rate-limiter")
    public ResponseEntity<String> rateLimiter(@RequestParam int quantity) {
        if (quantity > 10) {

            new Thread(this::consume).start();
            new Thread(this::printOdd).start();
            while (!bucket.tryConsume(1)) {
                log.info("sleeping with AvailableTokens = {} and quantity {}", bucket.getAvailableTokens(), quantity);
                try {
                    log.info("sleeping in try with AvailableTokens = {} and quantity {}", bucket.getAvailableTokens(), quantity);
                    Thread.sleep(Long.parseLong(Objects.requireNonNull(environment.getProperty(Constant.threadSleepDuration))));
                } catch (InterruptedException e) {
                    log.error("sleeping interrupted with error = {}", e.getMessage());
                }

            }
            log.info("consumed requests with AvailableTokens = {} and quantity {}", bucket.getAvailableTokens(), quantity);
            return ResponseEntity.ok("consumed requests with more than 10 quantity: " + quantity);
        } else {
            log.info("consumed requests with less than 11 quantity with AvailableTokens = {} and quantity {}", bucket.getAvailableTokens(), quantity);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("consumed requests with less than 11 quantity:" + quantity);
        }
    }


    private void consume() {
        synchronized (this) {
            while (!bucket.tryConsume(1)) {
                try {
                    wait();
                } catch (InterruptedException ignored) {}
                System.out.println(Thread.currentThread().getName());
                notify();
            }
        }
    }

    private void printOdd() {
        synchronized (this) {
            while (!bucket.tryConsume(1)) {
                try {
                    wait();
                } catch (InterruptedException ignored) {}
                System.out.println(Thread.currentThread().getName());
                notify();
            }
        }
    }

}
