package com.shortthirdman.primekit.essentials.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public enum HibernateDialects {

    ORACLE("oracle", "org.hibernate.dialect.OracleDialect"),
    ORACLE_9I("oracle9i", "org.hibernate.dialect.Oracle9iDialect"),
    ORACLE_10G("oracle10g", "org.hibernate.dialect.Oracle10gDialect"),
    MYSQL("mysql", "org.hibernate.dialect.MySQLDialect"),
    MYSQL_INNODB("mysqlinnodb", "org.hibernate.dialect.MySQLInnoDBDialect"),
    MYSQL_MYISAM("mysqlmyisam", "org.hibernate.dialect.MySQLMyISAMDialect"),
    DB2("db2","org.hibernate.dialect.DB2Dialect"),
    DB2AS400("db2as400","org.hibernate.dialect.DB2400Dialect"),
    DB2OS390("db2os390","org.hibernate.dialect.DB2390Dialect"),
    MSSQL_SERVER("mssql","org.hibernate.dialect.SQLServerDialect"),
    SYBASE("sybase","org.hibernate.dialect.SybaseDialect"),
    SYBASE_ANYWHERE("sybaseanywhere","org.hibernate.dialect.SybaseAnywhereDialect"),
    POSTGRESQL("postgres","org.hibernate.dialect.PostgreSQLDialect"),
    SAP_DB("sap","org.hibernate.dialect.SAPDBDialect"),
    INFORMIX("informix","org.hibernate.dialect.InformixDialect"),
    HYPERSONIC_SQL("hypersonic","org.hibernate.dialect.HSQLDialect"),
    INGRES("ingres","org.hibernate.dialect.IngresDialect"),
    PROGRESS("progress","org.hibernate.dialect.ProgressDialect"),
    MCKOI_SQL("mckoi","org.hibernate.dialect.MckoiDialect"),
    INTERBASE("interbase","org.hibernate.dialect.InterbaseDialect"),
    POINT_BASE("pointbase","org.hibernate.dialect.PointbaseDialect"),
    FRONT_BASE("frontbase","org.hibernate.dialect.FrontbaseDialect"),
    FIREBIRD("firebird","org.hibernate.dialect.FirebirdDialect");

    private final String vendorName;
    private final String driverClassName;

    HibernateDialects(String vendorName, String driverClassName) {
        this.vendorName = vendorName;
        this.driverClassName = driverClassName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vendor=").append(vendorName).append(", Driver Class=").append(driverClassName);
        return sb.toString();
    }

    /**
     * @param driverClass
     * @return
     */
    public static HibernateDialects fromDriverClass(String driverClass) {
        if (driverClass != null) {
            for (HibernateDialects obj : HibernateDialects.values()) {
                if (driverClass.equals(obj.driverClassName)) {
                    return obj;
                }
            }
        }
        return null;
    }

    public static HibernateDialects fromVendor(String vendor) {
        if (vendor != null) {
            Optional<HibernateDialects> firstMatch = Arrays.stream(HibernateDialects.values())
                    .sequential()
                    .filter(obj -> obj.vendorName.equals(vendor))
                    .findAny();
            return firstMatch.orElse(null);
        }

        return null;
    }
}
