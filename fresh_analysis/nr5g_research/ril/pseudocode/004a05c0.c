
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void _GLOBAL__sub_I_RmcNrSwitchHandler_cpp(void)

{
  pthread_mutexattr_init((pthread_mutexattr_t *)&DAT_005ed3a8);
  pthread_mutex_init((pthread_mutex_t *)&DAT_005ed3b0,(pthread_mutexattr_t *)&DAT_005ed3a8);
  DAT_005ed408 = 0;
  uRam00000000005ed3e0 = 0;
  _DAT_005ed3d8 = 0;
  uRam00000000005ed3f0 = 0;
  _DAT_005ed3e8 = 0;
  uRam00000000005ed400 = 0;
  _DAT_005ed3f8 = 0;
  RfxIdToStringUtils::registerId(0xc76c,"RFX_MSG_REQUEST_NR_SWITCH");
  RfxDataCloneManager::registerRequestId
            ((_func_RfxBaseData_ptr_void_ptr_int *)PTR_copyDataByData_005d2200,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)PTR_copyDataByObj_005d2208,0xc76c);
  RfxDataCloneManager::registerResponseId
            ((_func_RfxBaseData_ptr_void_ptr_int *)PTR_copyDataByData_005d2198,
             (_func_RfxBaseData_ptr_RfxBaseData_ptr *)PTR_copyDataByObj_005d21a0,0xc76c);
  RmcNrSwitchHandler::InitHelper::InitHelper
            ((InitHelper *)PTR_RmcNrSwitchHandlers_init_val_005d3fd8,8,0);
  return;
}


