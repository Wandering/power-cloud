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

import org.apache.sirona.Role;
import org.apache.sirona.store.gauge.AggregatedGaugeDataStoreAdapter;
import org.apache.sirona.store.gauge.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LoggerGaugeDataStore extends AggregatedGaugeDataStoreAdapter {
    public static final Logger logger = LoggerFactory.getLogger(LoggerGaugeDataStore.class.getName());
    private static final Logger SPEC_LOGGER = LoggerFactory.getLogger("GAUGE-LOGGER");

    private static final char SEP = '-';
    private static final String GAUGE_PREFIX = "gauge-";

    @Override
    protected void pushAggregatedGauges(final Map<Role, Value> gauges) {
        try {
            final long ts = System.currentTimeMillis();

            for (final Map.Entry<Role, Value> gauge : gauges.entrySet()) {
                SPEC_LOGGER.error(GAUGE_PREFIX + gauge.getKey().getName() + SEP + gauge.getValue().getMean() + SEP + ts);
            }
        } catch (final Exception e) {
            logger.error("error exception", e);
        }
    }
}
