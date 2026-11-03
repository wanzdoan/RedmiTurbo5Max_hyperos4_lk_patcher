
void _GLOBAL__sub_I_RmcDcCommonReqHandler_cpp(void)

{
  undefined *puVar1;
  undefined *puVar2;
  undefined *puVar3;
  undefined *puVar4;
  
  RfxIdToStringUtils::registerId(0xcd45,"RFX_MSG_UNSOL_PCO_DATA");
  RfxDataCloneManager::registerUrcId
            ((_func_RfxBaseData_ptr_void_ptr_int *)PTR_copyDataByData_005d1710,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)PTR_copyDataByObj_005d1718,0xcd45);
  RfxIdToStringUtils::registerId(0xcd46,"RFX_MSG_UNSOL_PCO_DATA_AFTER_ATTACHED");
  RfxDataCloneManager::registerUrcId
            ((_func_RfxBaseData_ptr_void_ptr_int *)PTR_copyDataByData_005d1ba0,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)PTR_copyDataByObj_005d1ba8,0xcd46);
  RfxIdToStringUtils::registerId(0xcd49,"RFX_MSG_URC_QUALIFIED_NETWORK_TYPES_CHANGED");
  puVar4 = PTR_copyDataByObj_005d2208;
  puVar3 = PTR_copyDataByData_005d2200;
  RfxDataCloneManager::registerUrcId
            ((_func_RfxBaseData_ptr_void_ptr_int *)PTR_copyDataByData_005d2200,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)PTR_copyDataByObj_005d2208,0xcd49);
  RfxIdToStringUtils::registerId(0xcd64,"RFX_MSG_EVENT_QUERY_PCO_WITH_AID");
  RfxDataCloneManager::registerEventId
            ((_func_RfxBaseData_ptr_void_ptr_int *)puVar3,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)puVar4,0xcd64);
  RfxIdToStringUtils::registerId(0xcd67,"RFX_MSG_EVENT_REQ_DATA_CONTEXT_IDS");
  puVar2 = PTR_copyDataByObj_005d21a0;
  puVar1 = PTR_copyDataByData_005d2198;
  RfxDataCloneManager::registerEventId
            ((_func_RfxBaseData_ptr_void_ptr_int *)PTR_copyDataByData_005d2198,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)PTR_copyDataByObj_005d21a0,0xcd67);
  RfxIdToStringUtils::registerId(0xcd69,"RFX_MSG_EVENT_SEND_QUALIFIED_NETWORK_TYPES_CHANGED");
  RfxDataCloneManager::registerEventId
            ((_func_RfxBaseData_ptr_void_ptr_int *)puVar3,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)puVar4,0xcd69);
  RfxIdToStringUtils::registerId(0xcd6a,"RFX_MSG_EVENT_QUERY_SELF_IA");
  RfxDataCloneManager::registerEventId
            ((_func_RfxBaseData_ptr_void_ptr_int *)puVar1,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)puVar2,0xcd6a);
  RfxIdToStringUtils::registerId(0xcd4a,"RFX_MSG_URC_MOBILE_DATA_USAGE");
  RfxDataCloneManager::registerUrcId
            ((_func_RfxBaseData_ptr_void_ptr_int *)puVar3,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)puVar4,0xcd4a);
  RfxIdToStringUtils::registerId(0xcd4b,"RFX_MSG_URC_KEEPALIVE_STATUS");
  RfxDataCloneManager::registerUrcId
            ((_func_RfxBaseData_ptr_void_ptr_int *)PTR_copyDataByData_005d1ff0,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)PTR_copyDataByObj_005d1ff8,0xcd4b);
  RfxIdToStringUtils::registerId(0xcd4c,"RFX_MSG_URC_NW_LIMIT");
  RfxDataCloneManager::registerUrcId
            ((_func_RfxBaseData_ptr_void_ptr_int *)puVar3,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)puVar4,0xcd4c);
  RfxIdToStringUtils::registerId(0xcd6e,"RFX_MSG_EVENT_KERNEL_ADDRESS_UPDATED");
  RfxDataCloneManager::registerEventId
            ((_func_RfxBaseData_ptr_void_ptr_int *)PTR_copyDataByData_005d1be8,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)PTR_copyDataByObj_005d1bf0,0xcd6e);
  RfxIdToStringUtils::registerId(0xcd75,"RFX_MSG_EVENT_RF_OCCUPY_TIMEOUT");
  RfxDataCloneManager::registerEventId
            ((_func_RfxBaseData_ptr_void_ptr_int *)puVar3,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)puVar4,0xcd75);
  return;
}


