
void onSapRequest(int param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,
                 undefined4 param_5)

{
  long lVar1;
  undefined1 local_4c [4];
  long local_48;
  
  lVar1 = tpidr_el0;
  local_48 = *(long *)(lVar1 + 0x28);
  mtkLogD("RIL-Fusion","[RilFusion] onSapRequest: %d",param_1);
  if (param_1 - 0xbU < 0xfffffff6) {
    mtkLogE("RIL-Fusion","invalid request");
    local_4c[0] = 1;
    sendSapResponseComplete(param_4,3,9,local_4c);
    if (*(long *)(lVar1 + 0x28) == local_48) {
      return;
    }
  }
  else if (*(long *)(lVar1 + 0x28) == local_48) {
    rfx_enqueue_sap_request_message(param_1,param_2,param_3,param_4,param_5);
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


