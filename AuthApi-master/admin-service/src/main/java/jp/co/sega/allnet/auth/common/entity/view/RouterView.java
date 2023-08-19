package jp.co.sega.allnet.auth.common.entity.view;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;

/**
 * The persistent class for the ROUTERS database table.
 * 
 */
@Entity
@NamedNativeQueries({
        @NamedNativeQuery(name = "findMachineRouterByAllnetId", query = "SELECT DISTINCT r.router_id, p.place_id, p.name AS place_name, r.router_type_id, rt.name AS router_type_name, r.lc_type_id, lc.name AS lc_type_name, r.place_ip, r.allnet_id FROM routers r LEFT OUTER JOIN router_types rt ON r.router_type_id = rt.router_type_id LEFT OUTER JOIN lc_types lc ON r.lc_type_id = lc.lc_type_id INNER JOIN places p ON r.allnet_id = p.allnet_id WHERE p.allnet_id = :allnetId ORDER BY r.router_id", resultClass = RouterView.class),
        @NamedNativeQuery(name = "findMachineRouterByAllnetIdAndGameIds", query = "SELECT DISTINCT r.router_id, p.place_id, p.name AS place_name, r.router_type_id, rt.name AS router_type_name, r.lc_type_id, lc.name AS lc_type_name, r.place_ip, r.allnet_id FROM routers r LEFT OUTER JOIN router_types rt ON r.router_type_id = rt.router_type_id LEFT OUTER JOIN lc_types lc ON r.lc_type_id = lc.lc_type_id INNER JOIN places p ON r.allnet_id = p.allnet_id INNER JOIN machines m ON p.allnet_id = m.allnet_id WHERE p.allnet_id = :allnetId AND (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) ORDER BY r.router_id", resultClass = RouterView.class),

        @NamedNativeQuery(name = "findMachineRouterByPlaceId", query = "SELECT DISTINCT r.router_id, p.place_id, p.name AS place_name, r.router_type_id, rt.name AS router_type_name, r.lc_type_id, lc.name AS lc_type_name, r.place_ip, r.allnet_id FROM routers r LEFT OUTER JOIN router_types rt ON r.router_type_id = rt.router_type_id LEFT OUTER JOIN lc_types lc ON r.lc_type_id = lc.lc_type_id INNER JOIN places p ON r.allnet_id = p.allnet_id WHERE p.place_id = :placeId ORDER BY r.router_id", resultClass = RouterView.class),
        @NamedNativeQuery(name = "findMachineRouterByPlaceIdAndGameIds", query = "SELECT DISTINCT r.router_id, p.place_id, p.name AS place_name, r.router_type_id, rt.name AS router_type_name, r.lc_type_id, lc.name AS lc_type_name, r.place_ip, r.allnet_id FROM routers r LEFT OUTER JOIN router_types rt ON r.router_type_id = rt.router_type_id LEFT OUTER JOIN lc_types lc ON r.lc_type_id = lc.lc_type_id INNER JOIN places p ON r.allnet_id = p.allnet_id INNER JOIN machines m ON p.allnet_id = m.allnet_id WHERE p.place_id = :placeId AND (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) ORDER BY r.router_id", resultClass = RouterView.class),

        @NamedNativeQuery(name = "findMachineRouterByRouterId", query = "SELECT DISTINCT r.router_id, p.place_id, p.name AS place_name, r.router_type_id, rt.name AS router_type_name, r.lc_type_id, lc.name AS lc_type_name, r.place_ip, r.allnet_id FROM routers r LEFT OUTER JOIN router_types rt ON r.router_type_id = rt.router_type_id LEFT OUTER JOIN lc_types lc ON r.lc_type_id = lc.lc_type_id INNER JOIN places p ON r.allnet_id = p.allnet_id WHERE r.router_id = :routerId ORDER BY r.router_id", resultClass = RouterView.class),
        @NamedNativeQuery(name = "findMachineRouterByRouterIdAndGameIds", query = "SELECT DISTINCT r.router_id, p.place_id, p.name AS place_name, r.router_type_id, rt.name AS router_type_name, r.lc_type_id, lc.name AS lc_type_name, r.place_ip, r.allnet_id FROM routers r LEFT OUTER JOIN router_types rt ON r.router_type_id = rt.router_type_id LEFT OUTER JOIN lc_types lc ON r.lc_type_id = lc.lc_type_id INNER JOIN places p ON r.allnet_id = p.allnet_id INNER JOIN machines m  ON p.allnet_id = m.allnet_id WHERE r.router_id = :routerId AND (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) ORDER BY r.router_id", resultClass = RouterView.class),

        @NamedNativeQuery(name = "findMachineRouterByIp", query = "SELECT DISTINCT r.router_id, p.place_id, p.name AS place_name, r.router_type_id, rt.name AS router_type_name, r.lc_type_id, lc.name AS lc_type_name, r.place_ip, r.allnet_id FROM routers r LEFT OUTER JOIN router_types rt ON r.router_type_id = rt.router_type_id LEFT OUTER JOIN lc_types lc ON r.lc_type_id = lc.lc_type_id INNER JOIN places p ON r.allnet_id = p.allnet_id WHERE r.place_ip = :ip ORDER BY r.router_id", resultClass = RouterView.class),
        @NamedNativeQuery(name = "findMachineRouterByIpAndGameIds", query = "SELECT DISTINCT r.router_id, p.place_id, p.name AS place_name, r.router_type_id, rt.name AS router_type_name, r.lc_type_id, lc.name AS lc_type_name, r.place_ip, r.allnet_id FROM routers r LEFT OUTER JOIN router_types rt ON r.router_type_id = rt.router_type_id LEFT OUTER JOIN lc_types lc ON r.lc_type_id = lc.lc_type_id INNER JOIN places p ON r.allnet_id = p.allnet_id INNER JOIN machines m ON p.allnet_id = m.allnet_id WHERE r.place_ip = :ip AND (m.game_id IN (:gameIds) OR m.reserved_game_id IN (:gameIds)) ORDER BY r.router_id", resultClass = RouterView.class) })
public class RouterView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ROUTER_ID", nullable = false)
    private String routerId;

    @Column(name = "PLACE_ID", nullable = false)
    private String placeId;

    @Column(name = "PLACE_NAME", nullable = false)
    private String placeName;

    @Column(name = "ROUTER_TYPE_ID", nullable = false)
    private BigDecimal routerTypeId;

    @Column(name = "ROUTER_TYPE_NAME")
    private String routerTypeName;

    @Column(name = "LC_TYPE_ID")
    private BigDecimal lcTypeId;

    @Column(name = "LC_TYPE_NAME")
    private String lcTypeName;

    @Column(name = "PLACE_IP", nullable = false)
    private String placeIp;

    @Column(name = "ALLNET_ID", nullable = false)
    private BigDecimal allnetId;

    /**
     * @return the routerId
     */
    public String getRouterId() {
        return routerId;
    }

    /**
     * @param routerId
     *            the routerId to set
     */
    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    /**
     * @return the placeId
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * @param placeId
     *            the placeId to set
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * @return the placeName
     */
    public String getPlaceName() {
        return placeName;
    }

    /**
     * @param placeName
     *            the placeName to set
     */
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    /**
     * @return the routerTypeId
     */
    public BigDecimal getRouterTypeId() {
        return routerTypeId;
    }

    /**
     * @param routerTypeId
     *            the routerTypeId to set
     */
    public void setRouterTypeId(BigDecimal routerTypeId) {
        this.routerTypeId = routerTypeId;
    }

    /**
     * @return the routerTypeName
     */
    public String getRouterTypeName() {
        return routerTypeName;
    }

    /**
     * @param routerTypeName
     *            the routerTypeName to set
     */
    public void setRouterTypeName(String routerTypeName) {
        this.routerTypeName = routerTypeName;
    }

    /**
     * @return the lcTypeId
     */
    public BigDecimal getLcTypeId() {
        return lcTypeId;
    }

    /**
     * @param lcTypeId
     *            the lcTypeId to set
     */
    public void setLcTypeId(BigDecimal lcTypeId) {
        this.lcTypeId = lcTypeId;
    }

    /**
     * @return the lcTypeName
     */
    public String getLcTypeName() {
        return lcTypeName;
    }

    /**
     * @param lcTypeName
     *            the lcTypeName to set
     */
    public void setLcTypeName(String lcTypeName) {
        this.lcTypeName = lcTypeName;
    }

    /**
     * @return the placeIp
     */
    public String getPlaceIp() {
        return placeIp;
    }

    /**
     * @param placeIp
     *            the placeIp to set
     */
    public void setPlaceIp(String placeIp) {
        this.placeIp = placeIp;
    }

    /**
     * @return the allnetId
     */
    public BigDecimal getAllnetId() {
        return allnetId;
    }

    /**
     * @param allnetId
     *            the allnetId to set
     */
    public void setAllnetId(BigDecimal allnetId) {
        this.allnetId = allnetId;
    }

}