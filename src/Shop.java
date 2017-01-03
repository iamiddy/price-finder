import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Created by iddymagohe on 10/21/16.
 */
public class Shop {

    private String name;

    Random random = new Random();

    public Shop(String name) {
        this.name = name;
    }

    public String getPrice(String product) {

        double price = calculatePrice(product);
        Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
        return String.format("%s:%.2f:%s", name, price, code);
    }

    public Future<String> getPriceAsyncOld(String product) {

        CompletableFuture<String> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            try {
                String price = getPrice(product); // executes the computation asynchronously in a different thread

                futurePrice.complete(price); //Set the value returned by long computation on the Future when it becomes available
            } catch (Exception ex) {
                futurePrice.completeExceptionally(ex); // complete the complete it exceptionally, with Exception that caused the failure.
            }

        }).start();
        return futurePrice; // Return the Future without waiting  for the computation of the result it contains to be completed.
    }

    public Future<String> getPriceAsync(String product) {
        return CompletableFuture.supplyAsync(() -> getPrice(product));
    }

    private double calculatePrice(String product) {
        delay();
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
