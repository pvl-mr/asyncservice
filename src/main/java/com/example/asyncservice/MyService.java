package com.example.asyncservice;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class MyService {

    @Async
    public CompletableFuture<Integer> calculate(int[][] matrix, int begin, int end){
        System.out.println("Current thread is " + Thread.currentThread().getName());
        int size = matrix.length;
        int sum = 0;
        for (int i = begin; i < end; i++) {
            int k = i+1;
            for (int j = k; j < size; j++) {
                if (i != j) {
                    sum += matrix[i][j];
                }
                k++;
            }
        }
        return CompletableFuture.completedFuture(sum);
    }
}
