
/* RfxMclMessage::obtainSapResponse(int, RIL_Errno, RfxBaseData const&, android::sp<RfxMclMessage>,
   bool) */

void __thiscall
RfxMclMessage::obtainSapResponse
          (long *param_1_00,undefined4 param_2,undefined4 param_1,RfxBaseData *param_4,long *param_5
          ,uint param_6)

{
  undefined *puVar1;
  long *plVar2;
  undefined8 uVar3;
  
  plVar2 = operator_new(0xd0);
  android::RefBase::RefBase((RefBase *)(plVar2 + 0x18));
  puVar1 = PTR_vtable_005d2218;
  plVar2[2] = 0;
  plVar2[3] = -1;
  plVar2[4] = -1;
  *(undefined4 *)(plVar2 + 5) = 0xffffffff;
  plVar2[10] = 0;
  plVar2[1] = -0x100000000;
  *plVar2 = (long)(puVar1 + 0x18);
  plVar2[7] = 0;
  plVar2[8] = 0;
  plVar2[6] = 0;
  *(undefined4 *)(plVar2 + 0xb) = 1;
  *(undefined4 *)(plVar2 + 9) = 0;
  plVar2[0xc] = 0;
  *(undefined4 *)(plVar2 + 0xd) = 0xffffffff;
  *(undefined4 *)(plVar2 + 0xe) = 0;
  plVar2[0x17] = 0;
  plVar2[0x18] = (long)(puVar1 + 0x60);
  plVar2[0x14] = 0;
  plVar2[0x15] = 0;
  *(undefined1 *)(plVar2 + 0x16) = 0;
  *param_1_00 = (long)plVar2;
  android::RefBase::incStrong(plVar2 + 0x18);
  *(undefined4 *)(*param_1_00 + 8) = 8;
  *(undefined4 *)(*param_1_00 + 0xc) = param_2;
  *(undefined4 *)(*param_1_00 + 0x48) = param_1;
  *(undefined4 *)(*param_1_00 + 0x18) = *(undefined4 *)(*param_5 + 0x18);
  *(undefined4 *)(*param_1_00 + 0x1c) = *(undefined4 *)(*param_5 + 0x1c);
  *(undefined4 *)(*param_1_00 + 0x24) = *(undefined4 *)(*param_5 + 0x24);
  if ((param_6 & 1) != 0) {
    param_4 = *(RfxBaseData **)(*param_5 + 0x10);
  }
  uVar3 = RfxDataCloneManager::copyData(*(int *)(*param_5 + 0xc),param_4,1);
  *(undefined8 *)(*param_1_00 + 0x10) = uVar3;
  return;
}


