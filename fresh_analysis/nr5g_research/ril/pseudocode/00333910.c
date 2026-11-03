
/* RfxMclMessage::obtainRequest(int, RfxBaseData*, int, int, bool, void*, long, bool) */

void __thiscall
RfxMclMessage::obtainRequest
          (RfxMclMessage *this,int param_1,RfxBaseData *param_2,int param_3,int param_4,bool param_5
          ,void *param_6,long param_7,bool param_8)

{
  undefined *puVar1;
  long *plVar2;
  undefined8 uVar3;
  long *in_x8;
  
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
  *in_x8 = (long)plVar2;
  android::RefBase::incStrong(plVar2 + 0x18);
  *(undefined4 *)(*in_x8 + 8) = 0;
  *(int *)(*in_x8 + 0xc) = (int)this;
  uVar3 = RfxDataCloneManager::copyData((int)this,(RfxBaseData *)(ulong)(uint)param_1,0);
  *(undefined8 *)(*in_x8 + 0x10) = uVar3;
  *(int *)(*in_x8 + 0x1c) = (int)param_2;
  *(undefined4 *)(*in_x8 + 0x20) = 0xffffffff;
  *(int *)(*in_x8 + 0x24) = param_3;
  *(char *)(*in_x8 + 0xa3) = (char)param_4;
  *(ulong *)(*in_x8 + 0x60) = (ulong)param_5;
  *(void **)(*in_x8 + 0xa8) = param_6;
  *(char *)(*in_x8 + 0xb0) = (char)param_7;
  return;
}


