/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.power.cloud.monitor;

import org.apache.sirona.counters.Counter;
import org.apache.sirona.counters.MetricData;
import org.apache.sirona.store.counter.BatchCounterDataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class LoggerCounterDataStore extends BatchCounterDataStore {
    public static final Logger logger = LoggerFactory.getLogger(LoggerGaugeDataStore.class.getName());
    private static final Logger SPEC_LOGGER = LoggerFactory.getLogger("COUNT-LOGGER");

    private static final String COUNTER_PREFIX = "counter-";
    private static final char SEP = '-';

    @Override
    protected synchronized void pushCountersByBatch(final Collection<Counter> instances) {
        try {
            final long ts = System.currentTimeMillis();

            for (final Counter counter : instances) {
                final Counter.Key key = counter.getKey();
                final String prefix = COUNTER_PREFIX + key.getRole().getName() + SEP + key.getName() + SEP;

                for (final MetricData data : MetricData.values()) {
                    SPEC_LOGGER.error(
                            prefix + data.name() + SEP +
                            data.value(counter) + SEP +
                            ts);
                }
            }
        } catch (final Exception e) {
            logger.error("logger exception", e);
        }
    }

}
