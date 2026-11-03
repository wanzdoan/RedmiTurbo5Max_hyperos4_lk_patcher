
/* RmcNetworkUrcHandler::handle5GUCChanged(android::sp<RfxMclMessage> const&) */

void RmcNetworkUrcHandler::handle5GUCChanged(sp *param_1)

{
  int iVar1;
  long lVar2;
  long *plVar3;
  undefined4 uVar4;
  long *in_x1;
  RfxBaseData *in_x3;
  RfxAtLine *this;
  long *local_78;
  long *local_70;
  int local_64;
  RfxIntsData aRStack_60 [28];
  int local_44;
  undefined8 local_40;
  long local_38;
  
  lVar2 = tpidr_el0;
  local_38 = *(long *)(lVar2 + 0x28);
  local_64 = 0;
  local_40 = 0;
  this = *(RfxAtLine **)(*in_x1 + 0x30);
  local_70 = (long *)0x0;
  RfxBaseHandler::logD((char *)param_1,"RmcNwHdlr","handle5GUCChanged +++");
  RfxAtLine::atTokStart(this,&local_64);
  local_44 = RfxAtLine::atTokNextint(this,&local_64);
  if (-1 < local_64) {
    uVar4 = RfxAtLine::atTokNextint(this,&local_64);
    local_40 = CONCAT44(local_40._4_4_,uVar4);
    if (-1 < local_64) {
      uVar4 = RfxAtLine::atTokNextint(this,&local_64);
      local_40 = CONCAT44(uVar4,(undefined4)local_40);
      if (-1 < local_64) {
        iVar1 = *(int *)(param_1 + 8);
        RfxIntsData::RfxIntsData(aRStack_60,&local_44,3);
        RfxMclMessage::obtainUrc((RfxMclMessage *)0xc7c9,iVar1,(int)aRStack_60,in_x3);
        plVar3 = local_70;
        if (local_70 != (long *)0x0) {
          android::RefBase::decStrong
                    ((RefBase *)((long)local_70 + *(long *)(*local_70 + -0x18)),&local_70);
        }
        if (plVar3 != local_70) {
          android::sp_report_race();
        }
        local_70 = local_78;
        RfxIntsData::~RfxIntsData(aRStack_60);
        RfxBaseHandler::responseToTelCore((RfxBaseHandler *)param_1,(sp *)&local_70);
        goto joined_r0x0049d2f0;
      }
    }
  }
  RfxBaseHandler::logE
            ((char *)param_1,"RmcNwHdlr","There is something wrong in %s","handle5GUCChanged");
joined_r0x0049d2f0:
  if (local_70 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_70 + *(long *)(*local_70 + -0x18)),&local_70);
  }
  if (*(long *)(lVar2 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


