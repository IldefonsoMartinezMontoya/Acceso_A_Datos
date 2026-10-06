<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>
    <xsl:template match="/">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>
                    <xsl:value-of select="name(/*)"/>
                </title>
                <!-- TODO: puedes añadir aquí estilos CSS -->
            </head>
            <body>
                <h1>
                    <xsl:value-of select="name(/*)"/>
                </h1>

                <table border="1">
                    <thead>
                        <tr>
                            <!-- Crea una columna por cada campo del primer registro -->
                            <xsl:for-each select="/*/*[1]/*">
                                <th>
                                    <xsl:value-of select="name()"/>
                                </th>
                            </xsl:for-each>
                        </tr>
                    </thead>

                    <tbody>
                        <!-- Recorre los registros que cuelgan de la raíz -->
                        <xsl:for-each select="/*/*">
                            <tr>
                                <!-- Escribe una celda por cada campo del registro -->
                                <xsl:for-each select="*">
                                    <td>
                                        <xsl:value-of select="."/>
                                    </td>
                                </xsl:for-each>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>