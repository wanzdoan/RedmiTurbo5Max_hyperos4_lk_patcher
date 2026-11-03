
void FUN_ffff000090710ff4(void)

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
  iVar1 = FUN_ffff00009071c4d4();
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
  local_88[0] = s_androidboot_bootreason__ffff000090804432[0];
  local_88[1] = s_androidboot_bootreason__ffff000090804432[1];
  local_88[2] = s_androidboot_bootreason__ffff000090804432[2];
  local_88[3] = s_androidboot_bootreason__ffff000090804432[3];
  local_88[4] = s_androidboot_bootreason__ffff000090804432[4];
  local_88[5] = s_androidboot_bootreason__ffff000090804432[5];
  local_88[6] = s_androidboot_bootreason__ffff000090804432[6];
  local_88[7] = s_androidboot_bootreason__ffff000090804432[7];
  acStack_80[0] = s_androidboot_bootreason__ffff000090804432[8];
  acStack_80[1] = s_androidboot_bootreason__ffff000090804432[9];
  acStack_80[2] = s_androidboot_bootreason__ffff000090804432[10];
  acStack_80[3] = s_androidboot_bootreason__ffff000090804432[0xb];
  acStack_80[4] = s_androidboot_bootreason__ffff000090804432[0xc];
  acStack_80[5] = s_androidboot_bootreason__ffff000090804432[0xd];
  acStack_80[6] = s_androidboot_bootreason__ffff000090804432[0xe];
  acStack_80[7] = s_androidboot_bootreason__ffff000090804432[0xf];
  local_78[0] = s_androidboot_bootreason__ffff000090804432[0x10];
  local_78[1] = s_androidboot_bootreason__ffff000090804432[0x11];
  local_78[2] = s_androidboot_bootreason__ffff000090804432[0x12];
  local_78[3] = s_androidboot_bootreason__ffff000090804432[0x13];
  local_78[4] = s_androidboot_bootreason__ffff000090804432[0x14];
  local_78[5] = s_androidboot_bootreason__ffff000090804432[0x15];
  local_78[6] = s_androidboot_bootreason__ffff000090804432[0x16];
  local_78[7] = s_androidboot_bootreason__ffff000090804432[0x17];
  uStack_70 = 0;
  if (iVar1 == 4) {
    uVar2 = FUN_ffff000090781b94(&local_4);
    if (((uVar2 & 1) == 0) || (uVar2 = FUN_ffff000090781bd0(&local_8), (uVar2 & 1) == 0)) {
LAB_ffff0000907113e4:
      FUN_ffff000090719078(s_androidboot_bootreason_reboot_ffff0000907d5821);
      FUN_ffff000090719078(s_aee_aed_pureason_reboot_ffff0000907d3ee4);
      pcVar4 = s_androidboot_crash_reason_Normal_ffff0000907dd994;
      goto LAB_ffff000090711404;
    }
    FUN_ffff00009077e11c
              (s__s__d__rc_wdt_status____d__rc_ex_ffff0000907d7485,
               s_bootreason_cmdline_ffff0000907edb75,0x186,local_4,local_8);
    if (DAT_ffff000090816f38 == local_8) {
      lVar5 = 0;
    }
    else if (DAT_ffff000090816f64 == local_8) {
      lVar5 = 1;
    }
    else if (DAT_ffff000090816f90 == local_8) {
      lVar5 = 2;
    }
    else if (DAT_ffff000090816fbc == local_8) {
      lVar5 = 3;
    }
    else if (DAT_ffff000090816fe8 == local_8) {
      lVar5 = 4;
    }
    else if (DAT_ffff000090817014 == local_8) {
      lVar5 = 5;
    }
    else if (DAT_ffff000090817040 == local_8) {
      lVar5 = 6;
    }
    else if (DAT_ffff00009081706c == local_8) {
      lVar5 = 7;
    }
    else if (DAT_ffff000090817098 == local_8) {
      lVar5 = 8;
    }
    else if (DAT_ffff0000908170c4 == local_8) {
      lVar5 = 9;
    }
    else {
      if (DAT_ffff0000908170f0 != local_8) {
        lVar5 = 0;
        do {
          if ((*(uint *)((long)&DAT_ffff00009081711c + lVar5) & (local_4 ^ 0xffffffff)) == 0) {
            pcVar4 = s_HW_reboot_ffff000090817120 + lVar5;
            lVar5 = FUN_ffff00009077f250(pcVar4);
            FUN_ffff00009077f270(local_88,pcVar4,99 - lVar5);
            FUN_ffff000090719078();
            FUN_ffff00009077f048(s_aee_aed_pureason__ffff000090816e10,pcVar4);
            FUN_ffff000090719078();
            pcVar3 = s_PMIC_cold_reboot_ffff0000907fb103;
            goto LAB_ffff00009071138c;
          }
          lVar5 = lVar5 + 0x2c;
        } while (lVar5 != 0x4fc);
        goto LAB_ffff0000907113e4;
      }
      lVar5 = 10;
    }
    pcVar4 = s_Watchdog_ffff000090816f3c + lVar5 * 0x2c;
    lVar5 = FUN_ffff00009077f250(pcVar4);
    FUN_ffff00009077f270(local_88,pcVar4,99 - lVar5);
    FUN_ffff000090719078();
    FUN_ffff00009077f048(s_aee_aed_pureason__ffff000090816e10,pcVar4);
    FUN_ffff000090719078();
    pcVar3 = s_mrdump_ffff0000907f5cf9;
LAB_ffff00009071138c:
    iVar1 = FUN_ffff00009077f0ac(pcVar3,pcVar4);
joined_r0xffff000090711320:
    if (iVar1 == 0) goto LAB_ffff000090711398;
  }
  else {
    if (DAT_ffff000090817618 == iVar1) {
      lVar5 = 0;
    }
    else if (DAT_ffff000090817644 == iVar1) {
      lVar5 = 1;
    }
    else if (DAT_ffff000090817670 == iVar1) {
      lVar5 = 2;
    }
    else if (DAT_ffff00009081769c == iVar1) {
      lVar5 = 3;
    }
    else if (DAT_ffff0000908176c8 == iVar1) {
      lVar5 = 4;
    }
    else if (DAT_ffff0000908176f4 == iVar1) {
      lVar5 = 5;
    }
    else if (DAT_ffff000090817720 == iVar1) {
      lVar5 = 6;
    }
    else if (DAT_ffff00009081774c == iVar1) {
      lVar5 = 7;
    }
    else if (DAT_ffff000090817778 == iVar1) {
      lVar5 = 8;
    }
    else if (DAT_ffff0000908177a4 == iVar1) {
      lVar5 = 9;
    }
    else if (DAT_ffff0000908177d0 == iVar1) {
      lVar5 = 10;
    }
    else {
      if (DAT_ffff0000908177fc != iVar1) goto LAB_ffff0000907113e4;
      lVar5 = 0xb;
    }
    pcVar4 = s_2sec_reboot_ffff00009081761c + lVar5 * 0x2c;
    lVar5 = FUN_ffff00009077f250(pcVar4);
    FUN_ffff00009077f270(local_88,pcVar4,99 - lVar5);
    FUN_ffff000090719078();
    FUN_ffff00009077f048(s_aee_aed_pureason__ffff000090816e10,pcVar4);
    FUN_ffff000090719078();
    iVar1 = FUN_ffff00009077f0ac(pcVar4,&DAT_ffff0000907f3ef5);
    if (((iVar1 != 0) &&
        (iVar1 = FUN_ffff00009077f0ac(pcVar4,s_reboot_longkey_ffff0000907e0b83), iVar1 != 0)) &&
       (iVar1 = FUN_ffff00009077f0ac(pcVar4,&DAT_ffff0000907e0b92), iVar1 != 0)) {
      iVar1 = FUN_ffff00009077f0ac(pcVar4,s_PowerKey_ffff0000907e427d);
      goto joined_r0xffff000090711320;
    }
LAB_ffff000090711398:
    FUN_ffff00009077f0d4(pcVar4,s_Normal_ffff0000907f93ba);
  }
  pcVar4 = (char *)FUN_ffff00009077f048(s_androidboot_crash_reason__ffff000090816e90,pcVar4);
LAB_ffff000090711404:
  FUN_ffff000090719078(pcVar4);
  return;
}


