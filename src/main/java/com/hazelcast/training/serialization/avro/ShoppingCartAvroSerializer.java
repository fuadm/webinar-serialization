package com.hazelcast.training.serialization.avro;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.Serializer;
import com.hazelcast.nio.serialization.StreamSerializer;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * TODO
 *
 * @author Viktor Gamov on 12/1/16.
 *         Twitter: @gamussa
 * @since 0.0.1
 */
public class ShoppingCartAvroSerializer implements StreamSerializer<ShoppingCart> {
    @Override
    public void write(ObjectDataOutput objectDataOutput, ShoppingCart cart) throws IOException {
        final DatumWriter<ShoppingCart> personDatumWriter = new SpecificDatumWriter<ShoppingCart>(ShoppingCart.class);
        final BinaryEncoder binaryEncoder = new EncoderFactory().binaryEncoder((OutputStream) objectDataOutput, null);
        personDatumWriter.write(cart, binaryEncoder);
        binaryEncoder.flush();
    }

    @Override
    public ShoppingCart read(ObjectDataInput objectDataInput) throws IOException {
        final DatumReader<ShoppingCart> reader = new SpecificDatumReader<ShoppingCart>(ShoppingCart.class);
        final BinaryDecoder binaryDecoder = new DecoderFactory().binaryDecoder((InputStream) objectDataInput, null);
        final ShoppingCart shoppingCart = new ShoppingCart();
        reader.read(shoppingCart, binaryDecoder);
        return shoppingCart;
    }

    @Override
    public int getTypeId() {
        return 42;
    }

    @Override
    public void destroy() {

    }
}
