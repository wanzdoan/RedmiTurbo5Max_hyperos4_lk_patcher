// ffff000090713840  size=240  FUN_ffff000090713840

void FUN_ffff000090713840(void)

{
  uint uVar1;
  uint uVar2;
  int iVar3;
  undefined4 uVar4;
  char *pcVar5;
  long lVar6;
  
  uVar2 = FUN_ffff000090710b48();
  uVar1 = uVar2 & 0xff;
  if (uVar1 == 4) {
LAB_ffff000090713874:
    FUN_ffff000090712d74(uVar2);
    FUN_ffff000090718f34(s_androidboot_init_rc__system_ext__ffff0000907e8cf9);
    if (uVar1 == 2) goto LAB_ffff0000907138b4;
    pcVar5 = s_androidboot_force_normal_boot_1_ffff0000907fb0a4;
  }
  else {
    if (uVar1 == 2) goto LAB_ffff0000907138b4;
    if (uVar1 == 1) goto LAB_ffff000090713874;
    FUN_ffff000090718f34(s_androidboot_force_normal_boot_1_ffff0000907fb0a4);
    if ((uVar2 & 0xff) != 0) goto LAB_ffff0000907138b4;
    iVar3 = FUN_ffff000090784760();
    if (iVar3 == 0) {
      uVar4 = FUN_ffff0000907692e0();
      iVar3 = FUN_ffff000090788154(0,uVar4);
      if (iVar3 != 0) {
        FUN_ffff00009077de54
                  (s__s__d__write_protect_result____d_ffff0000907f25c8,
                   s_boot_mode_cmdline_ffff0000907dd9e2,0xcb,iVar3);
      }
      pcVar5 = s_androidboot_atm_disabled_ffff0000907ef2ed;
    }
    else {
      pcVar5 = s_androidboot_atm_enable_ffff0000907fb0c4;
    }
  }
  FUN_ffff000090718f34(pcVar5);
LAB_ffff0000907138b4:
  lVar6 = FUN_ffff000090781bdc(uVar2);
  if (lVar6 != 0) {
    FUN_ffff000090718f34();
    return;
  }
  return;
}


