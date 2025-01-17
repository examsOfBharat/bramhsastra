package com.examsofbharat.bramhsastra.akash.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FormExecutorService {
    public static final ExecutorService mailExecutorService = Executors.newFixedThreadPool(5);
    public static final ExecutorService responseSaveService = Executors.newFixedThreadPool(3);
    public static final ExecutorService formResponseService = Executors.newFixedThreadPool(10);

    public static final ExecutorService appCacheService = Executors.newFixedThreadPool(10);

    public static final ExecutorService genResCacheService = Executors.newFixedThreadPool(10);

    public static final ExecutorService refreshCacheService = Executors.newFixedThreadPool(10);

    public static final ExecutorService formCacheService = Executors.newFixedThreadPool(20);
}
