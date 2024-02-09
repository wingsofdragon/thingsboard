/**
 * Copyright © 2016-2024 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.server.dao.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.HasId;
import org.thingsboard.server.common.data.id.QueueStatsId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.queue.QueueStats;
import org.thingsboard.server.dao.entity.AbstractEntityService;
import org.thingsboard.server.dao.service.DataValidator;

import java.util.List;
import java.util.Optional;

import static org.thingsboard.server.dao.service.Validator.validateId;

@Service("QueueStatsDaoService")
@Slf4j
@RequiredArgsConstructor
public class BaseQueueStatsService extends AbstractEntityService implements QueueStatsService {

    public static final String INCORRECT_TENANT_ID = "Incorrect tenantId ";

    private final QueueStatsDao queueStatsDao;

    private final DataValidator<QueueStats> queueStatsValidator;

    @Override
    public QueueStats save(TenantId tenantId, QueueStats queueStats) {
        log.trace("Executing save [{}]", queueStats);
        queueStatsValidator.validate(queueStats, QueueStats::getTenantId);
        return queueStatsDao.save(tenantId, queueStats);
    }

    @Override
    public QueueStats findQueueStatsById(TenantId tenantId, QueueStatsId queueStatsId) {
        log.trace("Executing findQueueStatsById [{}]", queueStatsId);
        validateId(queueStatsId, "Incorrect queueStatsId " + queueStatsId);
        return queueStatsDao.findById(tenantId, queueStatsId.getId());
    }

    @Override
    public QueueStats findByTenantIdAndNameAndServiceId(TenantId tenantId, String queueName, String serviceId) {
        log.trace("Executing findByTenantIdAndNameAndServiceId, tenantId: [{}], queueName: [{}], serviceId: [{}]", tenantId, queueName, serviceId);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        return queueStatsDao.findByTenantIdQueueNameAndServiceId(tenantId, queueName, serviceId);
    }

    @Override
    public List<QueueStats> findByTenantId(TenantId tenantId) {
        log.trace("Executing findByTenantId, tenantId: [{}]", tenantId);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        return queueStatsDao.findByTenantId(tenantId);
    }

    @Override
    public void deleteByTenantId(TenantId tenantId) {
        log.trace("Executing deleteDevicesByTenantId, tenantId [{}]", tenantId);
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        queueStatsDao.deleteByTenantId(tenantId);
    }

    @Override
    public Optional<HasId<?>> findEntity(TenantId tenantId, EntityId entityId) {
        return Optional.ofNullable(findQueueStatsById(tenantId, new QueueStatsId(entityId.getId())));
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.QUEUE_STATS;
    }
}
