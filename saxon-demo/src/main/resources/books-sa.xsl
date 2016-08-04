<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" indent="yes"></xsl:output>
	
	
	
	<!-- 希望将不用处理的文本扔掉，所以需要一个空白模板来处理文本节点 -->
	<xsl:template match="text()"/>
</xsl:stylesheet>