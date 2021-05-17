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
package com.manning.javapersistence.ch09.onetomany.jointable;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;

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
            Item otherItem = new Item("Other Item");
            em.persist(otherItem);

            User someUser = new User("johndoe");
            someUser.getBoughtItems().add(someItem); // Link
            someItem.setBuyer(someUser); // Link
            someUser.getBoughtItems().add(otherItem);
            otherItem.setBuyer(someUser);
            em.persist(someUser);

            Item unsoldItem = new Item("Unsold Item");
            em.persist(unsoldItem);

            em.getTransaction().commit();

            em.close();

        } finally {
            emf.close();
        }
    }
}
