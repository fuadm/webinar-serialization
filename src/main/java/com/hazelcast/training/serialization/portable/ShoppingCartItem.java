/*
 * Copyright (c) 2008-2010, Hazel Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hazelcast.training.serialization.portable;

import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;

import java.io.IOException;

public class ShoppingCartItem implements Portable {
    public long cost;
    public int quantity;
    public String itemName;
    public boolean inStock;
    public String url;

    @Override
    public int getFactoryId() {
        return 2;
    }

    @Override
    public int getClassId() {
        return 1;
    }

    @Override
    public void writePortable(PortableWriter out) throws IOException {
        out.writeLong("cost", cost);
        out.writeInt("quantity", quantity);
        out.writeUTF("name", itemName);
        out.writeBoolean("stock", inStock);
        out.writeUTF("url", url);
    }

    @Override
    public void readPortable(PortableReader in) throws IOException {
        url = in.readUTF("url");
        quantity = in.readInt("quantity");
        cost = in.readLong("cost");
        inStock = in.readBoolean("stock");
        itemName = in.readUTF("name");
    }
}
