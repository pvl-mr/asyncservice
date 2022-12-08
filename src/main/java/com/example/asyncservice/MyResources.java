package com.example.asyncservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@RestController
public class MyResources {
    @Autowired
    private MyService myService;

    private static int calculate(int[][] matrix){
        int size = matrix.length;
        int sum = 0;
        for (int i = 0; i < size-1; i++) {
            int k = i+1;
            for (int j = k; j < size; j++) {
                if (i != j) {
                    sum += matrix[i][j];
                }
                k++;
            }
        }
        return sum;
    }

    private static int[][] generate_matrix(){
        int size = 10000;
        int[][] matrix = new int[size][size];
        Random random = new Random(100);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = random.nextInt();
            }
        }
        return matrix;

    }

    private static String part1(int[][] matrix){
        Instant start = Instant.now();
        int result1 = calculate(matrix);
        Instant finish = Instant.now();
        long elapsed = Duration.between(start, finish).toMillis();
        return "Результат в однопотоке: " + result1 + " Время: " + elapsed + "ms";
    }


    @GetMapping("/lab")
    public List<String> calc_two_part() throws ExecutionException, InterruptedException {
        int[][] matrix = generate_matrix();

        String one_res = part1(matrix);

        Instant start2 = Instant.now();
        int step = matrix.length/10;
        int sum = 0;

        List<CompletableFuture> allFutures = new ArrayList<>();
        for (int i = 0; i < matrix.length; i += step) {
            CompletableFuture<Integer> future = myService.calculate(matrix, i, i+step);
            allFutures.add(future);
        }

        for (int i = 0; i < allFutures.size(); i++) {
            sum += (Integer)allFutures.get(i).get();
        }
        Instant finish2 = Instant.now();
        String two_res = "Результат в многопотоке: " +  sum + " Время: " + Duration.between(start2, finish2).toMillis() + "ms";

        List<String> str = new ArrayList<String>();
        str.add(one_res);
        str.add(two_res);
        return str;
    }
}
