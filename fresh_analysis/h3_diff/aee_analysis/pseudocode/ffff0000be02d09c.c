
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be02d09c(undefined8 param_1)

{
  int iVar1;
  undefined *puVar2;
  undefined *puVar3;
  int *piVar4;
  char *pcVar5;
  
  puVar3 = PTR_DAT_ffff0000be0f5630;
  puVar2 = PTR_DAT_ffff0000be0f5620;
  iVar1 = _DAT_ffff00001000d80c;
  FUN_ffff0000be05fd98
            (s__s__DFD_SOC_STATUS___0x_X__DFD_M_ffff0000be0a7fe5,s_dfd_soc_init_aee_ffff0000be0c0a6c
             ,_DAT_ffff00001000d80c,*(undefined4 *)PTR_DAT_ffff0000be0f5630,
             *(undefined4 *)PTR_DAT_ffff0000be0f5620);
  if (((iVar1 == 0) && ((*puVar3 & 1) == 0)) && ((*puVar2 & 1) == 0)) {
    pcVar5 = s__s__DFD_SOC_not_start__maybe_efu_ffff0000be0c5231;
  }
  else {
    piVar4 = (int *)FUN_ffff0000be02e184(0xdb45);
    if (piVar4 != (int *)0x0) {
      if (*piVar4 == 0x5566520) {
        FUN_ffff0000be05fd98
                  (s__s__plat_sram_flag0___0x_x_ffff0000be0b65ae,s_dfd_soc_init_aee_ffff0000be0c0a6c
                   ,piVar4[1]);
        FUN_ffff0000be02ce30(param_1);
        return;
      }
      FUN_ffff0000be05fd98
                (s__s__SRAM_flag_magic_number_check_ffff0000be0c2cbc,
                 s_dfd_soc_init_aee_ffff0000be0c0a6c);
      return;
    }
    pcVar5 = s__s__plat____NULL_ffff0000be0a48cc;
  }
  FUN_ffff0000be05fd98(pcVar5,s_dfd_soc_init_aee_ffff0000be0c0a6c);
  return;
}


