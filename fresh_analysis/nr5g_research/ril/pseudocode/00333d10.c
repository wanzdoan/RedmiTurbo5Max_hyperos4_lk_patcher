
/* RfxMclMessage::obtainResponse(RIL_Errno, RfxBaseData const&, android::sp<RfxMclMessage>) */

void __thiscall
RfxMclMessage::obtainResponse(long *param_1,undefined4 param_2,RfxBaseData *param_3,long *param_4)

{
  int iVar1;
  long lVar2;
  undefined *puVar3;
  long *plVar4;
  undefined8 uVar5;
  long *local_60;
  long local_58;
  
  lVar2 = tpidr_el0;
  local_58 = *(long *)(lVar2 + 0x28);
  local_60 = (long *)*param_4;
  iVar1 = *(int *)((long)local_60 + 0xc);
  android::RefBase::incStrong((void *)((long)local_60 + *(long *)(*local_60 + -0x18)));
  plVar4 = operator_new(0xd0);
  android::RefBase::RefBase((RefBase *)(plVar4 + 0x18));
  puVar3 = PTR_vtable_005d2218;
  plVar4[2] = 0;
  plVar4[3] = -1;
  plVar4[4] = -1;
  *(undefined4 *)(plVar4 + 5) = 0xffffffff;
  plVar4[10] = 0;
  plVar4[1] = -0x100000000;
  *plVar4 = (long)(puVar3 + 0x18);
  plVar4[7] = 0;
  plVar4[8] = 0;
  plVar4[6] = 0;
  *(undefined4 *)(plVar4 + 9) = 0;
  *(undefined4 *)(plVar4 + 0xb) = 1;
  plVar4[0xc] = 0;
  *(undefined4 *)(plVar4 + 0xd) = 0xffffffff;
  *(undefined4 *)(plVar4 + 0xe) = 0;
  plVar4[0x17] = 0;
  plVar4[0x18] = (long)(puVar3 + 0x60);
  plVar4[0x14] = 0;
  plVar4[0x15] = 0;
  *(undefined1 *)(plVar4 + 0x16) = 0;
  *param_1 = (long)plVar4;
  android::RefBase::incStrong(plVar4 + 0x18);
  *(undefined4 *)(*param_1 + 8) = 1;
  *(int *)(*param_1 + 0xc) = iVar1;
  *(undefined4 *)(*param_1 + 0x48) = param_2;
  *(int *)(*param_1 + 0x18) = (int)local_60[3];
  *(undefined4 *)(*param_1 + 0x1c) = *(undefined4 *)((long)local_60 + 0x1c);
  *(undefined4 *)(local_60 + 4) = 0xffffffff;
  *(undefined4 *)(*param_1 + 0x24) = *(undefined4 *)((long)local_60 + 0x24);
  uVar5 = RfxDataCloneManager::copyData(iVar1,param_3,1);
  *(undefined8 *)(*param_1 + 0x10) = uVar5;
  if (local_60 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_60 + *(long *)(*local_60 + -0x18)),&local_60);
  }
  if (*(long *)(lVar2 + 0x28) == local_58) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


