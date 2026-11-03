
void _GLOBAL__sub_I_RmcSuppServRequestNRTHandler_cpp(void)

{
  undefined *puVar1;
  undefined *puVar2;
  
  RfxIdToStringUtils::registerId(0xd31b,"RFX_MSG_REQUEST_GET_XCAP_STATUS");
  puVar2 = PTR_copyDataByObj_005d21a0;
  puVar1 = PTR_copyDataByData_005d2198;
  RfxDataCloneManager::registerRequestId
            ((_func_RfxBaseData_ptr_void_ptr_int *)PTR_copyDataByData_005d2198,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)PTR_copyDataByObj_005d21a0,0xd31b);
  RfxDataCloneManager::registerResponseId
            ((_func_RfxBaseData_ptr_void_ptr_int *)puVar1,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)puVar2,0xd31b);
  RmcSuppServRequestNRTHandler::InitHelper::InitHelper
            ((InitHelper *)PTR_RmcSuppServRequestNRTHandlers_init_val_005d36b8,7,0);
  return;
}


