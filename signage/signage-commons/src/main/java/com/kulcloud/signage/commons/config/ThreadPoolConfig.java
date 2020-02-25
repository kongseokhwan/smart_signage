package com.kulcloud.signage.commons.config;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {
	private static AsyncTaskExecutor thread;
	private static ScheduledExecutorService scheduler;
	
	public static AsyncTaskExecutor getThread() {
		return thread;
	}
	
	public static ScheduledExecutorService getScheduler() {
		return scheduler;
	}
	
	@Value("${signage.maxThreadPoolSize:30}")
	private int maxThreadPoolSize;
	
	@Primary
	@Bean(name = "asyncExecutor", destroyMethod = "destroy")
	public AsyncTaskExecutor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(maxThreadPoolSize);
        executor.setThreadNamePrefix("ASYNC-");
        executor.initialize();
        thread = new HandlingExecutor(executor);
        return thread;
	}
	
	@Bean(name = "scheduler")
	public ScheduledExecutorService scheduler() {
		scheduler = Executors.newScheduledThreadPool(5);
		return scheduler;
	}
	
	public class HandlingExecutor implements AsyncTaskExecutor {
		private AsyncTaskExecutor executor;

        public HandlingExecutor(AsyncTaskExecutor executor) {
            this.executor = executor;
        }

        @Override
        public void execute(Runnable task) {
            executor.execute(task);
        }

        @Override
        public void execute(Runnable task, long startTimeout) {
            executor.execute(task, startTimeout);
        }

        @Override
        public Future<?> submit(Runnable task) {
            return executor.submit(task);
        }

        @Override
        public <T> Future<T> submit(final Callable<T> task) {
            return executor.submit(task);
        }

        public void destroy() {
            if(executor instanceof ThreadPoolTaskExecutor){
                ((ThreadPoolTaskExecutor) executor).shutdown();
            }
            
            if(ThreadPoolConfig.thread == this) {
            	ThreadPoolConfig.thread = null;
            }
        }
    }
}
