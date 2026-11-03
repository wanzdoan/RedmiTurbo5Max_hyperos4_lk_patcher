
/* RmcNrSwitchHandler::UpdateAPNrSlot() */

void __thiscall RmcNrSwitchHandler::UpdateAPNrSlot(RmcNrSwitchHandler *this)

{
  long lVar1;
  uint uVar2;
  uint uVar3;
  int iVar4;
  RfxMclStatusManager *pRVar5;
  undefined4 local_78;
  undefined4 uStack_74;
  uint local_70;
  long local_48;
  
  lVar1 = tpidr_el0;
  local_48 = *(long *)(lVar1 + 0x28);
  uVar2 = getNrPsSlot(this);
  pRVar5 = (RfxMclStatusManager *)RfxBaseHandler::getNonSlotMclStatusManager();
  uVar3 = RfxMclStatusManager::getIntValue(pRVar5,0x47);
  if (uVar3 == 0xffffffff) {
    pRVar5 = (RfxMclStatusManager *)RfxBaseHandler::getNonSlotMclStatusManager();
    iVar4 = RfxMclStatusManager::getIntValue(pRVar5,0x34,0);
    uVar3 = (uint)(uVar2 == 0);
    if (iVar4 != 1) {
      uVar3 = uVar2;
    }
  }
  android::String8::format("%d",&local_78,(ulong)(uVar3 + 1));
  rfx_property_set("persist.vendor.radio.nrslot",CONCAT44(uStack_74,local_78));
  android::String8::~String8((String8 *)&local_78);
  pRVar5 = (RfxMclStatusManager *)RfxBaseHandler::getNonSlotMclStatusManager();
  local_78 = 2;
  local_70 = uVar3 + 1;
  RfxMclStatusManager::setValue(pRVar5,0x55,(RfxVariant *)&local_78,false,false);
  RfxVariant::deinit((RfxVariant *)&local_78);
  *(uint *)PTR_mNrPsSlot_005d3fd0 = uVar2;
  RfxBaseHandler::logV
            ((char *)this,"RmcNrSwHdlr","requestNrSwitch ok, mNrSlot=%d dataSlot=%d",(ulong)uVar2,
             (ulong)uVar3);
  if (*(long *)(lVar1 + 0x28) != local_48) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}


