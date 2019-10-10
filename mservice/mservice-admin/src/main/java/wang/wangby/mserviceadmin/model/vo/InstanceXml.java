package wang.wangby.mserviceadmin.model.vo;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * <instance>
 *     <instanceId>mservice-admin1</instanceId>
 *     <hostName>192.168.56.1</hostName>
 *     <app>MSERVICE-ADMIN</app>
 *     <ipAddr>192.168.56.1</ipAddr>
 *     <status>DOWN</status>
 *     <overriddenstatus>UNKNOWN</overriddenstatus>
 *     <port enabled="true">8080</port>
 *     <securePort enabled="false">443</securePort>
 *     <countryId>1</countryId>
 *     <dataCenterInfo class="com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo">
 *         <name>MyOwn</name>
 *     </dataCenterInfo>
 *     <leaseInfo>
 *         <renewalIntervalInSecs>30</renewalIntervalInSecs>
 *         <durationInSecs>90</durationInSecs>
 *         <registrationTimestamp>1570614012699</registrationTimestamp>
 *         <lastRenewalTimestamp>1570614162715</lastRenewalTimestamp>
 *         <evictionTimestamp>0</evictionTimestamp>
 *         <serviceUpTimestamp>0</serviceUpTimestamp>
 *     </leaseInfo>
 *     <metadata>
 *         <management.port>8080</management.port>
 *     </metadata>
 *     <homePageUrl>http://192.168.56.1:8080/</homePageUrl>
 *     <statusPageUrl>http://192.168.56.1:8080/actuator/info</statusPageUrl>
 *     <healthCheckUrl>http://192.168.56.1:8080/actuator/health</healthCheckUrl>
 *     <vipAddress>mservice-admin</vipAddress>
 *     <secureVipAddress>mservice-admin</secureVipAddress>
 *     <isCoordinatingDiscoveryServer>false</isCoordinatingDiscoveryServer>
 *     <lastUpdatedTimestamp>1570614012699</lastUpdatedTimestamp>
 *     <lastDirtyTimestamp>1570614010673</lastDirtyTimestamp>
 *     <actionType>ADDED</actionType>
 * </instance>
 * */
@XmlRootElement(name = "instance")
@Data
public class InstanceXml {
    private String homePageUrl;
    private String status;
}
