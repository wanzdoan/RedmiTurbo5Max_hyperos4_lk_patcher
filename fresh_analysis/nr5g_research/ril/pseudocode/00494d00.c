
void _GLOBAL__sub_I_RmcNetworkNrtRequestHandler_cpp(void)

{
  undefined *puVar1;
  undefined *puVar2;
  undefined *puVar3;
  undefined *puVar4;
  
  RfxIdToStringUtils::registerId(0xc745,"RFX_MSG_REQUEST_QUERY_AVAILABLE_NETWORKS");
  puVar4 = PTR_copyDataByObj_005d21a0;
  puVar3 = PTR_copyDataByData_005d2198;
  RfxDataCloneManager::registerRequestId
            ((_func_RfxBaseData_ptr_void_ptr_int *)PTR_copyDataByData_005d2198,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)PTR_copyDataByObj_005d21a0,0xc745);
  puVar2 = PTR_copyDataByObj_005d1cc0;
  puVar1 = PTR_copyDataByData_005d1cb8;
  RfxDataCloneManager::registerResponseId
            ((_func_RfxBaseData_ptr_void_ptr_int *)PTR_copyDataByData_005d1cb8,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)PTR_copyDataByObj_005d1cc0,0xc745);
  RfxIdToStringUtils::registerId(0xc746,"RFX_MSG_REQUEST_QUERY_AVAILABLE_NETWORKS_WITH_ACT");
  RfxDataCloneManager::registerRequestId
            ((_func_RfxBaseData_ptr_void_ptr_int *)puVar3,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)puVar4,0xc746);
  RfxDataCloneManager::registerResponseId
            ((_func_RfxBaseData_ptr_void_ptr_int *)puVar1,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)puVar2,0xc746);
  RfxIdToStringUtils::registerId(0xc762,"RFX_MSG_RIL_REQUEST_START_NETWORK_SCAN");
  RfxDataCloneManager::registerRequestId
            ((_func_RfxBaseData_ptr_void_ptr_int *)PTR_copyDataByData_005d1e28,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)PTR_copyDataByObj_005d1e30,0xc762);
  RfxDataCloneManager::registerResponseId
            ((_func_RfxBaseData_ptr_void_ptr_int *)puVar3,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)puVar4,0xc762);
  RfxIdToStringUtils::registerId(0xc756,"RFX_MSG_REQUEST_GET_FEMTOCELL_LIST");
  RfxDataCloneManager::registerRequestId
            ((_func_RfxBaseData_ptr_void_ptr_int *)puVar3,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)puVar4,0xc756);
  RfxDataCloneManager::registerResponseId
            ((_func_RfxBaseData_ptr_void_ptr_int *)puVar1,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)puVar2,0xc756);
  RfxIdToStringUtils::registerId(0xc7d2,"RFX_MSG_EVENT_SIMULATE_NETWORK_SCAN");
  RfxDataCloneManager::registerEventId
            ((_func_RfxBaseData_ptr_void_ptr_int *)puVar3,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)puVar4,0xc7d2);
  RmcNetworkNrtRequestHandler::InitHelper::InitHelper
            ((InitHelper *)PTR_RmcNetworkNrtRequestHandlers_init_val_005d3f60,7,0);
  return;
}


