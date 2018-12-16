<?xml version="1.0" encoding="UTF-8"?>


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:tux="http://myGame/tux">
    <xsl:output method="html"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>profile.xsl</title>
                <!-- CSS <link .........> -->
            </head>
            <body>
                <h1>Bienvenue sur la page profil joueur</h1>
                <h2>Nom : <xsl:value-of select="//tux:name"/></h2>
                <hr/>
                <xsl:element name="img">
                    <xsl:attribute name="src"><xsl:value-of select="//tux:avatar"/></xsl:attribute>
                    <xsl:attribute name="alt">avatar joueur</xsl:attribute>
                    <hr/>
                </xsl:element>

                
                <p>
                    Date de naissance : <xsl:value-of select="//tux:birthday"/>
                </p>
                <hr/>
                <ol>
                    <xsl:apply-templates select="//tux:games/tux:game"/>                     
                </ol>
                 <hr/>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="tux:game">
        <li>
            <ul>
                <li>Temps : <xsl:value-of select="tux:time"/> secondes</li>
                <li>Mot : <xsl:value-of select="tux:word"/></li>
                <li>Trouvé : <xsl:value-of select="@found"/></li>              
            </ul>
        </li>
       <hr/>
       <br/>
    </xsl:template>
</xsl:stylesheet>