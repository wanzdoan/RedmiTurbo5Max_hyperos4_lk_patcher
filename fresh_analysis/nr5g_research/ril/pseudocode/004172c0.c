
/* RtcSbpController::getRefinedSbpId(int) */

int __thiscall RtcSbpController::getRefinedSbpId(RtcSbpController *this,int param_1)

{
  long lVar1;
  short sVar2;
  char cVar3;
  long lVar4;
  int *piVar5;
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
  
  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  uStack_3c = 0;
  uStack_40 = 0;
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
  rfx_property_get("ro.vendor.mtk_carrierexpress_pack",&local_90,&DAT_001f9509);
  sVar2 = (short)local_90;
  cVar3 = local_90._2_1_;
  if ((short)local_90 == 0x616c && local_90._2_1_ == '\0') {
    lVar4 = 5;
    piVar5 = &DAT_0025a2fc;
  }
  else if ((short)local_90 == 0x616e && local_90._2_1_ == '\0') {
    lVar4 = 8;
    piVar5 = &DAT_00257258;
  }
  else if ((int)local_90 == 0x745f616e && local_90._4_2_ == 0x66) {
    lVar4 = 6;
    piVar5 = &DAT_0025a310;
  }
  else if (sVar2 == 0x7565 && cVar3 == '\0') {
    lVar4 = 2;
    piVar5 = &DAT_002522f8;
  }
  else if ((int)local_90 == 0x646e69) {
    lVar4 = 1;
    piVar5 = &DAT_0025a32c;
  }
  else if ((int)local_90 == 0x6e706a) {
    lVar4 = 1;
    piVar5 = &DAT_0025a334;
  }
  else if ((int)local_90 == 0x61656d) {
    lVar4 = 1;
    piVar5 = &DAT_0025a330;
  }
  else if (sVar2 == 0x7561 && cVar3 == '\0') {
    lVar4 = 1;
    piVar5 = &DAT_0025a328;
  }
  else if ((int)local_90 == 0x737572) {
    lVar4 = 2;
    piVar5 = &DAT_00252288;
  }
  else {
    piVar5 = (int *)&UNK_0025a364;
    if ((int)local_90 == 0x706f7777 && local_90._4_1_ == '\0') {
      piVar5 = &DAT_0025a338;
    }
    lVar4 = 0xb;
    if ((int)local_90 != 0x706f7777 || local_90._4_1_ != '\0') {
      lVar4 = 1;
    }
  }
  do {
    if (*piVar5 == param_1) goto LAB_004174ac;
    lVar4 = lVar4 + -1;
    piVar5 = piVar5 + 1;
  } while (lVar4 != 0);
  param_1 = 0;
LAB_004174ac:
  if (*(long *)(lVar1 + 0x28) == local_28) {
    return param_1;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


