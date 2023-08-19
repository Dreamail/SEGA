/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.common.entity.util;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;

/**
 * @author NakanoY
 * 
 */
@Entity
@SqlResultSetMappings({ @SqlResultSetMapping(name = "optDeliverReportMapping", entities = { @EntityResult(entityClass = OptDeliverReportView.class) }) })
public class OptDeliverReportView extends AbstractDeliverReportView implements
        Serializable {

    private static final long serialVersionUID = 1L;

}
