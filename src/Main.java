import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Main {
    static List<Shop> shops;
     static {
         shops = Arrays.asList(new Shop("BestPrice"),
                new Shop("LetsSaveBig"),
                new Shop("MyFavoriteShop"),
                new Shop("BuyItAll"));
    }

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
        //System.out.println(findPricesParalleStreams("myPhone27S")); // Done in 1061 msecs
        //System.out.println(findPrices("myPhone27S")); //Done in 1054 msecs
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");
    }

    public static List<String> findPricesStreams(String product){
        return shops.stream().map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))).collect(Collectors.toList());
    }

    public static List<String> findPricesParalleStreams(String product){
        return shops.parallelStream().map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))).collect(Collectors.toList());
    }

    public static List<String> findPrices(String product){
        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getName() + " price is " + shop.getPrice(product)))
                .collect(Collectors.toList());

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
}
