
undefined8 FUN_ffff0000b8022ac0(long *param_1,undefined4 *param_2)

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
  if (DAT_ffff0000b811bbb0 == 0) {
    return 0;
  }
  if (DAT_ffff0000b811bb10 < 0x1e) {
    return 0;
  }
  local_38 = 0;
  piVar3 = (int *)FUN_ffff0000b8023da0(0xdb45);
  if (piVar3 == (int *)0x0) {
    FUN_ffff0000b804c33c
              (s__s__get_dbg_info_base_PLAT_SRAM__ffff0000b809549d,
               s_dfd_internal_dump_check_triggere_ffff0000b80918ee);
    cVar7 = '\0';
    goto LAB_ffff0000b8022c34;
  }
  if (*piVar3 != 0x5566520) {
    FUN_ffff0000b804c33c
              (s__s__SRAM_flag_magic_number_check_ffff0000b809e598,
               s_dfd_internal_dump_check_triggere_ffff0000b80918ee);
    cVar7 = '\0';
    goto LAB_ffff0000b8022c34;
  }
  uVar6 = piVar3[2];
  if ((uVar6 & 1) == 0) {
LAB_ffff0000b8022b50:
    FUN_ffff0000b804c33c
              (s__s__plat_sram_flag1___0x_x_ffff0000b80a03a5,
               s_dfd_internal_dump_check_triggere_ffff0000b80918ee,uVar6);
    uVar2 = FUN_ffff0000b8022920();
    FUN_ffff0000b804c33c
              (s__s__get_efuse_dfd_disabled_____0_ffff0000b8087269,
               s_dfd_internal_dump_check_triggere_ffff0000b80918ee,uVar2);
    DAT_ffff0000b811bbd0 = '\0';
  }
  else {
    iVar1 = FUN_ffff0000b8022920();
    if (iVar1 != 0) {
      uVar6 = piVar3[2];
      goto LAB_ffff0000b8022b50;
    }
    DAT_ffff0000b811bbd0 = '\x01';
  }
  FUN_ffff0000b804c33c
            (s__s__dfd_status___0x_x_ffff0000b8098c66,
             s_dfd_internal_dump_check_triggere_ffff0000b80918ee,DAT_ffff0000b80e9c90);
  if (DAT_ffff0000b80e9c90 == 0xfffffffc) {
    FUN_ffff0000b804c33c
              (s__s__dfd_status_is_as_initialized_ffff0000b8094558,
               s_dfd_internal_dump_check_triggere_ffff0000b80918ee);
    FUN_ffff0000b804c33c(s_it_should_be_save_dfd_status___i_ffff0000b8099c4c);
  }
  else if ((DAT_ffff0000b80e9c90 & 1) == 0) {
    DAT_ffff0000b811bbd0 = '\0';
  }
  FUN_ffff0000b804c33c
            (s__s__dfd_internal_dump_triggered___ffff0000b8090bdf,
             s_dfd_internal_dump_check_triggere_ffff0000b80918ee,DAT_ffff0000b811bbd0);
  cVar7 = DAT_ffff0000b811bbd0;
LAB_ffff0000b8022c34:
  if (DAT_ffff0000b811bb20 != 1) {
    if (cVar7 == '\0') {
      *param_2 = 0;
      return 0;
    }
    piVar3 = (int *)FUN_ffff0000b8023da0(0xdb45);
    if (piVar3 == (int *)0x0) {
      FUN_ffff0000b804c33c
                (s__s___Error__Can_t_get_plat_sram__ffff0000b808eda5,s_dfd_mcu_get_ffff0000b8088195)
      ;
    }
    else {
      iVar1 = *piVar3;
      if (iVar1 == 0x5566520) {
        uVar6 = piVar3[3];
        if ((DAT_ffff0000b811bb38 - 1U & uVar6) != 0) {
          FUN_ffff0000b804c33c
                    (s__s___Error__paddr__0x_llx_is_abn_ffff0000b809b864,
                     s_dfd_mcu_get_ffff0000b8088195,uVar6);
          return 0;
        }
        FUN_ffff0000b804c33c
                  (s__s__paddr____0x_llx__ffff0000b80881a1,s_dfd_mcu_get_ffff0000b8088195,uVar6);
        iVar1 = FUN_ffff0000b804a5f8
                          (PTR_DAT_ffff0000b81086d8,s_dfd_temp_buffer_ffff0000b8087259,
                           DAT_ffff0000b811bb30,&local_38,0xc,uVar6,0,1);
        if (iVar1 == 0) {
          if ((uVar6 != 0) && (local_38 != 0)) {
            *param_1 = local_38;
            uVar5 = FUN_ffff0000b8022a68(local_38);
            if ((uVar5 & 1) == 0) {
              FUN_ffff0000b804c33c
                        (s__s__check_dfd_valid___0_ffff0000b80918d5,s_dfd_mcu_get_ffff0000b8088195);
            }
            else {
              if (DAT_ffff0000b811bb90 == (code *)0x0) {
                FUN_ffff0000b804c33c
                          (s__s__no_plt_dfd_misc_decode_funct_ffff0000b809d7ce,
                           s_dfd_mcu_get_ffff0000b8088195);
              }
              else {
                (*DAT_ffff0000b811bb90)(*param_1);
              }
              FUN_ffff0000b8022e98(*param_1);
              FUN_ffff0000b8022f90(*param_1);
              *(undefined1 *)(*param_1 + (ulong)DAT_ffff0000b811bb5c) = DAT_ffff0000b811bb64;
              *(undefined1 *)(*param_1 + (ulong)DAT_ffff0000b811bb5c + 1) = DAT_ffff0000b811bb65;
              *(undefined1 *)(*param_1 + (ulong)DAT_ffff0000b811bb5c + 2) = DAT_ffff0000b811bb66;
              *(undefined1 *)(*param_1 + (ulong)DAT_ffff0000b811bb5c + 3) = DAT_ffff0000b811bb67;
              *(undefined1 *)(*param_1 + (ulong)DAT_ffff0000b811bb5c + 4) = DAT_ffff0000b811bb68;
              *(undefined1 *)(*param_1 + (ulong)DAT_ffff0000b811bb5c + 5) = DAT_ffff0000b811bb69;
              *(undefined1 *)(*param_1 + (ulong)DAT_ffff0000b811bb5c + 6) = DAT_ffff0000b811bb6a;
              *(undefined1 *)(*param_1 + (ulong)DAT_ffff0000b811bb5c + 7) = DAT_ffff0000b811bb6b;
            }
            *param_2 = (int)DAT_ffff0000b811bb30;
            return 1;
          }
          FUN_ffff0000b804c33c
                    (s__s__pa_or_va_is_invalid__>_skip_p_ffff0000b809e5c7,
                     s_dfd_mcu_get_ffff0000b8088195,uVar6);
          return 0;
        }
        pcVar4 = s__s__vmm_alloc_physical_fail___st_ffff0000b808fbf8;
      }
      else {
        pcVar4 = s__s__SRAM_flag_magic_number_check_ffff0000b809e598;
      }
      FUN_ffff0000b804c33c(pcVar4,s_dfd_mcu_get_ffff0000b8088195,iVar1);
    }
  }
  return 0;
}


