
/* RfxMessage::obtainRequest(int, int, android::sp<RfxMessage> const&, bool,
   RILD_RadioTechnology_Group) */

void __thiscall
RfxMessage::obtainRequest
          (long *param_1,RfxMessage *this,undefined8 param_3,long *param_4,uint param_5,
          undefined4 param_6)

{
  undefined8 uVar1;
  long lVar2;
  
  if (*param_4 != 0) {
    *param_1 = 0;
    obtainRequest(this,param_3,param_6);
    lVar2 = *param_1;
    *(undefined4 *)(lVar2 + 0x14) = *(undefined4 *)(*param_4 + 0x14);
    *(undefined4 *)(lVar2 + 0x18) = *(undefined4 *)(*param_4 + 0x18);
    *(undefined8 *)(lVar2 + 0x30) = *(undefined8 *)(*param_4 + 0x30);
    *(undefined8 *)(lVar2 + 0x38) = *(undefined8 *)(*param_4 + 0x38);
    *(undefined4 *)(lVar2 + 0x28) = *(undefined4 *)(*param_4 + 0x28);
    *(undefined8 *)(lVar2 + 0x90) = *(undefined8 *)(*param_4 + 0x90);
    if ((param_5 & 1) != 0) {
      uVar1 = RfxDataCloneManager::copyData((int)param_3,*(RfxBaseData **)(*param_4 + 0x48),0);
      *(undefined8 *)(lVar2 + 0x48) = uVar1;
    }
    return;
  }
  *param_1 = 0;
  return;
}


