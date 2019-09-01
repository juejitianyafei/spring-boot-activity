# SpringBoot集成Activity工作流
## 1.SpringBoot版本2.1.6, Activity版本6.0, 数据库Mysql

## 2.Activity介绍
####一 activity架构
![avatar](/1.png)
####二 7大接口
    RepositoryService：提供一系列管理流程部署和流程定义的API。
    
    RuntimeService：在流程运行时对流程实例进行管理与控制。
    
    TaskService：对流程任务进行管理，例如任务提醒、任务完成和创建任务等。
    
    IdentityService：提供对流程角色数据进行管理的API，这些角色数据包括用户组、用户及它们之间的关系。
    
    ManagementService：提供对流程引擎进行管理和维护的服务。
    
    HistoryService：对流程的历史数据进行操作，包括查询、删除这些历史数据。
####三 28张表
![avatar](/2.png)

    1、act_ge_ 通用数据表，ge是general的缩写
    
    2、act_hi_ 历史数据表，hi是history的缩写，对应HistoryService接口
    
    3、act_id_ 身份数据表，id是identity的缩写，对应IdentityService接口
    
    4、act_re_ 流程存储表，re是repository的缩写，对应RepositoryService接口，存储流程部署和流程定义等静态数据
    
    5、act_ru_ 运行时数据表，ru是runtime的缩写，对应RuntimeService接口和TaskService接口，存储流程实例和用户任务等动态数据
 

####四 activity 配置
    spring.activiti.database-schema-update
    配置项可以设置流程引擎启动和关闭时数据库执行的策略，可以选择四种模式
    
    false：false为默认值，设置为该值后，Activiti在启动时，会对比数据库表中保存的版本，如果没有表或者版本不匹配时，将在启动时抛出异常。
    
    true：设置为该值后，Activiti会对数据库中所有的表进行更新，如果表不存在，则Activiti会自动创建。
    
    create-drop：Activiti启动时，会执行数据库表的创建操作，在Activiti关闭时，执行数据库表的删除操作。
    
    drop-create：Activiti启动时，执行数据库表的删除操作在Activiti关闭时，会执行数据库表的创建操作。
    
    注意
    第一次启动程序后，数据库会自动生成关于activiti28张表，可以关闭程序，修改配置项
    
    #每次应用启动不检查Activiti数据表是否存在及版本号是否匹配，提升应用启动速度
    spring.activiti.database-schema-update=false
    1
    2
    spring.activiti.history-level 配置项
    
    对于历史数据，保存到何种粒度，Activiti提供了history-level属性对其进行配置。history-level属性有点像log4j的日志输出级别，该属性有以下四个值：
    
    none：不保存任何的历史数据，因此，在流程执行过程中，这是最高效的。
    
    activity：级别高于none，保存流程实例与流程行为，其他数据不保存。
    
    audit：除activity级别会保存的数据外，还会保存全部的流程任务及其属性。audit为history的默认值。
    
    full：保存历史数据的最高级别，除了会保存audit级别的数据外，还会保存其他全部流程相关的细节数据，包括一些流程参数等。
    