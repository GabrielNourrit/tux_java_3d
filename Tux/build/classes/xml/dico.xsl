<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:dico='Dico'>
    <xsl:output method="html"/>


    <xsl:template match="/">
        <html>
            <head>
                <title>dico.xsl</title>
            </head>
            <body>
                <ul>        
                    <xsl:apply-templates select="//dico:mot">
                        <xsl:sort select="@niveau" order="ascending">
                            <xsl:sort select="./text()" order="ascending"/>
                        </xsl:sort>
                    </xsl:apply-templates>
                </ul>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="dico:mot">
        <li> Niveau <xsl:value-of select="@niveau"/> : <xsl:value-of select="./text()"/></li>
    </xsl:template>


</xsl:stylesheet>
