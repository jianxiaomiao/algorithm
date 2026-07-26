import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class Java21FeatureDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("===== 1. Record、枚举、集合、泛型 =====");
        demonstrateBasicFeatures();

        System.out.println("\n===== 2. Stream、Lambda、Optional =====");
        demonstrateStreamAndOptional();

        System.out.println("\n===== 3. 注解和反射 =====");
        demonstrateAnnotationAndReflection();

        System.out.println("\n===== 4. 并发基础和线程池 =====");
        demonstrateThreadPool();

        System.out.println("\n===== 5. CompletableFuture =====");
        demonstrateCompletableFuture();

        System.out.println("\n===== 6. Java 21 虚拟线程 =====");
        demonstrateVirtualThreads();
    }

    /*
     * =========================================================
     * 1. Record
     * =========================================================
     *
     * Record 适合表示主要用于传递数据的对象。
     *
     * 编译器会自动生成：
     * - 构造方法
     * - id()
     * - title()
     * - priority()
     * - equals()
     * - hashCode()
     * - toString()
     */
    public record Ticket(
            long id,
            String title,
            TicketPriority priority,
            TicketStatus status,
            LocalDateTime createdAt
    ) {
        /*
         * Record 也可以写紧凑构造器，用于校验参数。
         */
        public Ticket {
            if (id <= 0) {
                throw new IllegalArgumentException("工单 ID 必须大于 0");
            }

            if (title == null || title.isBlank()) {
                throw new IllegalArgumentException("工单标题不能为空");
            }

            if (priority == null) {
                throw new IllegalArgumentException("工单优先级不能为空");
            }

            if (status == null) {
                throw new IllegalArgumentException("工单状态不能为空");
            }
        }

        /*
         * Record 不仅能保存数据，也可以定义方法。
         */
        public boolean isUrgent() {
            return priority == TicketPriority.P0
                    || priority == TicketPriority.P1;
        }
    }

    /*
     * =========================================================
     * 2. 枚举
     * =========================================================
     *
     * 枚举不仅是字符串常量，还可以拥有字段和方法。
     */
    public enum TicketPriority {
        P0(15),
        P1(30),
        P2(240),
        P3(1_440);

        private final int responseMinutes;

        TicketPriority(int responseMinutes) {
            this.responseMinutes = responseMinutes;
        }

        public int getResponseMinutes() {
            return responseMinutes;
        }
    }

    public enum TicketStatus {
        CREATED,
        ASSIGNED,
        PROCESSING,
        RESOLVED,
        CLOSED;

        /*
         * 用枚举保存状态转换规则。
         */
        public boolean canTransitionTo(TicketStatus target) {
            return switch (this) {
                case CREATED ->
                        target == ASSIGNED;

                case ASSIGNED ->
                        target == PROCESSING;

                case PROCESSING ->
                        target == RESOLVED;

                case RESOLVED ->
                        target == CLOSED
                                || target == PROCESSING;

                case CLOSED ->
                        false;
            };
        }
    }

    /*
     * =========================================================
     * 3. 自定义异常
     * =========================================================
     */
    public static class TicketNotFoundException extends RuntimeException {

        public TicketNotFoundException(long ticketId) {
            super("找不到工单，ticketId=" + ticketId);
        }
    }

    public static class InvalidTicketStatusException
            extends RuntimeException {

        public InvalidTicketStatusException(
                TicketStatus current,
                TicketStatus target
        ) {
            super("不允许从 " + current + " 转换到 " + target);
        }
    }

    /*
     * =========================================================
     * 4. 泛型
     * =========================================================
     *
     * Repository<T, ID> 可以存储不同类型的对象。
     *
     * T：实体类型
     * ID：实体主键类型
     */
    public interface Repository<T, ID> {

        void save(ID id, T entity);

        Optional<T> findById(ID id);

        List<T> findAll();

        void deleteById(ID id);
    }

    /*
     * 泛型接口的一个内存实现。
     */
    public static class InMemoryRepository<T, ID>
            implements Repository<T, ID> {

        /*
         * ConcurrentHashMap 是线程安全的 Map。
         */
        private final Map<ID, T> data = new ConcurrentHashMap<>();

        @Override
        public void save(ID id, T entity) {
            data.put(id, entity);
        }

        @Override
        public Optional<T> findById(ID id) {
            /*
             * Optional 表示结果可能存在，也可能不存在。
             */
            return Optional.ofNullable(data.get(id));
        }

        @Override
        public List<T> findAll() {
            /*
             * 返回副本，防止外部直接修改内部集合。
             */
            return new ArrayList<>(data.values());
        }

        @Override
        public void deleteById(ID id) {
            data.remove(id);
        }
    }

    /*
     * =========================================================
     * 5. 注解
     * =========================================================
     *
     * RetentionPolicy.RUNTIME 表示运行时仍能读取此注解。
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface AuditOperation {

        String value();
    }

    public static class TicketService {

        private final Repository<Ticket, Long> repository;

        public TicketService(Repository<Ticket, Long> repository) {
            this.repository = repository;
        }

        @AuditOperation("创建工单")
        public Ticket create(
                long id,
                String title,
                TicketPriority priority
        ) {
            Ticket ticket = new Ticket(
                    id,
                    title,
                    priority,
                    TicketStatus.CREATED,
                    LocalDateTime.now()
            );

            repository.save(id, ticket);
            return ticket;
        }

        @AuditOperation("查询工单")
        public Ticket getById(long id) {
            return repository.findById(id)
                    .orElseThrow(
                            () -> new TicketNotFoundException(id)
                    );
        }

        @AuditOperation("转换工单状态")
        public Ticket transition(
                long id,
                TicketStatus target
        ) {
            Ticket oldTicket = getById(id);

            if (!oldTicket.status().canTransitionTo(target)) {
                throw new InvalidTicketStatusException(
                        oldTicket.status(),
                        target
                );
            }

            Ticket newTicket = new Ticket(
                    oldTicket.id(),
                    oldTicket.title(),
                    oldTicket.priority(),
                    target,
                    oldTicket.createdAt()
            );

            repository.save(id, newTicket);
            return newTicket;
        }

        public List<Ticket> findAll() {
            return repository.findAll();
        }

        /*
         * Predicate<T> 是一个函数式接口。
         * 它接收一个 T，返回 boolean。
         */
        public List<Ticket> filter(Predicate<Ticket> condition) {
            return repository.findAll()
                    .stream()
                    .filter(condition)
                    .toList();
        }
    }

    private static TicketService createServiceWithData() {
        Repository<Ticket, Long> repository =
                new InMemoryRepository<>();

        TicketService service = new TicketService(repository);

        service.create(
                1L,
                "生产服务器无法访问",
                TicketPriority.P0
        );

        service.create(
                2L,
                "申请开通代码仓库权限",
                TicketPriority.P2
        );

        service.create(
                3L,
                "登录页面显示异常",
                TicketPriority.P1
        );

        service.create(
                4L,
                "更新员工通讯录",
                TicketPriority.P3
        );

        return service;
    }

    private static void demonstrateBasicFeatures() {
        TicketService service = createServiceWithData();

        /*
         * List 是有序集合。
         */
        List<Ticket> tickets = service.findAll();

        /*
         * Set 不允许重复元素。
         */
        Set<TicketStatus> workingStatuses = EnumSet.of(
                TicketStatus.ASSIGNED,
                TicketStatus.PROCESSING
        );

        System.out.println("工单数量：" + tickets.size());
        System.out.println("工作中状态：" + workingStatuses);

        Ticket first = tickets.getFirst();

        System.out.println("第一张工单：" + first);
        System.out.println("是否紧急：" + first.isUrgent());
        System.out.println(
                "响应时间要求："
                        + first.priority().getResponseMinutes()
                        + " 分钟"
        );

        /*
         * 异常处理。
         */
        try {
            service.getById(999L);
        } catch (TicketNotFoundException exception) {
            System.out.println(
                    "成功捕获业务异常：" + exception.getMessage()
            );
        }

        /*
         * 状态机异常处理。
         *
         * CREATED 不能直接转换到 CLOSED。
         */
        try {
            service.transition(1L, TicketStatus.CLOSED);
        } catch (InvalidTicketStatusException exception) {
            System.out.println(
                    "状态转换失败：" + exception.getMessage()
            );
        }
    }

    private static void demonstrateStreamAndOptional() {
        TicketService service = createServiceWithData();

        /*
         * Lambda：
         *
         * ticket -> ticket.isUrgent()
         *
         * 表示接收一个 ticket，返回它是否紧急。
         */
        List<Ticket> urgentTickets = service.findAll()
                .stream()
                .filter(ticket -> ticket.isUrgent())
                .sorted(
                        Comparator.comparing(
                                Ticket::priority
                        )
                )
                .toList();

        System.out.println("紧急工单：");

        urgentTickets.forEach(
                ticket -> System.out.println(
                        ticket.id() + " - " + ticket.title()
                )
        );

        /*
         * Stream 可以对集合进行：
         * - filter：过滤
         * - map：转换
         * - sorted：排序
         * - reduce：聚合
         */
        List<String> titles = service.findAll()
                .stream()
                .map(Ticket::title)
                .map(String::toUpperCase)
                .toList();

        System.out.println("转换后的标题：" + titles);

        /*
         * 按优先级分组。
         */
        Map<TicketPriority, List<Ticket>> grouped =
                service.findAll()
                        .stream()
                        .collect(
                                java.util.stream.Collectors.groupingBy(
                                        Ticket::priority
                                )
                        );

        System.out.println("按照优先级分组：" + grouped);

        /*
         * Optional 示例。
         */
        Repository<Ticket, Long> repository =
                new InMemoryRepository<>();

        Optional<Ticket> optionalTicket =
                repository.findById(100L);

        String result = optionalTicket
                .map(Ticket::title)
                .orElse("没有找到工单");

        System.out.println("Optional 查询结果：" + result);

        /*
         * Predicate Lambda。
         */
        List<Ticket> p0Tickets = service.filter(
                ticket -> ticket.priority() == TicketPriority.P0
        );

        System.out.println("P0 工单数量：" + p0Tickets.size());
    }

    private static void demonstrateAnnotationAndReflection()
            throws Exception {

        /*
         * 反射可以在程序运行时检查类、方法、字段和注解。
         */
        Class<TicketService> serviceClass = TicketService.class;

        for (Method method : serviceClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AuditOperation.class)) {
                AuditOperation annotation =
                        method.getAnnotation(AuditOperation.class);

                System.out.println(
                        "方法名：" + method.getName()
                                + "，审计操作："
                                + annotation.value()
                );
            }
        }

        /*
         * 通过反射调用方法。
         *
         * 实际业务中不建议到处这样调用。
         * Spring 框架底层会大量使用反射。
         */
        TicketService service = createServiceWithData();

        Method getByIdMethod =
                serviceClass.getMethod("getById", long.class);

        Object result = getByIdMethod.invoke(service, 1L);

        System.out.println("反射调用结果：" + result);
    }

    private static void demonstrateThreadPool()
            throws InterruptedException {

        /*
         * AtomicInteger 提供线程安全的整数操作。
         *
         * 普通 int++ 不是原子操作。
         */
        AtomicInteger successCount = new AtomicInteger();

        /*
         * 创建固定大小线程池。
         *
         * 这里最多同时运行 4 个任务。
         */
        ExecutorService executor =
                Executors.newFixedThreadPool(4);

        for (int i = 1; i <= 10; i++) {
            int taskId = i;

            executor.submit(() -> {
                try {
                    System.out.println(
                            Thread.currentThread().getName()
                                    + " 正在处理任务 "
                                    + taskId
                    );

                    Thread.sleep(200);

                    successCount.incrementAndGet();
                } catch (InterruptedException exception) {
                    /*
                     * 恢复中断标记。
                     */
                    Thread.currentThread().interrupt();

                    System.out.println(
                            "任务被中断：" + taskId
                    );
                }
            });
        }

        /*
         * 不再接受新任务。
         */
        executor.shutdown();

        /*
         * 等待已有任务执行完成。
         */
        boolean completed = executor.awaitTermination(
                5,
                TimeUnit.SECONDS
        );

        System.out.println("是否全部完成：" + completed);
        System.out.println(
                "成功处理数量：" + successCount.get()
        );
    }

    private static void demonstrateCompletableFuture() {
        /*
         * CompletableFuture 适合组织多个异步任务。
         *
         * 假设展示工单详情时，需要同时查询：
         * - 用户信息
         * - 工单信息
         * - 权限信息
         */
        ExecutorService executor =
                Executors.newFixedThreadPool(3);

        CompletableFuture<String> userFuture =
                CompletableFuture.supplyAsync(() -> {
                    sleep(500);
                    return "用户：小淼";
                }, executor);

        CompletableFuture<String> ticketFuture =
                CompletableFuture.supplyAsync(() -> {
                    sleep(700);
                    return "工单：生产服务器无法访问";
                }, executor);

        CompletableFuture<String> permissionFuture =
                CompletableFuture.supplyAsync(() -> {
                    sleep(300);
                    return "权限：ticket:read";
                }, executor);

        /*
         * thenCombine 合并两个异步结果。
         */
        CompletableFuture<String> pageFuture =
                userFuture
                        .thenCombine(
                                ticketFuture,
                                (user, ticket) ->
                                        user + "\n" + ticket
                        )
                        .thenCombine(
                                permissionFuture,
                                (page, permission) ->
                                        page + "\n" + permission
                        )
                        .exceptionally(exception ->
                                "加载失败：" + exception.getMessage()
                        );

        /*
         * join 等待并取得结果。
         */
        String page = pageFuture.join();

        System.out.println(page);

        executor.shutdown();
    }

    private static void demonstrateVirtualThreads()
            throws InterruptedException {

        /*
         * Java 21 虚拟线程。
         *
         * 每个任务使用一个虚拟线程。
         *
         * 它特别适合大量包含阻塞等待的 I/O 任务，例如：
         * - HTTP 请求
         * - 数据库访问
         * - 文件读写
         *
         * 它不会让 CPU 计算本身变快。
         */
        try (
                ExecutorService executor =
                        Executors.newVirtualThreadPerTaskExecutor()
        ) {
            List<CompletableFuture<String>> futures =
                    new ArrayList<>();

            for (int i = 1; i <= 20; i++) {
                int taskId = i;

                CompletableFuture<String> future =
                        CompletableFuture.supplyAsync(() -> {
                            sleep(200);

                            return "虚拟线程任务 "
                                    + taskId
                                    + "，线程="
                                    + Thread.currentThread();
                        }, executor);

                futures.add(future);
            }

            /*
             * 等待所有任务完成。
             */
            CompletableFuture.allOf(
                    futures.toArray(
                            CompletableFuture[]::new
                    )
            ).join();

            futures.stream()
                    .limit(5)
                    .map(CompletableFuture::join)
                    .forEach(System.out::println);

            System.out.println(
                    "其余虚拟线程任务也已执行完成。"
            );
        }
    }

    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                    "线程被中断",
                    exception
            );
        }
    }
}