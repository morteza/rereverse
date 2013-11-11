/*******************************************************************************
 *        File: DeliveryMapping.java
 *      Author: Morteza Ansarinia <ansarinia@me.com>
 *  Created on: Nov 10, 2013
 *     Project: ReReverse
 *   Copyright: See the file "LICENSE" for the full license governing this code.
 *******************************************************************************/
package rereverse.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="delivery_mapping")
public class DeliveryMapping {
	
	@Id
	Integer id;
	
	public String uri;
	
	public String remoteHost;
	
	public int remotePort = 80;
	
	@Version
	Timestamp modifiedAt;
	
	public DeliveryMapping(String uri, String remoteHost) {
		this.uri = uri;
		this.remoteHost = remoteHost;
	}
}
