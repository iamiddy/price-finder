import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Created by iddymagohe on 10/21/16.
 */
public class Shop {

    private String name;

    public Shop(String name) {
        this.name = name;
    }

    public double getPrice(String product) {
        return calculatePrice(product);
    }

    public Future<Double> getPriceAsyncOld(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            try {
                double price = calculatePrice(product); // executes the computation asynchronously in a different thread
                futurePrice.complete(price); //Set the value returned by long computation on the Future when it becomes available
            } catch (Exception ex) {
                futurePrice.completeExceptionally(ex); // complete the complete it exceptionally, with Exception that caused the failure.
            }

        }).start();
        return futurePrice; // Return the Future without waiting  for the computation of the result it contains to be completed.
    }

    public Future<Double> getPriceAsync(String product) {
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }

    private double calculatePrice(String product) {
        delay();
        Random random = new Random();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    public static void delay(){
        try {
            Thread.sleep(1000L);
        }catch (InterruptedException e){
            throw  new RuntimeException(e);
        }
    }


    public String getName() {
        return name;
    }
}
