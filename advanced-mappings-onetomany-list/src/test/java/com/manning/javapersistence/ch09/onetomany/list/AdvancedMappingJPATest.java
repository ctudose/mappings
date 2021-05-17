/*
 * ========================================================================
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ========================================================================
 */
package com.manning.javapersistence.ch09.onetomany.list;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdvancedMappingJPATest {

    @Test
    void storeLoadEntities() {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("ch09.advancedmapingsonetomany");

        try {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();

            Item someItem = new Item("Some Item");
            em.persist(someItem);

            Bid someBid = new Bid(new BigDecimal("123.00"), someItem);
            someItem.addBid(someBid);
            em.persist(someBid);

            Bid secondBid = new Bid(new BigDecimal("456.00"), someItem);
            someItem.addBid(secondBid);
            em.persist(secondBid);

            em.getTransaction().commit();

            em.getTransaction().begin();

            Item item = em.find(Item.class, someItem.getId());
            List<Bid> bids = item.getBids();

            em.getTransaction().commit();

            assertAll(
                    () -> assertEquals(2, bids.size()),
                    () -> assertEquals(0, bids.get(0).getAmount().compareTo(new BigDecimal("123"))),
                    () -> assertEquals(0, bids.get(1).getAmount().compareTo(new BigDecimal("456")))
            );

            em.close();

        } finally {
            emf.close();
        }
    }
}
