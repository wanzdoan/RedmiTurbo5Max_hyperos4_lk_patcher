
/* RfxMessage::obtainRequest(int, int, RfxBaseData const&, RILD_RadioTechnology_Group) */

void __thiscall
RfxMessage::obtainRequest
          (long *param_1,RfxMessage *this,undefined8 param_3,RfxBaseData *param_4,undefined4 param_5
          )

{
  undefined8 uVar1;
  
  *param_1 = 0;
  obtainRequest(this,param_3,param_5);
  uVar1 = RfxDataCloneManager::copyData((int)param_3,param_4,0);
  *(undefined8 *)(*param_1 + 0x48) = uVar1;
  return;
}


