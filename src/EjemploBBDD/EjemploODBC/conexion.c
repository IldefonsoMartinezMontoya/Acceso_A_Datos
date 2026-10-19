#include <stdio.h>
#include <sql.h>
#include <sqlext.h>

int main() {
    SQLHENV env;
    SQLHDBC dbc;
    SQLHSTMT stmt;
    SQLRETURN ret; 

    // Definir el entorno 
    SQLAllocHandle(SQL_HANDLE_ENV, SQL_NULL_HANDLE, &env);
    SQLSetEnvAttr(env, SQL_ATTR_ODBC_VERSION, (void *) SQL_OV_ODBC3, 0);
    SQLAllocHandle(SQL_HANDLE_DBC, env, &dbc);

    // Conectarse al origen de datos Mysql-odbc
    ret = SQLDriverConnect(dbc, NULL, (SQLCHAR*)"DSN=Mysql-odbc;", SQL_NTS, NULL, 0, NULL, SQL_DRIVER_COMPLETE);

    char *consulta = "SELECT * FROM departamentos"; 
    SQLSMALLINT nCols = 0;
    SQLINTEGER nFilas = 0;
    SQLLEN nIndicator = 0;
    SQLCHAR buf[1024] = {0}; 

    if (SQL_SUCCEEDED(ret)) {
        printf("Conectado\n"); 
        SQLAllocHandle(SQL_HANDLE_STMT, dbc, &stmt);
        ret = SQLExecDirect(stmt, (SQLCHAR*)consulta, SQL_NTS);
        SQLNumResultCols(stmt, &nCols);
        SQLRowCount(stmt, &nFilas);
        printf("* Número de Columnas: %d\n", nCols);
        printf("* Número de Filas: %d\n", nFilas); 

        while (SQL_SUCCEEDED(ret = SQLFetch(stmt))) { 
            printf("\n"); 
            for (int i = 1; i <= nCols; ++i) { 
                ret = SQLGetData(stmt, i, SQL_C_CHAR, buf, sizeof(buf), &nIndicator);
                if (SQL_SUCCEEDED(ret)) { 
                    printf("* Columna %s\n", buf); 
                }
            }
        } // while
        printf("\n"); 
        SQLFreeHandle(SQL_HANDLE_STMT, stmt);
        SQLDisconnect(dbc);
    } else {
        printf("NO HA SIDO POSIBLE LA CONEXIÓN\n");
    }

    SQLFreeHandle(SQL_HANDLE_DBC, dbc);
    SQLFreeHandle(SQL_HANDLE_ENV, env);
    return 0;
}