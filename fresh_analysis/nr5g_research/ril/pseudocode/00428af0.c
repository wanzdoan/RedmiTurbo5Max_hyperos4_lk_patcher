
/* RmcRadioUrcHandler::handleDSBPEnhancement(RfxAtLine*) */

void __thiscall
RmcRadioUrcHandler::handleDSBPEnhancement(RmcRadioUrcHandler *this,RfxAtLine *param_1)

{
  int iVar1;
  long lVar2;
  uint uVar3;
  ulong uVar4;
  RfxMclStatusManager *this_00;
  RfxBaseData *pRVar5;
  long *local_78;
  uint local_70;
  int local_6c;
  undefined8 local_68;
  uint local_60;
  long local_38;
  
  lVar2 = tpidr_el0;
  local_38 = *(long *)(lVar2 + 0x28);
  local_6c = 0;
  RfxAtLine::atTokStart(param_1,&local_6c);
  uVar4 = RfxAtLine::atTokNextint(param_1,&local_6c);
  if (-1 < local_6c) {
    uVar3 = RfxAtLine::atTokNextint(param_1,&local_6c);
    local_70 = uVar3;
    if (-1 < local_6c) {
      RfxBaseHandler::logI
                ((char *)this,"RmcRadUrc","handleDSBPEnhancement: SBP: %d, state: %d",
                 uVar4 & 0xffffffff,(ulong)uVar3);
      this_00 = (RfxMclStatusManager *)RfxBaseHandler::getMclStatusManager((RfxBaseHandler *)this);
      local_60 = (uint)(uVar3 == 1);
      local_68 = CONCAT44(local_68._4_4_,2);
      pRVar5 = (RfxBaseData *)0x0;
      RfxMclStatusManager::setValue(this_00,0x69,(RfxVariant *)&local_68,false,false);
      RfxVariant::deinit((RfxVariant *)&local_68);
      iVar1 = *(int *)(this + 8);
      local_78 = (long *)0x0;
      RfxIntsData::RfxIntsData((RfxIntsData *)&local_68,(int *)&local_70,1);
      RfxMclMessage::obtainUrc((RfxMclMessage *)0xdada,iVar1,(int)&local_68,pRVar5);
      RfxIntsData::~RfxIntsData((RfxIntsData *)&local_68);
      RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_78);
      if (local_78 != (long *)0x0) {
        android::RefBase::decStrong
                  ((RefBase *)((long)local_78 + *(long *)(*local_78 + -0x18)),&local_78);
      }
      goto LAB_00428c80;
    }
  }
  uVar4 = __rfx_is_gt_mode();
  if ((uVar4 & 1) == 0) {
    mtkLogE("RmcRadUrc","parser +EDSBP error!");
  }
  else {
    local_68 = 0;
    android::String8::format("%s%s",&local_68,&DAT_001fbebf,"RmcRadUrc");
    mtkLogE(local_68,"parser +EDSBP error!");
    android::String8::~String8((String8 *)&local_68);
  }
LAB_00428c80:
  if (*(long *)(lVar2 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


