package com.hazelcast.training.serialization.avro;

import com.hazelcast.config.Config;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.internal.serialization.SerializationServiceBuilder;
import com.hazelcast.internal.serialization.impl.DefaultSerializationServiceBuilder;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.spi.serialization.SerializationService;
import com.hazelcast.training.serialization.benchmarks.ShoppingCartBenchmark;

import java.util.Date;
import java.util.Random;

/**
 * TODO
 *
 * @author Viktor Gamov on 12/1/16.
 *         Twitter: @gamussa
 * @since 0.0.1
 */
public class AvroBenchmark implements ShoppingCartBenchmark {

    private HazelcastInstance hz;
    private IMap<Object, Object> cartMap;
    private int maxOrders = 100 * 1000;
    private int maxCartItems = 5;
    private String[] products;

    @Override
    public void setUp() {

        Config config = new Config();
        config.getSerializationConfig().getSerializerConfigs().add(
                new SerializerConfig().
                        setTypeClass(ShoppingCart.class).
                        setImplementation(new ShoppingCartAvroSerializer()));


        hz = Hazelcast.newHazelcastInstance(config);

        cartMap = hz.getMap("carts");
        products = new String[100];
        for (int k = 0; k < 100; k++) {
            products[k] = "product-" + k;
        }
        Random random = new Random();
        SerializationServiceBuilder builder = new DefaultSerializationServiceBuilder();
        builder.setConfig(hz.getConfig().getSerializationConfig());
        SerializationService ss = builder.build();
        int total = 0;
        for (int k = 0; k < maxOrders; k++) {
            ShoppingCart cart = createNewShoppingCart(random);
            Data data = ss.toData(cart);
            total += data.totalSize();
            cartMap.set(cart.id, cart);
        }
        System.out.println("Average size is " + total / maxOrders + " bytes");
    }

    @Override
    public void tearDown() {
        Hazelcast.shutdownAll();
    }

    @Override
    public void writePerformance() {
        Random random = new Random();
        for (int k = 0; k < OPERATIONS_PER_INVOCATION; k++) {
            ShoppingCart cart = createNewShoppingCart(random);
            cartMap.set(cart.getId(), cart);
        }
    }

    @Override
    public void readPerformance() {
        Random random = new Random();
        for (int k = 0; k < OPERATIONS_PER_INVOCATION; k++) {
            long orderId = random.nextInt(maxOrders);
            cartMap.get(orderId);
        }
    }


    private ShoppingCart createNewShoppingCart(Random random) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId((long) random.nextInt(maxOrders));
        cart.setDate(new Date().getTime());
        final ShoppingCartDelegate delegate = ShoppingCartDelegate.of(cart);
        int count = random.nextInt(maxCartItems);
        for (int k = 0; k < count; k++) {
            ShoppingCartItem item = createNewShoppingCartItem(random);
            delegate.addItem(item);
        }
        return delegate.getCart();
    }

    private ShoppingCartItem createNewShoppingCartItem(Random random) {
        int i = random.nextInt(10);
        ShoppingCartItem item = new ShoppingCartItem();
        item.setCost((long) (i * 9));
        item.setQuantity(i % 2);
        item.setItemName("item_" + i);
        item.setInStock(i == 9);
        item.setUrl("http://www.amazon.com/gp/product/" + i);
        return item;
    }
}
