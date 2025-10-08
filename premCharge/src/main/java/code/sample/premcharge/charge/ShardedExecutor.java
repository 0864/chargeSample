package code.sample.premcharge.charge;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * 使用 CompletableFuture 替代 Future 的版本。
 * 每个 shard 都有独立的单线程执行器。
 */
public class ShardedExecutor implements AutoCloseable {

    private final int shards;
    private final ExecutorService[] singleExecutors;

    public ShardedExecutor(int shards, String prefix) {
        this.shards = shards;
        this.singleExecutors = IntStream.range(0, shards)
                .mapToObj(i -> Executors.newSingleThreadExecutor(r -> {
                    Thread t = new Thread(r);
                    t.setName(prefix + "-shard-" + i);
                    t.setDaemon(true);
                    return t;
                }))
                .toArray(ExecutorService[]::new);
    }

    /**
     * 异步提交任务并返回 CompletableFuture。
     */
    public CompletableFuture<Void> submitAsync(long key, Runnable task) {
        int shard = (int) Math.floorMod(key, shards);
        ExecutorService executor = singleExecutors[shard];
        return CompletableFuture.runAsync(task, executor);
    }

    @Override
    public void close() {
        Arrays.stream(singleExecutors).forEach(ExecutorService::shutdown);
    }
}
