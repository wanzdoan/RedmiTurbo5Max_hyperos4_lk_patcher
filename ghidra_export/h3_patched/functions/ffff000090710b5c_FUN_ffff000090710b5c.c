// ffff000090710b5c  size=792  FUN_ffff000090710b5c

void FUN_ffff000090710b5c(void)

{
  int iVar1;
  char cVar2;
  uint uVar3;
  ulong uVar4;
  undefined8 uVar5;
  long lVar6;
  char *pcVar7;
  ulong local_48;
  ulong local_40;
  undefined8 local_38;
  undefined8 local_30;
  undefined4 local_28;
  
  uVar3 = FUN_ffff00009071c3a4();
  if (((uVar3 & 0xff) < 8) && ((1 << (ulong)(uVar3 & 0x1f) & 0x92U) != 0)) {
    DAT_ffff00009083f628 = (undefined1)uVar3;
  }
  else {
    uVar4 = FUN_ffff000090729748(1);
    if ((uVar4 & 1) == 0) {
      uVar4 = FUN_ffff000090729748(6);
      if ((uVar4 & 1) == 0) {
        cVar2 = FUN_ffff000090745d6c();
        if ((cVar2 == '\x02') || (uVar4 = FUN_ffff00009071fd14(), (uVar4 & 1) != 0)) {
          DAT_ffff00009083f628 = 2;
          return;
        }
        cVar2 = FUN_ffff000090745d6c();
        if (cVar2 != '\x03') {
          local_28 = 0;
          local_38 = 0x220240725;
          local_30 = 0x100000000;
          local_48 = 0x200000002;
          local_40 = 0x12345678;
          lVar6 = FUN_ffff00009077bd60(s_charger_ffff0000907fe924);
          if (lVar6 == 0) {
            FUN_ffff00009077de54
                      (s__s__d___XMCHG_LK__bio_open_faild_ffff0000907ebdea,
                       s_GetPowerOffMode_ffff0000907eda00,0xd7);
          }
          uVar5 = FUN_ffff00009077bdf4(lVar6,&local_38,0,0x14);
          FUN_ffff00009077de54
                    (s__s__d___XMCHG_LK__val__d_magic_0_ffff0000907f7a62,
                     s_GetPowerOffMode_ffff0000907eda00,0xdd,uVar5,local_38 & 0xffffffff,
                     local_38._4_4_,local_30 & 0xffffffff,local_30._4_4_,local_28);
          if ((int)local_38 != 0x20240725) {
            FUN_ffff00009077de54
                      (s__s__d___XMCHG_LK__magic_error__p_ffff0000907d25b9,
                       s_GetPowerOffMode_ffff0000907eda00,0xdf);
            return;
          }
          local_38 = 0x220240725;
          FUN_ffff00009077be7c(lVar6,&local_38,0,0x14);
          uVar5 = FUN_ffff00009077bdf4(lVar6,&local_38,0,0x14);
          FUN_ffff00009077de54
                    (s__s__d___XMCHG_LK__val__d_magic_0_ffff0000907f7a62,
                     s_GetPowerOffMode_ffff0000907eda00,0xe6,uVar5,local_38 & 0xffffffff,
                     local_38._4_4_,local_30 & 0xffffffff,local_30._4_4_,local_28);
          uVar5 = FUN_ffff00009077bdf4(lVar6,&local_48,0x1000,0x10);
          FUN_ffff00009077de54
                    (s__s__d___XMCHG_LK__val__d_power_o_ffff0000907dbec9,
                     s_GetPowerOffMode_ffff0000907eda00,0xeb,uVar5,local_48 & 0xffffffff);
          iVar1 = (int)local_48;
          if ((int)local_48 == 1) {
            pcVar7 = s__s__d___XMCHG_LK__poweroffmode_y_ffff0000907e73dd;
            uVar5 = 0xed;
          }
          else {
            pcVar7 = s__s__d___XMCHG_LK__poweroffmode_n_ffff0000907da4fe;
            uVar5 = 0xf1;
          }
          FUN_ffff00009077de54(pcVar7,s_GetPowerOffMode_ffff0000907eda00,uVar5);
          local_48 = CONCAT44(local_48._4_4_,2);
          local_40 = CONCAT44(local_40._4_4_,0x12345678);
          FUN_ffff00009077be7c(lVar6,&local_48,0x1000,0x10);
          uVar5 = FUN_ffff00009077bdf4(lVar6,&local_48,0x1000,0x10);
          FUN_ffff00009077de54
                    (s__s__d___XMCHG_LK__val__d_powerof_ffff0000907dbef5,
                     s_GetPowerOffMode_ffff0000907eda00,0xf7,uVar5,local_48 & 0xffffffff,
                     local_40 & 0xffffffff);
          FUN_ffff00009077bce4(lVar6);
          if (iVar1 != 1) {
            return;
          }
          uVar4 = FUN_ffff000090710e74();
          if ((uVar4 & 1) == 0) {
            return;
          }
          DAT_ffff00009083f628 = 8;
          FUN_ffff00009077de54
                    (s__s__d___s__enter_for_kpoc_check__ffff000090800297,
                     s_boot_mode_check_ffff0000907e5a82,0x112,s_boot_mode_check_ffff0000907e5a82);
          return;
        }
        uVar5 = 1;
      }
      else {
        uVar5 = 0;
      }
      FUN_ffff000090703c78(uVar5);
      DAT_ffff00009083f628 = 99;
    }
    else {
      FUN_ffff000090703c78(2);
      FUN_ffff00009076447c(0);
      DAT_ffff00009083f628 = 2;
    }
  }
  return;
}


