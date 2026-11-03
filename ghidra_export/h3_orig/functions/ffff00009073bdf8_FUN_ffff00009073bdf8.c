// ffff00009073bdf8  size=984  FUN_ffff00009073bdf8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009073bdf8(long *param_1,undefined4 *param_2)

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
  if (_DAT_ffff00009086e088 == 0) {
    return 0;
  }
  if (_DAT_ffff00009086dfe8 < 0x1e) {
    return 0;
  }
  local_38 = 0;
  piVar3 = (int *)FUN_ffff00009073dc20(0xdb45);
  if (piVar3 == (int *)0x0) {
    FUN_ffff00009077de54
              (s__s__get_dbg_info_base_PLAT_SRAM__ffff0000907ec559,
               s_dfd_internal_dump_check_triggere_ffff0000907e624f);
    cVar7 = '\0';
    goto LAB_ffff00009073bf6c;
  }
  if (*piVar3 != 0x5566520) {
    FUN_ffff00009077de54
              (s__s__SRAM_flag_magic_number_check_ffff0000907fd3d3,
               s_dfd_internal_dump_check_triggere_ffff0000907e624f);
    cVar7 = '\0';
    goto LAB_ffff00009073bf6c;
  }
  uVar6 = piVar3[2];
  if ((uVar6 & 1) == 0) {
LAB_ffff00009073be88:
    FUN_ffff00009077de54
              (s__s__plat_sram_flag1___0x_x_ffff000090800aaf,
               s_dfd_internal_dump_check_triggere_ffff0000907e624f,uVar6);
    uVar2 = FUN_ffff00009073bc58();
    FUN_ffff00009077de54
              (s__s__get_efuse_dfd_disabled_____0_ffff0000907d2d57,
               s_dfd_internal_dump_check_triggere_ffff0000907e624f,uVar2);
    DAT_ffff00009086e0a8 = '\0';
  }
  else {
    iVar1 = FUN_ffff00009073bc58();
    if (iVar1 != 0) {
      uVar6 = piVar3[2];
      goto LAB_ffff00009073be88;
    }
    DAT_ffff00009086e0a8 = '\x01';
  }
  FUN_ffff00009077de54
            (s__s__dfd_status___0x_x_ffff0000907f2bee,
             s_dfd_internal_dump_check_triggere_ffff0000907e624f,DAT_ffff000090819f08);
  if (DAT_ffff000090819f08 == 0xfffffffc) {
    FUN_ffff00009077de54
              (s__s__dfd_status_is_as_initialized_ffff0000907eaca3,
               s_dfd_internal_dump_check_triggere_ffff0000907e624f);
    FUN_ffff00009077de54(s_it_should_be_save_dfd_status___i_ffff0000907f487a);
  }
  else if ((DAT_ffff000090819f08 & 1) == 0) {
    DAT_ffff00009086e0a8 = '\0';
  }
  FUN_ffff00009077de54
            (s__s__dfd_internal_dump_triggered___ffff0000907e4a59,
             s_dfd_internal_dump_check_triggere_ffff0000907e624f,DAT_ffff00009086e0a8);
  cVar7 = DAT_ffff00009086e0a8;
LAB_ffff00009073bf6c:
  if (_DAT_ffff00009086dff8 != 1) {
    if (cVar7 == '\0') {
      *param_2 = 0;
      return 0;
    }
    piVar3 = (int *)FUN_ffff00009073dc20(0xdb45);
    if (piVar3 == (int *)0x0) {
      FUN_ffff00009077de54
                (s__s___Error__Can_t_get_plat_sram__ffff0000907e140c,s_dfd_mcu_get_ffff0000907d4657)
      ;
    }
    else {
      iVar1 = *piVar3;
      if (iVar1 == 0x5566520) {
        uVar6 = piVar3[3];
        if ((_DAT_ffff00009086e010 - 1U & uVar6) != 0) {
          FUN_ffff00009077de54
                    (s__s___Error__paddr__0x_llx_is_abn_ffff0000907f81d0,
                     s_dfd_mcu_get_ffff0000907d4657,uVar6);
          return 0;
        }
        FUN_ffff00009077de54
                  (s__s__paddr____0x_llx__ffff0000907d4663,s_dfd_mcu_get_ffff0000907d4657,uVar6);
        iVar1 = FUN_ffff00009077b79c
                          (DAT_ffff000090839908,s_dfd_temp_buffer_ffff0000907d2ce6,
                           _DAT_ffff00009086e008,&local_38,0xc,uVar6,0,1);
        if (iVar1 == 0) {
          if ((uVar6 != 0) && (local_38 != 0)) {
            *param_1 = local_38;
            uVar5 = FUN_ffff00009073bda0(local_38);
            if ((uVar5 & 1) == 0) {
              FUN_ffff00009077de54
                        (s__s__check_dfd_valid___0_ffff0000907e6236,s_dfd_mcu_get_ffff0000907d4657);
            }
            else {
              if (_DAT_ffff00009086e068 == (code *)0x0) {
                FUN_ffff00009077de54
                          (s__s__no_plt_dfd_misc_decode_funct_ffff0000907fb804,
                           s_dfd_mcu_get_ffff0000907d4657);
              }
              else {
                (*_DAT_ffff00009086e068)(*param_1);
              }
              FUN_ffff00009073c1d0(*param_1);
              FUN_ffff00009073c2c8(*param_1);
              *(undefined1 *)(*param_1 + (ulong)_DAT_ffff00009086e034) = DAT_ffff00009086e03c;
              *(undefined1 *)(*param_1 + (ulong)_DAT_ffff00009086e034 + 1) = DAT_ffff00009086e03d;
              *(undefined1 *)(*param_1 + (ulong)_DAT_ffff00009086e034 + 2) = DAT_ffff00009086e03e;
              *(undefined1 *)(*param_1 + (ulong)_DAT_ffff00009086e034 + 3) = DAT_ffff00009086e03f;
              *(undefined1 *)(*param_1 + (ulong)_DAT_ffff00009086e034 + 4) = DAT_ffff00009086e040;
              *(undefined1 *)(*param_1 + (ulong)_DAT_ffff00009086e034 + 5) = DAT_ffff00009086e041;
              *(undefined1 *)(*param_1 + (ulong)_DAT_ffff00009086e034 + 6) = DAT_ffff00009086e042;
              *(undefined1 *)(*param_1 + (ulong)_DAT_ffff00009086e034 + 7) = DAT_ffff00009086e043;
            }
            *param_2 = (int)_DAT_ffff00009086e008;
            return 1;
          }
          FUN_ffff00009077de54
                    (s__s__pa_or_va_is_invalid__>_skip_p_ffff0000907fd402,
                     s_dfd_mcu_get_ffff0000907d4657,uVar6);
          return 0;
        }
        pcVar4 = s__s__vmm_alloc_physical_fail___st_ffff0000907e2dc8;
      }
      else {
        pcVar4 = s__s__SRAM_flag_magic_number_check_ffff0000907fd3d3;
      }
      FUN_ffff00009077de54(pcVar4,s_dfd_mcu_get_ffff0000907d4657,iVar1);
    }
  }
  return 0;
}


