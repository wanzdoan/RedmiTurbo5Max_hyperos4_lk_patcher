// ffff00009073aeec  size=596  FUN_ffff00009073aeec

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009073aeec(undefined8 param_1)

{
  undefined4 uVar1;
  int iVar2;
  undefined4 *puVar3;
  char *pcVar4;
  long local_50 [4];
  
  FUN_ffff00009077de54(s__s__In_dfd_init_ffff0000907dabd1,s_dfd_mcu_init_ffff0000907fd3a2);
  puVar3 = (undefined4 *)FUN_ffff00009073dc20(0xdb45);
  if (puVar3 == (undefined4 *)0x0) {
    pcVar4 = s__s___Error__plat____NULL_ffff0000907ff049;
LAB_ffff00009073b048:
    FUN_ffff00009077de54(pcVar4,s_dfd_mcu_init_ffff0000907fd3a2);
    return;
  }
  local_50[2] = 0;
  local_50[3] = 0;
  local_50[0] = 0;
  local_50[1] = 0;
  *(undefined8 *)(puVar3 + 2) = 0;
  *puVar3 = 0x5566520;
  iVar2 = FUN_ffff00009073b140(param_1);
  if (iVar2 < 1) {
    return;
  }
  if (_DAT_ffff00009086dff8 == 0) {
    _DAT_ffff00009086e000 =
         FUN_ffff000090719798
                   (_DAT_ffff00009086e008,_DAT_ffff00009086e010,_DAT_ffff00009086e018,0,0,
                    s_platform_debug_dfdmcu_dump_ffff0000907dabe2);
    FUN_ffff00009077de54
              (s__s__buf_addr___0x_llx_ffff0000907d7ac8,s_dfd_mcu_init_ffff0000907fd3a2,
               _DAT_ffff00009086e000);
    if (_DAT_ffff00009086e000 == 0) {
      pcVar4 = s__s__Reserve_DFD_buffer_failed_ffff0000907d1254;
      goto LAB_ffff00009073b048;
    }
  }
  iVar2 = FUN_ffff000090739a84(1);
  if (iVar2 < 0) {
    pcVar4 = s__s__disable_therm1_reboot_fail____ffff0000907ee20d;
  }
  else {
    iVar2 = FUN_ffff000090739b00(0);
    if (iVar2 < 0) {
      pcVar4 = s__s__disable_therm2_reboot_fail____ffff0000907e937c;
    }
    else {
      iVar2 = FUN_ffff000090739b7c(_DAT_ffff00009086dff0,0);
      if (-1 < iVar2) {
        FUN_ffff00009071ea6c
                  (0xc2000416,0x99716150,_DAT_ffff00009086e000,_DAT_ffff00009086e058,
                   _DAT_ffff00009086e060,0,0,0,local_50);
        if (local_50[0] != 0) {
          FUN_ffff00009077de54
                    (s__s__call___smc_conuit_fail__0x_x_ffff0000907e79e9,
                     s_dfd_mcu_init_ffff0000907fd3a2,0xc2000416,0x99716150);
          return;
        }
        iVar2 = FUN_ffff000090739a08(1);
        if (-1 < iVar2) {
          if (_DAT_ffff00009086dff8 == 0) {
            uVar1 = _DAT_ffff00009086e000;
            puVar3[3] = _DAT_ffff00009086e000;
          }
          else {
            uVar1 = puVar3[3];
          }
          puVar3[2] = 1;
          FUN_ffff00009077de54
                    (s__s__plat_sram_flag2___0x_x__plat_ffff0000907d1273,
                     s_dfd_mcu_init_ffff0000907fd3a2,uVar1,1);
          FUN_ffff00009074b22c();
          return;
        }
        FUN_ffff00009077de54
                  (s__s__dfd_mcu_start_count_fail___d_ffff0000907d4635,
                   s_dfd_mcu_init_ffff0000907fd3a2,iVar2);
        return;
      }
      pcVar4 = s__s__Setup_dfd_mcu_timeout_fail____ffff0000907fd3af;
    }
  }
  FUN_ffff00009077de54(pcVar4,s_dfd_mcu_init_ffff0000907fd3a2,iVar2);
  return;
}


