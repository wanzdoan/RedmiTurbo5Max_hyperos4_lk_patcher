
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009073b184(undefined8 param_1)

{
  undefined4 uVar1;
  int iVar2;
  undefined4 *puVar3;
  char *pcVar4;
  long local_50 [4];
  
  FUN_ffff00009077e11c(s__s__In_dfd_init_ffff0000907dacf3,s_dfd_mcu_init_ffff0000907fd54a);
  puVar3 = (undefined4 *)FUN_ffff00009073deb8(0xdb45);
  if (puVar3 == (undefined4 *)0x0) {
    pcVar4 = s__s___Error__plat____NULL_ffff0000907ff1f1;
LAB_ffff00009073b2e0:
    FUN_ffff00009077e11c(pcVar4,s_dfd_mcu_init_ffff0000907fd54a);
    return;
  }
  local_50[2] = 0;
  local_50[3] = 0;
  local_50[0] = 0;
  local_50[1] = 0;
  *(undefined8 *)(puVar3 + 2) = 0;
  *puVar3 = 0x5566520;
  iVar2 = FUN_ffff00009073b3d8(param_1);
  if (iVar2 < 1) {
    return;
  }
  if (DAT_ffff00009086e090 == 0) {
    _DAT_ffff00009086e098 =
         FUN_ffff0000907198dc
                   (DAT_ffff00009086e0a0,_DAT_ffff00009086e0a8,DAT_ffff00009086e0b0,0,0,
                    s_platform_debug_dfdmcu_dump_ffff0000907dad04);
    FUN_ffff00009077e11c
              (s__s__buf_addr___0x_llx_ffff0000907d7b5e,s_dfd_mcu_init_ffff0000907fd54a,
               _DAT_ffff00009086e098);
    if (_DAT_ffff00009086e098 == 0) {
      pcVar4 = s__s__Reserve_DFD_buffer_failed_ffff0000907d1291;
      goto LAB_ffff00009073b2e0;
    }
  }
  iVar2 = FUN_ffff000090739d1c(1);
  if (iVar2 < 0) {
    pcVar4 = s__s__disable_therm1_reboot_fail____ffff0000907ee395;
  }
  else {
    iVar2 = FUN_ffff000090739d98(0);
    if (iVar2 < 0) {
      pcVar4 = s__s__disable_therm2_reboot_fail____ffff0000907e94d7;
    }
    else {
      iVar2 = FUN_ffff000090739e14(DAT_ffff00009086e088,0);
      if (-1 < iVar2) {
        FUN_ffff00009071ebf4
                  (0xc2000416,0x99716150,_DAT_ffff00009086e098,DAT_ffff00009086e0f0,
                   DAT_ffff00009086e0f8,0,0,0,local_50);
        if (local_50[0] != 0) {
          FUN_ffff00009077e11c
                    (s__s__call___smc_conuit_fail__0x_x_ffff0000907e7b44,
                     s_dfd_mcu_init_ffff0000907fd54a,0xc2000416,0x99716150);
          return;
        }
        iVar2 = FUN_ffff000090739ca0(1);
        if (-1 < iVar2) {
          if (DAT_ffff00009086e090 == 0) {
            uVar1 = DAT_ffff00009086e098;
            puVar3[3] = DAT_ffff00009086e098;
          }
          else {
            uVar1 = puVar3[3];
          }
          puVar3[2] = 1;
          FUN_ffff00009077e11c
                    (s__s__plat_sram_flag2___0x_x__plat_ffff0000907d12b0,
                     s_dfd_mcu_init_ffff0000907fd54a,uVar1,1);
          FUN_ffff00009074b4c4();
          return;
        }
        FUN_ffff00009077e11c
                  (s__s__dfd_mcu_start_count_fail___d_ffff0000907d4672,
                   s_dfd_mcu_init_ffff0000907fd54a,iVar2);
        return;
      }
      pcVar4 = s__s__Setup_dfd_mcu_timeout_fail____ffff0000907fd557;
    }
  }
  FUN_ffff00009077e11c(pcVar4,s_dfd_mcu_init_ffff0000907fd54a,iVar2);
  return;
}


