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

package com.hazelcast.training.serialization.identified;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShoppingCart implements IdentifiedDataSerializable {
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
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeLong(total);
        out.writeLong(date.getTime());
        out.writeLong(id);
        out.writeInt(items.size());
        items.forEach(item -> {
            try {
                item.writeData(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        total = in.readLong();
        date = new Date(in.readLong());
        id = in.readLong();
        int count = in.readInt();
        items = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            ShoppingCartItem item = new ShoppingCartItem();
            item.readData(in);
            items.add(item);
        }
    }

    @Override
    public int getFactoryId() {
        return 1;
    }

    @Override
    public int getId() {
        return 2;
    }
}
