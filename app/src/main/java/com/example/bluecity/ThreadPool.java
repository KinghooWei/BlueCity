package com.example.bluecity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    public static final ExecutorService executorService = Executors.newSingleThreadExecutor();
}
