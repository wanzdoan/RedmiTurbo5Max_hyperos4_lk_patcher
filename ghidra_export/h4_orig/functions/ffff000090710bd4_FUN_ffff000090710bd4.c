// ffff000090710bd4  size=900  FUN_ffff000090710bd4

void FUN_ffff000090710bd4(void)

{
  int iVar1;
  char cVar2;
  uint uVar3;
  int iVar4;
  ulong uVar5;
  undefined8 uVar6;
  long lVar7;
  char *pcVar8;
  ulong local_48;
  ulong local_40;
  undefined8 local_38;
  undefined8 local_30;
  undefined4 local_28;
  
  uVar3 = FUN_ffff00009071c4e8();
  if (((uVar3 & 0xff) < 8) && ((1 << (ulong)(uVar3 & 0x1f) & 0x92U) != 0)) {
    DAT_ffff00009083f660 = (char)uVar3;
    return;
  }
  uVar5 = FUN_ffff00009072998c(1);
  if ((uVar5 & 1) != 0) {
    FUN_ffff000090703bd4(2);
    FUN_ffff000090764714(0);
    DAT_ffff00009083f660 = 2;
    return;
  }
  uVar5 = FUN_ffff00009072998c(6);
  if ((uVar5 & 1) != 0) {
    uVar6 = 0;
LAB_ffff000090710c64:
    FUN_ffff000090703bd4(uVar6);
    DAT_ffff00009083f660 = 99;
    return;
  }
  cVar2 = FUN_ffff000090746004();
  if ((cVar2 == '\x02') || (uVar5 = FUN_ffff00009071fe9c(), (uVar5 & 1) != 0)) {
    DAT_ffff00009083f660 = 2;
    return;
  }
  cVar2 = FUN_ffff000090746004();
  if (cVar2 == '\x03') {
    uVar6 = 1;
    goto LAB_ffff000090710c64;
  }
  local_28 = 0;
  local_48 = 0x200000002;
  local_40 = 0;
  local_38 = 0x220240725;
  local_30 = 0x100000000;
  lVar7 = FUN_ffff00009077c028(s_charger_ffff0000907feacc);
  if (lVar7 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___XMCHG_LK__bio_open_faild_ffff0000907ebf84,
               s_GetPowerOffMode_ffff0000907edb88,0xd7);
  }
  uVar6 = FUN_ffff00009077c0bc(lVar7,&local_38,0,0x14);
  FUN_ffff00009077e11c
            (s__s__d___XMCHG_LK__val__d_magic_0_ffff0000907f7bec,s_GetPowerOffMode_ffff0000907edb88,
             0xdd,uVar6,local_38 & 0xffffffff,local_38._4_4_,local_30 & 0xffffffff,local_30._4_4_,
             local_28);
  if ((int)local_38 != 0x20240725) {
    FUN_ffff00009077e11c
              (s__s__d___XMCHG_LK__magic_error__p_ffff0000907d25f6,
               s_GetPowerOffMode_ffff0000907edb88,0xdf);
    return;
  }
  local_38 = 0x220240725;
  FUN_ffff00009077c144(lVar7,&local_38,0,0x14);
  uVar6 = FUN_ffff00009077c0bc(lVar7,&local_38,0,0x14);
  FUN_ffff00009077e11c
            (s__s__d___XMCHG_LK__val__d_magic_0_ffff0000907f7bec,s_GetPowerOffMode_ffff0000907edb88,
             0xe6,uVar6,local_38 & 0xffffffff,local_38._4_4_,local_30 & 0xffffffff,local_30._4_4_,
             local_28);
  uVar6 = FUN_ffff00009077c0bc(lVar7,&local_48,0x1000,0x10);
  FUN_ffff00009077e11c
            (s__s__d___XMCHG_LK__val__d_power_o_ffff0000907dbfeb,s_GetPowerOffMode_ffff0000907edb88,
             0xeb,uVar6,local_48 & 0xffffffff);
  iVar1 = (int)local_48;
  if ((int)local_48 == 1) {
    pcVar8 = s__s__d___XMCHG_LK__poweroffmode_y_ffff0000907e7538;
    uVar6 = 0xed;
  }
  else {
    pcVar8 = s__s__d___XMCHG_LK__poweroffmode_n_ffff0000907da5ac;
    uVar6 = 0xf1;
  }
  FUN_ffff00009077e11c(pcVar8,s_GetPowerOffMode_ffff0000907edb88,uVar6);
  local_48 = CONCAT44(local_48._4_4_,2);
  FUN_ffff00009077c144(lVar7,&local_48,0x1000,0x10);
  uVar6 = FUN_ffff00009077c0bc(lVar7,&local_48,0x1000,0x10);
  FUN_ffff00009077e11c
            (s__s__d___XMCHG_LK__val__d_powerof_ffff00009080047c,s_GetPowerOffMode_ffff0000907edb88,
             0xf6,uVar6,local_48 & 0xffffffff,local_40 & 0xffffffff);
  uVar5 = FUN_ffff000090710f58();
  if ((uVar5 & 1) == 0) {
    local_40 = local_40 & 0xffffffff00000000;
    iVar4 = FUN_ffff00009077c144(lVar7,&local_48,0x1000,0x10);
    if (iVar4 == 0) {
      pcVar8 = s__s__d___XMCHG_LK__ChgPartition__w_ffff0000907da5cf;
      uVar6 = 0xfd;
    }
    else {
      pcVar8 = s__s__d___XMCHG_LK__ChgPartition__c_ffff0000907e4286;
      uVar6 = 0xff;
    }
    FUN_ffff00009077e11c(pcVar8,s_GetPowerOffMode_ffff0000907edb88,uVar6);
  }
  else if ((int)local_40 == 1) {
    FUN_ffff00009077e11c
              (s__s__d___XMCHG_LK__ChgPartition__f_ffff0000907f7c45,
               s_GetPowerOffMode_ffff0000907edb88,0x102);
    goto LAB_ffff000090710f24;
  }
  FUN_ffff00009077bfac(lVar7);
  if (iVar1 != 1) {
    return;
  }
LAB_ffff000090710f24:
  uVar5 = FUN_ffff000090710f58();
  if ((uVar5 & 1) != 0) {
    DAT_ffff00009083f660 = 8;
    FUN_ffff00009077e11c
              (s__s__d___s__enter_for_kpoc_check__ffff00009080043f,
               s_boot_mode_check_ffff0000907e5bad,0x120,s_boot_mode_check_ffff0000907e5bad);
  }
  return;
}


