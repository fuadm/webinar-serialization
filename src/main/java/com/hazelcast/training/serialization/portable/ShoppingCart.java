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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShoppingCart implements Portable {
    public long total = 0;
    public Date date;
    public long id;
    private List<ShoppingCartItem> items = new ArrayList<>();

    public void addItem(ShoppingCartItem item) {
        items.add(item);
        total += item.cost * item.quantity;
    }

    public void removeItem(int index) {
        ShoppingCartItem item = items.remove(index);
        total -= item.cost * item.quantity;
    }

    public int size() {
        return items.size();
    }

    @Override
    public int getFactoryId() {
        return 2;
    }

    @Override
    public int getClassId() {
        return 2;
    }

    @Override
    public void writePortable(PortableWriter out) throws IOException {
        out.writeLong("total", total);
        out.writeLong("date", date.getTime());
        out.writeLong("id", id);
        Portable[] portables = items.toArray(new Portable[]{});
        out.writePortableArray("items", portables);
    }

    @Override
    public void readPortable(PortableReader in) throws IOException {
        Portable[] portables = in.readPortableArray("items");
        items = new ArrayList<>(portables.length);
        for (Portable portable : portables) {
            items.add((ShoppingCartItem) portable);
        }
        id = in.readLong("id");
        total = in.readLong("total");
        date = new Date(in.readLong("date"));
    }
}
