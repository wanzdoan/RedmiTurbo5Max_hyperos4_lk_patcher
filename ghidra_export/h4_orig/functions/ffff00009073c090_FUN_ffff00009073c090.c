// ffff00009073c090  size=984  FUN_ffff00009073c090

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009073c090(long *param_1,undefined4 *param_2)

{
  int iVar1;
  undefined4 uVar2;
  int *piVar3;
  char *pcVar4;
  ulong uVar5;
  uint uVar6;
  char cVar7;
  long local_38;
  
  if (param_1 == (long *)0x0) {
    return 0;
  }
  if (param_2 == (undefined4 *)0x0) {
    return 0;
  }
  *param_2 = 0;
  *param_1 = 0;
  if (_DAT_ffff00009086e120 == 0) {
    return 0;
  }
  if (_DAT_ffff00009086e080 < 0x1e) {
    return 0;
  }
  local_38 = 0;
  piVar3 = (int *)FUN_ffff00009073deb8(0xdb45);
  if (piVar3 == (int *)0x0) {
    FUN_ffff00009077e11c
              (s__s__get_dbg_info_base_PLAT_SRAM__ffff0000907ec6dc,
               s_dfd_internal_dump_check_triggere_ffff0000907e6363);
    cVar7 = '\0';
    goto LAB_ffff00009073c204;
  }
  if (*piVar3 != 0x5566520) {
    FUN_ffff00009077e11c
              (s__s__SRAM_flag_magic_number_check_ffff0000907fd57b,
               s_dfd_internal_dump_check_triggere_ffff0000907e6363);
    cVar7 = '\0';
    goto LAB_ffff00009073c204;
  }
  uVar6 = piVar3[2];
  if ((uVar6 & 1) == 0) {
LAB_ffff00009073c120:
    FUN_ffff00009077e11c
              (s__s__plat_sram_flag1___0x_x_ffff000090800c8b,
               s_dfd_internal_dump_check_triggere_ffff0000907e6363,uVar6);
    uVar2 = FUN_ffff00009073bef0();
    FUN_ffff00009077e11c
              (s__s__get_efuse_dfd_disabled_____0_ffff0000907d2d94,
               s_dfd_internal_dump_check_triggere_ffff0000907e6363,uVar2);
    DAT_ffff00009086e140 = '\0';
  }
  else {
    iVar1 = FUN_ffff00009073bef0();
    if (iVar1 != 0) {
      uVar6 = piVar3[2];
      goto LAB_ffff00009073c120;
    }
    DAT_ffff00009086e140 = '\x01';
  }
  FUN_ffff00009077e11c
            (s__s__dfd_status___0x_x_ffff0000907f2d7a,
             s_dfd_internal_dump_check_triggere_ffff0000907e6363,DAT_ffff000090819f28);
  if (DAT_ffff000090819f28 == 0xfffffffc) {
    FUN_ffff00009077e11c
              (s__s__dfd_status_is_as_initialized_ffff0000907eae26,
               s_dfd_internal_dump_check_triggere_ffff0000907e6363);
    FUN_ffff00009077e11c(s_it_should_be_save_dfd_status___i_ffff0000907f49e7);
  }
  else if ((DAT_ffff000090819f28 & 1) == 0) {
    DAT_ffff00009086e140 = '\0';
  }
  FUN_ffff00009077e11c
            (s__s__dfd_internal_dump_triggered___ffff0000907e4b5d,
             s_dfd_internal_dump_check_triggere_ffff0000907e6363,DAT_ffff00009086e140);
  cVar7 = DAT_ffff00009086e140;
LAB_ffff00009073c204:
  if (_DAT_ffff00009086e090 != 1) {
    if (cVar7 == '\0') {
      *param_2 = 0;
      return 0;
    }
    piVar3 = (int *)FUN_ffff00009073deb8(0xdb45);
    if (piVar3 == (int *)0x0) {
      FUN_ffff00009077e11c
                (s__s___Error__Can_t_get_plat_sram__ffff0000907e14dd,s_dfd_mcu_get_ffff0000907d4694)
      ;
    }
    else {
      iVar1 = *piVar3;
      if (iVar1 == 0x5566520) {
        uVar6 = piVar3[3];
        if ((_DAT_ffff00009086e0a8 - 1U & uVar6) != 0) {
          FUN_ffff00009077e11c
                    (s__s___Error__paddr__0x_llx_is_abn_ffff0000907f836c,
                     s_dfd_mcu_get_ffff0000907d4694,uVar6);
          return 0;
        }
        FUN_ffff00009077e11c
                  (s__s__paddr____0x_llx__ffff0000907d46a0,s_dfd_mcu_get_ffff0000907d4694,uVar6);
        iVar1 = FUN_ffff00009077ba64
                          (DAT_ffff000090839928,s_dfd_temp_buffer_ffff0000907d2d23,
                           _DAT_ffff00009086e0a0,&local_38,0xc,uVar6,0,1);
        if (iVar1 == 0) {
          if ((uVar6 != 0) && (local_38 != 0)) {
            *param_1 = local_38;
            uVar5 = FUN_ffff00009073c038(local_38);
            if ((uVar5 & 1) == 0) {
              FUN_ffff00009077e11c
                        (s__s__check_dfd_valid___0_ffff0000907e634a,s_dfd_mcu_get_ffff0000907d4694);
            }
            else {
              if (_DAT_ffff00009086e100 == (code *)0x0) {
                FUN_ffff00009077e11c
                          (s__s__no_plt_dfd_misc_decode_funct_ffff0000907fb9a0,
                           s_dfd_mcu_get_ffff0000907d4694);
              }
              else {
                (*_DAT_ffff00009086e100)(*param_1);
              }
              FUN_ffff00009073c468(*param_1);
              FUN_ffff00009073c560(*param_1);
              *(undefined1 *)(*param_1 + (ulong)_DAT_ffff00009086e0cc) = DAT_ffff00009086e0d4;
              *(undefined1 *)(*param_1 + (ulong)_DAT_ffff00009086e0cc + 1) = DAT_ffff00009086e0d5;
              *(undefined1 *)(*param_1 + (ulong)_DAT_ffff00009086e0cc + 2) = DAT_ffff00009086e0d6;
              *(undefined1 *)(*param_1 + (ulong)_DAT_ffff00009086e0cc + 3) = DAT_ffff00009086e0d7;
              *(undefined1 *)(*param_1 + (ulong)_DAT_ffff00009086e0cc + 4) = DAT_ffff00009086e0d8;
              *(undefined1 *)(*param_1 + (ulong)_DAT_ffff00009086e0cc + 5) = DAT_ffff00009086e0d9;
              *(undefined1 *)(*param_1 + (ulong)_DAT_ffff00009086e0cc + 6) = DAT_ffff00009086e0da;
              *(undefined1 *)(*param_1 + (ulong)_DAT_ffff00009086e0cc + 7) = DAT_ffff00009086e0db;
            }
            *param_2 = (int)_DAT_ffff00009086e0a0;
            return 1;
          }
          FUN_ffff00009077e11c
                    (s__s__pa_or_va_is_invalid__>_skip_p_ffff0000907fd5aa,
                     s_dfd_mcu_get_ffff0000907d4694,uVar6);
          return 0;
        }
        pcVar4 = s__s__vmm_alloc_physical_fail___st_ffff0000907e2e99;
      }
      else {
        pcVar4 = s__s__SRAM_flag_magic_number_check_ffff0000907fd57b;
      }
      FUN_ffff00009077e11c(pcVar4,s_dfd_mcu_get_ffff0000907d4694,iVar1);
    }
  }
  return 0;
}


