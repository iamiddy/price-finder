import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

public class Main {
    static List<Shop> shops;
     static {
         shops = Arrays.asList(new Shop("BestPrice"),
                new Shop("LetsSaveBig"),
                new Shop("MyFavoriteShop"),
                 new Shop("MyFavoergsdgriteShopYeee"),
                 new Shop("MyFavoriteShop"),
                 new Shop("MyFavoergsdgriteShopYeee"),
                 new Shop("MyFavoriteShop"),
                 new Shop("MyFavoergsdgriteShopYeee"),
                 new Shop("MyFavoriteShop"),
                 new Shop("MyFavoergsdgriteShopYeee"),
                 new Shop("LetsSaveBig"),
                 new Shop("MyFavoriteShop"),
                 new Shop("MyFavoergsdgriteShopYeee"),
                 new Shop("MyFavoriteShop"),
                 new Shop("MyFavoergsdgriteShopYeee"),
                 new Shop("MyFavoriteShop"),
                 new Shop("MyFavoergsdgriteShopYeee"),
                 new Shop("MyFavoriteShop"),
                 new Shop("MyFavoergsdgriteShopYeee"),
                 new Shop("LetsSaveBig"),
                 new Shop("MyFavoriteShop"),
                 new Shop("MyFavoergsdgriteShopYeee"),
                 new Shop("MyFavoriteShop"),
                 new Shop("MyFavoergsdgriteShopYeee"),
                 new Shop("MyFavoriteShop"),
                 new Shop("MyFavoergsdgriteShopYeee"),
                 new Shop("MyFavoriteShop"),
                 new Shop("MyFavoergsdgriteShopYeee"),
                 new Shop("LetsSaveBig"),
                 new Shop("MyFavoriteShop"),
                 new Shop("MyFavoergsdgriteShopYeee"),
                 new Shop("MyFavoriteShop"),
                 new Shop("MyFavoergsdgriteShopYeee"),
                 new Shop("MyFavoriteShop"),
                 new Shop("MyFavoergsdgriteShopYeee"),
                 new Shop("MyFavoriteShop"),
                 new Shop("MyFavoergsdgriteShopYeee"),
                new Shop("BuyItAll"));
    }

    private static final Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true); // Use daemon threads—they don’t prevent the termination of the program.
            return  thread;
        }
    });


    public static void main(String[] args) {

//        Shop shop = new Shop("BestSHop");
//        long start = System.nanoTime();
//        Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
//        long invocationTime = ((System.nanoTime() - start) / 1_000_000);
//        System.out.println("The Invocation returned after " + invocationTime + "msecs");
//        // do some more tasks, like querying other shops doSomethingElse()
//        //while the price of product is being calculated
//        try {
//            double price = futurePrice.get();
//            System.out.printf("Price is %.2f%n", price);
//        } catch (Exception ex) {
//            throw new RuntimeException(ex);
//        }
//        long retrievalTime = ((System.nanoTime() - start)/1_000_000);
//        System.out.println("The Invocation returned after " + retrievalTime + "msecs");

        long start = System.nanoTime();
        //System.out.println(findPricesStreams("myPhone27S")); //Done in 4073 msecs
      //System.out.println(findPricesParalleStreams("myPhone27S")); // Done in 5091 msecs
       // System.out.println("P>>" +findPrices("myPhone27S")); //Done in 1113 msecs
        System.out.println("P>>" +findPricesComposed("myPhone27S")); //Done in 1067 msecs
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");
    }

    public static List<String> findPricesStreams(String product){
        return shops.stream().map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))).collect(Collectors.toList());
    }

    public static List<String> findPricesParalleStreams(String product){
        return shops.parallelStream().map(shop -> shop.getPrice(product)).collect(Collectors.toList());
    }

    public static List<String> findPrices(String product){
        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product),executor))
                .collect(Collectors.toList());

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public static List<String> findPricesComposed(String product){
        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product),executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote),executor)))// compose the resulting future with another async task, applying the discount code
                .collect(Collectors.toList());

        return priceFutures.stream()
                .map(CompletableFuture::join) // wait for all futures in the stream to be completed & extract their respective result
                .collect(Collectors.toList());
    }
}
