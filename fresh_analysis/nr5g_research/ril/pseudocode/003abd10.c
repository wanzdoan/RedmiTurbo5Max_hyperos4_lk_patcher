
/* RtcRatSwitchController::getDefaultNetWorkType(int) */

void RtcRatSwitchController::getDefaultNetWorkType(int param_1)

{
  uint uVar1;
  long lVar2;
  int iVar3;
  uint uVar4;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 uStack_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 uStack_50;
  undefined4 uStack_48;
  undefined4 local_44;
  undefined4 uStack_40;
  undefined8 uStack_3c;
  long local_28;
  
  lVar2 = tpidr_el0;
  local_28 = *(long *)(lVar2 + 0x28);
  iVar3 = RatConfig_isC2kSupported(param_1);
  uVar4 = 0x38e0e;
  if (iVar3 != 0) {
    uVar4 = 0x3bffe;
  }
  iVar3 = RatConfig_isLteFddSupported();
  if ((iVar3 != 0) || (iVar3 = RatConfig_isLteTddSupported(), iVar3 != 0)) {
    uVar4 = uVar4 | 0x84000;
  }
  iVar3 = RatConfig_isNrSupported();
  uStack_3c = 0;
  uStack_40 = 0;
  uVar1 = uVar4;
  if (iVar3 != 0) {
    uVar1 = uVar4 | 0x100000;
  }
  uStack_78 = 0;
  local_80 = 0;
  uStack_68 = 0;
  uStack_70 = 0;
  uStack_58 = 0;
  local_60 = 0;
  uStack_48 = 0;
  local_44 = 0;
  uStack_50 = 0;
  uStack_88 = 0;
  local_90 = 0;
  rfx_property_get(uVar4 | 0x100000,"ro.vendor.mtk_protocol1_rat_config",&local_90,&DAT_00207797);
  if ((int)local_90 == 0x664c2f43 && local_90._4_1_ == '\0') {
    uVar1 = uVar1 & 0x1c71f1;
  }
  if (*(long *)(lVar2 + 0x28) == local_28) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(uVar1);
}


