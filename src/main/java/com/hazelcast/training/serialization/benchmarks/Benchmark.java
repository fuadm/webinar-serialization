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

package com.hazelcast.training.serialization.benchmarks;

import com.hazelcast.training.serialization.kryo.KryoBenchmark;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;

public class Benchmark {

    public static void main(String[] args) throws IOException, RunnerException {
//        doBenchmark(new SerializableBenchmark());
//        doBenchmark(new ExternalizableBenchmark());
//        doBenchmark(new DataSerializableBenchmark());
//        doBenchmark(new IdentifiedDataSerializableBenchmark(false));
//        doBenchmark("Unsafe ", new IdentifiedDataSerializableBenchmark(true));
//        doBenchmark(new PortableBenchmark(false));
//        doBenchmark("Unsafe ", new PortableBenchmark(true));
        doBenchmark(new KryoBenchmark());
    }

    private static void doBenchmark(ShoppingCartBenchmark sb) {
        doBenchmark("", sb);
    }

    private static void doBenchmark(String prefix, ShoppingCartBenchmark sb) {
        doBenchmark(prefix + sb.getClass().getSimpleName().
                replace("Benchmark", "") + " Write Performance ", sb, () -> sb.writePerformance());
        doBenchmark(prefix + sb.getClass().getSimpleName().
                replace("Benchmark", "") + " Read Performance", sb, () -> sb.readPerformance());
    }

    private static void doBenchmark(String name, ShoppingCartBenchmark sb, Runnable runnable) {
        System.out.println(name );
        sb.setUp();
        long total = 0;
        int count = 5;
        for (int i = 1; i <= count; i++) {
            System.out.print(name + " :: " + i + " iteration: ");
            long start = System.currentTimeMillis();
            runnable.run();
            long end = System.currentTimeMillis();
            long ops = (long) ShoppingCartBenchmark.OPERATIONS_PER_INVOCATION / (end - start);
            total += ops;
            System.out.println(ops + " ops in ms");
        }
        sb.tearDown();
        System.out.println(name + ":: average " + total / count);
    }
}
