
void FUN_ffff0000be02c038(undefined8 param_1)

{
  int *piVar1;
  
  FUN_ffff0000be05fd98(s__s__In_dfd_init_ffff0000be0ab61e,s_dfd_mcu_init_aee_ffff0000be0ac824);
  piVar1 = (int *)FUN_ffff0000be02e184(0xdb45);
  if (piVar1 == (int *)0x0) {
    FUN_ffff0000be05fd98
              (s__s___Error__plat____NULL_ffff0000be0c4064,s_dfd_mcu_init_aee_ffff0000be0ac824);
    return;
  }
  if (*piVar1 == 0x5566520) {
    FUN_ffff0000be05fd98
              (s__s__plat_sram_flag2___0x_x__plat_ffff0000be0a483d,
               s_dfd_mcu_init_aee_ffff0000be0ac824,piVar1[3],piVar1[2]);
    FUN_ffff0000be0607b0(&DAT_ffff0000be11839c,&DAT_ffff0000be0af410,4);
    FUN_ffff0000be036764();
    FUN_ffff0000be02b8c8(param_1);
    return;
  }
  FUN_ffff0000be05fd98
            (s__s__SRAM_flag_magic_number_check_ffff0000be0c2cbc,s_dfd_mcu_init_aee_ffff0000be0ac824
            );
  return;
}


