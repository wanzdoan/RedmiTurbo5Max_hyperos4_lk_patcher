
undefined8 FUN_ffff00009073cd34(long *param_1,undefined4 *param_2)

{
  uint uVar1;
  undefined8 uVar2;
  int iVar3;
  int *piVar4;
  char *pcVar5;
  long local_38;
  
  if (param_1 == (long *)0x0) {
    return 0;
  }
  if (param_2 == (undefined4 *)0x0) {
    return 0;
  }
  *param_2 = 0;
  *param_1 = 0;
  if (DAT_ffff00009086e170 != 0) {
    piVar4 = (int *)FUN_ffff00009073deb8(0xdb45);
    if (piVar4 == (int *)0x0) {
      FUN_ffff00009077e11c
                (s__s___Error__Can_t_get_plat_sram__ffff0000907dfaca,s_dfd_soc_get_ffff0000907d7ba0)
      ;
    }
    else {
      local_38 = 0;
      iVar3 = *piVar4;
      if (iVar3 == 0x5566520) {
        uVar1 = piVar4[1];
        if ((DAT_ffff00009086e160 - 1U & uVar1) != 0) {
          FUN_ffff00009077e11c
                    (s__s___Error__paddr__0x_llx_is_abn_ffff0000907f836c,
                     s_dfd_soc_get_ffff0000907d7ba0,uVar1);
          return 0;
        }
        FUN_ffff00009077e11c
                  (s__s__paddr____0x_llx__ffff0000907d46a0,s_dfd_soc_get_ffff0000907d7ba0,uVar1);
        iVar3 = FUN_ffff00009077ba64
                          (PTR_DAT_ffff000090839928,s_dfd_temp_buffer_ffff0000907d2d23,
                           DAT_ffff00009086e158,&local_38,0xc,uVar1,0,1);
        uVar2 = DAT_ffff00009086e158;
        if (iVar3 == 0) {
          if ((uVar1 != 0) && (local_38 != 0)) {
            *param_1 = local_38;
            *param_2 = (int)uVar2;
            return 1;
          }
          FUN_ffff00009077e11c
                    (s__s__pa_or_va_is_invalid__>_skip_p_ffff0000907fd5aa,
                     s_dfd_soc_get_ffff0000907d7ba0,uVar1);
          return 0;
        }
        pcVar5 = s__s__vmm_alloc_physical_fail___st_ffff0000907e2e99;
      }
      else {
        pcVar5 = s__s__SRAM_flag_magic_number_check_ffff0000907fd57b;
      }
      FUN_ffff00009077e11c(pcVar5,s_dfd_soc_get_ffff0000907d7ba0,iVar3);
    }
  }
  return 0;
}


