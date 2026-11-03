// ffff000090710f10  size=1136  FUN_ffff000090710f10

void FUN_ffff000090710f10(void)

{
  int iVar1;
  ulong uVar2;
  char *pcVar3;
  char *pcVar4;
  long lVar5;
  char local_88 [8];
  char acStack_80 [8];
  char local_78 [8];
  undefined8 uStack_70;
  undefined8 local_68;
  undefined8 uStack_60;
  undefined8 local_58;
  undefined8 uStack_50;
  undefined8 local_48;
  undefined8 uStack_40;
  undefined8 local_38;
  undefined8 uStack_30;
  undefined4 local_28;
  int local_8;
  uint local_4;
  
  local_4 = 0;
  iVar1 = FUN_ffff00009071c390();
  local_8 = 0;
  local_28 = 0;
  local_38 = 0;
  uStack_30 = 0;
  local_48 = 0;
  uStack_40 = 0;
  local_58 = 0;
  uStack_50 = 0;
  local_68 = 0;
  uStack_60 = 0;
  local_88[0] = s_androidboot_bootreason__ffff00009080420a[0];
  local_88[1] = s_androidboot_bootreason__ffff00009080420a[1];
  local_88[2] = s_androidboot_bootreason__ffff00009080420a[2];
  local_88[3] = s_androidboot_bootreason__ffff00009080420a[3];
  local_88[4] = s_androidboot_bootreason__ffff00009080420a[4];
  local_88[5] = s_androidboot_bootreason__ffff00009080420a[5];
  local_88[6] = s_androidboot_bootreason__ffff00009080420a[6];
  local_88[7] = s_androidboot_bootreason__ffff00009080420a[7];
  acStack_80[0] = s_androidboot_bootreason__ffff00009080420a[8];
  acStack_80[1] = s_androidboot_bootreason__ffff00009080420a[9];
  acStack_80[2] = s_androidboot_bootreason__ffff00009080420a[10];
  acStack_80[3] = s_androidboot_bootreason__ffff00009080420a[0xb];
  acStack_80[4] = s_androidboot_bootreason__ffff00009080420a[0xc];
  acStack_80[5] = s_androidboot_bootreason__ffff00009080420a[0xd];
  acStack_80[6] = s_androidboot_bootreason__ffff00009080420a[0xe];
  acStack_80[7] = s_androidboot_bootreason__ffff00009080420a[0xf];
  local_78[0] = s_androidboot_bootreason__ffff00009080420a[0x10];
  local_78[1] = s_androidboot_bootreason__ffff00009080420a[0x11];
  local_78[2] = s_androidboot_bootreason__ffff00009080420a[0x12];
  local_78[3] = s_androidboot_bootreason__ffff00009080420a[0x13];
  local_78[4] = s_androidboot_bootreason__ffff00009080420a[0x14];
  local_78[5] = s_androidboot_bootreason__ffff00009080420a[0x15];
  local_78[6] = s_androidboot_bootreason__ffff00009080420a[0x16];
  local_78[7] = s_androidboot_bootreason__ffff00009080420a[0x17];
  uStack_70 = 0;
  if (iVar1 == 4) {
    uVar2 = FUN_ffff0000907818cc(&local_4);
    if (((uVar2 & 1) == 0) || (uVar2 = FUN_ffff000090781908(&local_8), (uVar2 & 1) == 0)) {
LAB_ffff000090711300:
      FUN_ffff000090718f34(s_androidboot_bootreason_reboot_ffff0000907d57d1);
      FUN_ffff000090718f34(s_aee_aed_pureason_reboot_ffff0000907d3ea7);
      pcVar4 = s_androidboot_crash_reason_Normal_ffff0000907dd8f1;
      goto LAB_ffff000090711320;
    }
    FUN_ffff00009077de54
              (s__s__d__rc_wdt_status____d__rc_ex_ffff0000907d73ef,
               s_bootreason_cmdline_ffff0000907eda04,0x178,local_4,local_8);
    if (DAT_ffff000090816f28 == local_8) {
      lVar5 = 0;
    }
    else if (DAT_ffff000090816f54 == local_8) {
      lVar5 = 1;
    }
    else if (DAT_ffff000090816f80 == local_8) {
      lVar5 = 2;
    }
    else if (DAT_ffff000090816fac == local_8) {
      lVar5 = 3;
    }
    else if (DAT_ffff000090816fd8 == local_8) {
      lVar5 = 4;
    }
    else if (DAT_ffff000090817004 == local_8) {
      lVar5 = 5;
    }
    else if (DAT_ffff000090817030 == local_8) {
      lVar5 = 6;
    }
    else if (DAT_ffff00009081705c == local_8) {
      lVar5 = 7;
    }
    else if (DAT_ffff000090817088 == local_8) {
      lVar5 = 8;
    }
    else if (DAT_ffff0000908170b4 == local_8) {
      lVar5 = 9;
    }
    else {
      if (DAT_ffff0000908170e0 != local_8) {
        lVar5 = 0;
        do {
          if ((*(uint *)((long)&DAT_ffff00009081710c + lVar5) & (local_4 ^ 0xffffffff)) == 0) {
            pcVar4 = s_HW_reboot_ffff000090817110 + lVar5;
            lVar5 = FUN_ffff00009077ef88(pcVar4);
            FUN_ffff00009077efa8(local_88,pcVar4,99 - lVar5);
            FUN_ffff000090718f34();
            FUN_ffff00009077ed80(s_aee_aed_pureason__ffff000090816e00,pcVar4);
            FUN_ffff000090718f34();
            pcVar3 = s_PMIC_cold_reboot_ffff0000907faf67;
            goto LAB_ffff0000907112a8;
          }
          lVar5 = lVar5 + 0x2c;
        } while (lVar5 != 0x4fc);
        goto LAB_ffff000090711300;
      }
      lVar5 = 10;
    }
    pcVar4 = s_Watchdog_ffff000090816f2c + lVar5 * 0x2c;
    lVar5 = FUN_ffff00009077ef88(pcVar4);
    FUN_ffff00009077efa8(local_88,pcVar4,99 - lVar5);
    FUN_ffff000090718f34();
    FUN_ffff00009077ed80(s_aee_aed_pureason__ffff000090816e00,pcVar4);
    FUN_ffff000090718f34();
    pcVar3 = s_mrdump_ffff0000907f5b82;
LAB_ffff0000907112a8:
    iVar1 = FUN_ffff00009077ede4(pcVar3,pcVar4);
joined_r0xffff00009071123c:
    if (iVar1 == 0) goto LAB_ffff0000907112b4;
  }
  else {
    if (DAT_ffff000090817608 == iVar1) {
      lVar5 = 0;
    }
    else if (DAT_ffff000090817634 == iVar1) {
      lVar5 = 1;
    }
    else if (DAT_ffff000090817660 == iVar1) {
      lVar5 = 2;
    }
    else if (DAT_ffff00009081768c == iVar1) {
      lVar5 = 3;
    }
    else if (DAT_ffff0000908176b8 == iVar1) {
      lVar5 = 4;
    }
    else if (DAT_ffff0000908176e4 == iVar1) {
      lVar5 = 5;
    }
    else if (DAT_ffff000090817710 == iVar1) {
      lVar5 = 6;
    }
    else if (DAT_ffff00009081773c == iVar1) {
      lVar5 = 7;
    }
    else if (DAT_ffff000090817768 == iVar1) {
      lVar5 = 8;
    }
    else if (DAT_ffff000090817794 == iVar1) {
      lVar5 = 9;
    }
    else if (DAT_ffff0000908177c0 == iVar1) {
      lVar5 = 10;
    }
    else {
      if (DAT_ffff0000908177ec != iVar1) goto LAB_ffff000090711300;
      lVar5 = 0xb;
    }
    pcVar4 = s_2sec_reboot_ffff00009081760c + lVar5 * 0x2c;
    lVar5 = FUN_ffff00009077ef88(pcVar4);
    FUN_ffff00009077efa8(local_88,pcVar4,99 - lVar5);
    FUN_ffff000090718f34();
    FUN_ffff00009077ed80(s_aee_aed_pureason__ffff000090816e00,pcVar4);
    FUN_ffff000090718f34();
    iVar1 = FUN_ffff00009077ede4(pcVar4,&DAT_ffff0000907f3d69);
    if (((iVar1 != 0) &&
        (iVar1 = FUN_ffff00009077ede4(pcVar4,s_reboot_longkey_ffff0000907e0ae0), iVar1 != 0)) &&
       (iVar1 = FUN_ffff00009077ede4(pcVar4,&DAT_ffff0000907e0aef), iVar1 != 0)) {
      iVar1 = FUN_ffff00009077ede4(pcVar4,s_PowerKey_ffff0000907e41ac);
      goto joined_r0xffff00009071123c;
    }
LAB_ffff0000907112b4:
    FUN_ffff00009077ee0c(pcVar4,s_Normal_ffff0000907f921e);
  }
  pcVar4 = (char *)FUN_ffff00009077ed80(s_androidboot_crash_reason__ffff000090816e80,pcVar4);
LAB_ffff000090711320:
  FUN_ffff000090718f34(pcVar4);
  return;
}


