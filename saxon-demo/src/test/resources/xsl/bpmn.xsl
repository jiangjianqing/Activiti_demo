<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xpath-default-namespace="http://www.omg.org/spec/BPMN/20100524/MODEL">
	<xsl:output method="xml" indent="yes"/>
	<xsl:strip-space elements="*"/>
	
	<xsl:template match="/" >
		<FSPlatform version="1.0.0">
		<xsl:apply-templates/>
		</FSPlatform>	    
	</xsl:template>
	
	<!-- 第一步，处理process节点 -->
	<xsl:template match="process">
		<processList>
			<process isExecutable="true">
			<id>
				<xsl:value-of select="@id"/>
			</id>
			<description>demo show process</description>
			<statement>
				<xsl:apply-templates/>
			</statement>
			</process>
		</processList>
	</xsl:template>
	
	<!--第二步，处理流程入口点 -->
	<xsl:template match="startEvent">
		<startEvent>这是启动事件，算法引擎会自动忽略</startEvent>
		<xsl:variable name="id"><xsl:value-of select="@id"></xsl:value-of></xsl:variable>
		<xsl:call-template name="process_flow">
			<xsl:with-param name="flow" select="../sequenceFlow[@sourceRef=$id]"></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- 第三步，根据流的方向处理其他节点 -->
	<xsl:template name="process_flow">
		<xsl:param name="flow"></xsl:param>
		<xsl:for-each select="$flow">
			<xsl:variable name="target_id" select="@targetRef"></xsl:variable>
			<!-- 
			<xsl:value-of select="$target_id"></xsl:value-of> -->
			<xsl:call-template name="process_targetref">
				<xsl:with-param name="target" select="../*[@id=$target_id]"></xsl:with-param>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>
	
	<!-- 第四步，处理flow对象的target属性所指向的node -->
	<xsl:template name="process_targetref">
		<xsl:param name="target"></xsl:param>
		<xsl:for-each select="$target">
<!--  
			<xsl:value-of select="@id"></xsl:value-of> -->
			<xsl:variable name="node_type">
				<xsl:value-of select="name()"></xsl:value-of>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="$node_type='task'">
					<xsl:call-template name="process_task">
						<xsl:with-param name="task" select="current()"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="$node_type='endEvent'">
					<xsl:call-template name="process_endEvent">

					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					遇到未处理节点，name=<xsl:value-of select="name()"></xsl:value-of>,id=<xsl:value-of select="@id"></xsl:value-of>
				</xsl:otherwise>
			</xsl:choose>
			
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="process_task">
		<xsl:param name="task"></xsl:param>
		<xsl:variable name="task_id">
		<!-- 这里需要再考虑，当前假定当前处理的对象是task，与上游方法强耦合了 -->
			<xsl:value-of select="@id"></xsl:value-of>
		</xsl:variable>
		<xsl:comment>在这里需要处理task对应的业务逻辑 </xsl:comment>
		
		<func id="{$task_id}">
			<name>imshow</name>
		</func>
		
		<xsl:variable name="id"><xsl:value-of select="@id"></xsl:value-of></xsl:variable>
		<xsl:call-template name="process_flow">
			<xsl:with-param name="flow" select="../sequenceFlow[@sourceRef=$id]"></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template name="process_endEvent">
		<endEvent>这是结束事件，算法引擎会自动忽略</endEvent>
		<statement>
			<expression>return</expression>
		</statement>
		
	</xsl:template>
	
</xsl:stylesheet>	