// ffff000090713924  size=240  FUN_ffff000090713924

void FUN_ffff000090713924(void)

{
  uint uVar1;
  uint uVar2;
  int iVar3;
  undefined4 uVar4;
  char *pcVar5;
  long lVar6;
  
  uVar2 = FUN_ffff000090710bc0();
  uVar1 = uVar2 & 0xff;
  if (uVar1 == 4) {
LAB_ffff000090713958:
    FUN_ffff000090712e58(uVar2);
    FUN_ffff000090719078(s_androidboot_init_rc__system_ext__ffff0000907e8e54);
    if (uVar1 == 2) goto LAB_ffff000090713998;
    pcVar5 = s_androidboot_force_normal_boot_1_ffff0000907fb240;
  }
  else {
    if (uVar1 == 2) goto LAB_ffff000090713998;
    if (uVar1 == 1) goto LAB_ffff000090713958;
    FUN_ffff000090719078(s_androidboot_force_normal_boot_1_ffff0000907fb240);
    if ((uVar2 & 0xff) != 0) goto LAB_ffff000090713998;
    iVar3 = FUN_ffff000090784a28();
    if (iVar3 == 0) {
      uVar4 = FUN_ffff0000907695a8();
      iVar3 = FUN_ffff000090788414(0,uVar4);
      if (iVar3 != 0) {
        FUN_ffff00009077e11c
                  (s__s__d__write_protect_result____d_ffff0000907f276b,
                   s_boot_mode_cmdline_ffff0000907dda9c,0xcc,iVar3);
      }
      pcVar5 = s_androidboot_atm_disabled_ffff0000907ef475;
    }
    else {
      pcVar5 = s_androidboot_atm_enable_ffff0000907fb260;
    }
  }
  FUN_ffff000090719078(pcVar5);
LAB_ffff000090713998:
  lVar6 = FUN_ffff000090781ea4(uVar2);
  if (lVar6 != 0) {
    FUN_ffff000090719078();
    return;
  }
  return;
}


