//package com.dns.polinsight.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.AsyncTaskExecutor;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.util.concurrent.Callable;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Future;
//
//@Slf4j
//@EnableAsync
//@Configuration
//public class AsyncConfig {
//
//  public Executor threadPoolTaskExecutor() {
//    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//    taskExecutor.setCorePoolSize(3);
//    taskExecutor.setMaxPoolSize(30);
//    taskExecutor.setQueueCapacity(10);
//    taskExecutor.setThreadNamePrefix("Executor-");
//    taskExecutor.initialize();
//    return new HandlingExecutor(taskExecutor);
//  }
//
//
//  public class HandlingExecutor implements AsyncTaskExecutor {
//
//    private final AsyncTaskExecutor executor;
//
//    public HandlingExecutor(AsyncTaskExecutor executor) {
//      this.executor = executor;
//    }
//
//    @Override
//    public void execute(Runnable task, long startTimeout) {
//      executor.execute(createWrappedRunnable(task), startTimeout);
//    }
//
//    @Override
//    public Future<?> submit(Runnable task) {
//      return executor.submit(createWrappedRunnable(task));
//    }
//
//    @Override
//    public <T> Future<T> submit(Callable<T> task) {
//      return executor.submit(createCallable(task));
//    }
//
//    @Override
//    public void execute(Runnable task) {
//
//    }
//
//    private <T> Callable<T> createCallable(final Callable<T> task) {
//      return new Callable<T>() {
//        @Override
//        public T call() throws Exception {
//          try {
//            return task.call();
//          } catch (Exception e) {
//            handle(e);
//            throw e;
//          }
//        }
//      };
//    }
//
//    private Runnable createWrappedRunnable(final Runnable task) {
//      return new Runnable() {
//        @Override
//        public void run() {
//          try {
//            task.run();
//          } catch (Exception e) {
//            handle(e);
//          }
//        }
//      };
//    }
//
//    private void handle(Exception e) {
//      log.info("Faild to execute task: {}", e.getMessage());
//      log.error("Failed to execute task ", e);
//    }
//
//    public void destory() {
//      if (executor instanceof ThreadPoolTaskExecutor) {
//        ((ThreadPoolTaskExecutor) executor).shutdown();
//      }
//    }
//
//  }
//
//}
