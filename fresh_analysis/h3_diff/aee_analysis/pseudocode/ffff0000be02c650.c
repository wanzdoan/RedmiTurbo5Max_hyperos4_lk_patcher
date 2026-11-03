
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000be02c650(long *param_1,undefined4 *param_2)

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
  if (DAT_ffff0000be1183e8 == 0) {
    return 0;
  }
  if (DAT_ffff0000be118348 < 0x1e) {
    return 0;
  }
  local_38 = 0;
  piVar3 = (int *)FUN_ffff0000be02e184(0xdb45);
  if (piVar3 == (int *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__get_dbg_info_base_PLAT_SRAM__ffff0000be0b77b2,
               s_dfd_internal_dump_check_triggere_ffff0000be0b2fb2);
    cVar7 = '\0';
    goto LAB_ffff0000be02c7c4;
  }
  if (*piVar3 != 0x5566520) {
    FUN_ffff0000be05fd98
              (s__s__SRAM_flag_magic_number_check_ffff0000be0c2cbc,
               s_dfd_internal_dump_check_triggere_ffff0000be0b2fb2);
    cVar7 = '\0';
    goto LAB_ffff0000be02c7c4;
  }
  uVar6 = piVar3[2];
  if ((uVar6 & 1) == 0) {
LAB_ffff0000be02c6e0:
    FUN_ffff0000be05fd98
              (s__s__plat_sram_flag1___0x_x_ffff0000be0c51eb,
               s_dfd_internal_dump_check_triggere_ffff0000be0b2fb2,uVar6);
    uVar2 = FUN_ffff0000be02c4b0();
    FUN_ffff0000be05fd98
              (s__s__get_efuse_dfd_disabled_____0_ffff0000be0a5b9f,
               s_dfd_internal_dump_check_triggere_ffff0000be0b2fb2,uVar2);
    DAT_ffff0000be118408 = '\0';
  }
  else {
    iVar1 = FUN_ffff0000be02c4b0();
    if (iVar1 != 0) {
      uVar6 = piVar3[2];
      goto LAB_ffff0000be02c6e0;
    }
    DAT_ffff0000be118408 = '\x01';
  }
  FUN_ffff0000be05fd98
            (s__s__dfd_status___0x_x_ffff0000be0bbe9d,
             s_dfd_internal_dump_check_triggere_ffff0000be0b2fb2,_DAT_ffff0000be0d7260);
  if (_DAT_ffff0000be0d7260 == 0xfffffffc) {
    FUN_ffff0000be05fd98
              (s__s__dfd_status_is_as_initialized_ffff0000be0b6586,
               s_dfd_internal_dump_check_triggere_ffff0000be0b2fb2);
    FUN_ffff0000be05fd98(s_it_should_be_save_dfd_status___i_ffff0000be0bd1fb);
  }
  else if ((_DAT_ffff0000be0d7260 & 1) == 0) {
    DAT_ffff0000be118408 = '\0';
  }
  FUN_ffff0000be05fd98
            (s__s__dfd_internal_dump_triggered___ffff0000be0b1eda,
             s_dfd_internal_dump_check_triggere_ffff0000be0b2fb2,DAT_ffff0000be118408);
  cVar7 = DAT_ffff0000be118408;
LAB_ffff0000be02c7c4:
  if (DAT_ffff0000be118358 != 1) {
    if (cVar7 == '\0') {
      *param_2 = 0;
      return 0;
    }
    piVar3 = (int *)FUN_ffff0000be02e184(0xdb45);
    if (piVar3 == (int *)0x0) {
      FUN_ffff0000be05fd98
                (s__s___Error__Can_t_get_plat_sram__ffff0000be0afa29,s_dfd_mcu_get_ffff0000be0a6e12)
      ;
    }
    else {
      iVar1 = *piVar3;
      if (iVar1 == 0x5566520) {
        uVar6 = piVar3[3];
        if ((DAT_ffff0000be118370 - 1U & uVar6) != 0) {
          FUN_ffff0000be05fd98
                    (s__s___Error__paddr__0x_llx_is_abn_ffff0000be0bf7dd,
                     s_dfd_mcu_get_ffff0000be0a6e12,uVar6);
          return 0;
        }
        FUN_ffff0000be05fd98
                  (s__s__paddr____0x_llx__ffff0000be0a6e1e,s_dfd_mcu_get_ffff0000be0a6e12,uVar6);
        iVar1 = FUN_ffff0000be05d9a8
                          (PTR_DAT_ffff0000be0f5570,s_dfd_temp_buffer_ffff0000be0a5b2e,
                           DAT_ffff0000be118368,&local_38,0xc,uVar6,0,1);
        if (iVar1 == 0) {
          if ((uVar6 != 0) && (local_38 != 0)) {
            *param_1 = local_38;
            uVar5 = FUN_ffff0000be02c5f8(local_38);
            if ((uVar5 & 1) == 0) {
              FUN_ffff0000be05fd98
                        (s__s__check_dfd_valid___0_ffff0000be0b2f99,s_dfd_mcu_get_ffff0000be0a6e12);
            }
            else {
              if (DAT_ffff0000be1183c8 == (code *)0x0) {
                FUN_ffff0000be05fd98
                          (s__s__no_plt_dfd_misc_decode_funct_ffff0000be0c1bf6,
                           s_dfd_mcu_get_ffff0000be0a6e12);
              }
              else {
                (*DAT_ffff0000be1183c8)(*param_1);
              }
              FUN_ffff0000be02ca28(*param_1);
              FUN_ffff0000be02cb20(*param_1);
              *(undefined1 *)(*param_1 + (ulong)DAT_ffff0000be118394) = DAT_ffff0000be11839c;
              *(undefined1 *)(*param_1 + (ulong)DAT_ffff0000be118394 + 1) = DAT_ffff0000be11839d;
              *(undefined1 *)(*param_1 + (ulong)DAT_ffff0000be118394 + 2) = DAT_ffff0000be11839e;
              *(undefined1 *)(*param_1 + (ulong)DAT_ffff0000be118394 + 3) = DAT_ffff0000be11839f;
              *(undefined1 *)(*param_1 + (ulong)DAT_ffff0000be118394 + 4) = DAT_ffff0000be1183a0;
              *(undefined1 *)(*param_1 + (ulong)DAT_ffff0000be118394 + 5) = DAT_ffff0000be1183a1;
              *(undefined1 *)(*param_1 + (ulong)DAT_ffff0000be118394 + 6) = DAT_ffff0000be1183a2;
              *(undefined1 *)(*param_1 + (ulong)DAT_ffff0000be118394 + 7) = DAT_ffff0000be1183a3;
            }
            *param_2 = (int)DAT_ffff0000be118368;
            return 1;
          }
          FUN_ffff0000be05fd98
                    (s__s__pa_or_va_is_invalid__>_skip_p_ffff0000be0c2ceb,
                     s_dfd_mcu_get_ffff0000be0a6e12,uVar6);
          return 0;
        }
        pcVar4 = s__s__vmm_alloc_physical_fail___st_ffff0000be0b0c7b;
      }
      else {
        pcVar4 = s__s__SRAM_flag_magic_number_check_ffff0000be0c2cbc;
      }
      FUN_ffff0000be05fd98(pcVar4,s_dfd_mcu_get_ffff0000be0a6e12,iVar1);
    }
  }
  return 0;
}


