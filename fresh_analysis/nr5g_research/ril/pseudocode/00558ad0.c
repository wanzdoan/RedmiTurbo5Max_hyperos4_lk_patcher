
void onRequest(undefined4 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,
              undefined4 param_5)

{
  undefined4 uVar1;
  
  mtkLogD("RIL-Fusion","[RilFusion] onRequest: request = %d, datalen = %zu, slotId = %d",param_1,
          param_3);
  uVar1 = toRealSlot(param_5);
  rfx_enqueue_request_message(param_1,param_2,param_3,param_4,uVar1);
  return;
}


