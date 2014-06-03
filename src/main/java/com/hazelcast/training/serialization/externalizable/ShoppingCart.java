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

package com.hazelcast.training.serialization.externalizable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShoppingCart implements Externalizable {
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
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(total);
        out.writeLong(date.getTime());
        out.writeLong(id);
        out.writeInt(items.size());
        items.forEach(item -> {
            try {
                item.writeExternal(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        total = in.readLong();
        date = new Date(in.readLong());
        id = in.readLong();
        int count = in.readInt();
        items = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            ShoppingCartItem item = new ShoppingCartItem();
            item.readExternal(in);
            items.add(item);
        }
    }
}
