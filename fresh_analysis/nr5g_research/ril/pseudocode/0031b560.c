
/* RfxMessage::obtainRequest(int, RfxBaseData const&, android::sp<RfxMessage> const&, bool) */

void __thiscall
RfxMessage::obtainRequest
          (RfxMessage *this,int param_1,RfxBaseData *param_2,sp *param_3,bool param_4)

{
  RfxMessage *pRVar4;
  undefined8 uVar1;
  RfxBaseData *pRVar2;
  long *in_x8;
  long lVar3;
  
  pRVar2 = (RfxBaseData *)(ulong)(uint)param_1;
                    /* WARNING: Load size is inaccurate */
  pRVar4._0_4_ = *(RfxMessage **)(*(long *)param_2 + 0x24);
  *in_x8 = 0;
  obtainRequest(pRVar4._0_4_,(ulong)this & 0xffffffff,0,(int)param_3,param_4);
  lVar3 = *in_x8;
  *(undefined4 *)(lVar3 + 0x14) = *(undefined4 *)(*(long *)param_2 + 0x14);
  *(undefined4 *)(lVar3 + 0x18) = *(undefined4 *)(*(long *)param_2 + 0x18);
  *(undefined8 *)(lVar3 + 0x30) = *(undefined8 *)(*(long *)param_2 + 0x30);
  *(undefined8 *)(lVar3 + 0x38) = *(undefined8 *)(*(long *)param_2 + 0x38);
  *(undefined4 *)(lVar3 + 0x28) = *(undefined4 *)(*(long *)param_2 + 0x28);
  *(undefined8 *)(lVar3 + 0x90) = *(undefined8 *)(*(long *)param_2 + 0x90);
  if (((ulong)param_3 & 1) != 0) {
    pRVar2 = *(RfxBaseData **)(*(long *)param_2 + 0x48);
  }
  uVar1 = RfxDataCloneManager::copyData((int)this,pRVar2,0);
  *(undefined8 *)(lVar3 + 0x48) = uVar1;
  return;
}


