package com.shortthirdman.sharedlibs.common;

import java.util.Map;

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
    SAPDB("sap","org.hibernate.dialect.SAPDBDialect"),
    INFORMIX("informix","org.hibernate.dialect.InformixDialect"),
    HYPERSONICSQL("hypersonic","org.hibernate.dialect.HSQLDialect"),
    INGRES("ingres","org.hibernate.dialect.IngresDialect"),
    PROGRESS("progress","org.hibernate.dialect.ProgressDialect"),
    MCKOISQL("mckoi","org.hibernate.dialect.MckoiDialect"),
    INTERBASE("interbase","org.hibernate.dialect.InterbaseDialect"),
    POINTBASE("pointbase","org.hibernate.dialect.PointbaseDialect"),
    FRONTBASE("frontbase","org.hibernate.dialect.FrontbaseDialect"),
    FIREBIRD("firebird","org.hibernate.dialect.FirebirdDialect");

    private final String vendorName;
    private final String driverClassName;

    HibernateDialects(String vendor, String driverClassName) {
        this.vendorName = vendor;
        this.driverClassName = driverClassName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vendor=").append(vendorName).append(", Driver Class=").append(driverClassName);
        return sb.toString();
    }

    public String getDriveClassName() {
        return driverClassName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public static HibernateDialects fromString(String parameterName) {
        if (parameterName != null) {
            for (HibernateDialects obj : HibernateDialects.values()) {
                if (parameterName.equals(obj.driverClassName)) {
                    return obj;
                }
            }
        }
        return null;
    }

    public static HibernateDialects from(String vendor) {
        if (vendor != null) {
            for (HibernateDialects objType : HibernateDialects.values()) {
                if (objType.vendorName.equals(vendor)) {
                    return objType;
                }
            }
        }
        return null;
    }
}
