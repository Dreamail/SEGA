/**
 * Copyright (C) 2011 SEGA Corporation. All rights reserved.
 */
package jp.co.sega.allnet.auth.service.util;

/**
 * @author NakanoY
 * 
 */
public interface SequenceGenerator {

    String generatePlaceId();

    int generateRegionId(String countryCode, int level);

}
