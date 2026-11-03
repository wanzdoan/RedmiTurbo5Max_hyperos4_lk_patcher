
undefined4 FUN_ffff0000be0502c0(ulong param_1)

{
  ulong uVar1;
  char *pcVar2;
  undefined8 uVar3;
  undefined4 local_44;
  undefined8 local_40;
  undefined8 local_38;
  undefined8 local_30;
  int local_24;
  
  local_38 = 0;
  local_30 = 0;
  local_40 = 0;
  local_44 = 0;
  local_24 = 0;
  uVar1 = FUN_ffff0000be04fba4(&local_24,1);
  FUN_ffff0000be05fd98
            (s__s__d____s___partition_control_o_ffff0000be0b6d43,
             s_GetBlackBoxPartitionControlConfi_ffff0000be0b033a,0xa86,s_BLACKBOX_ffff0000be0aa9ee,
             local_24);
  if (((uVar1 & 1) != 0) && (local_24 != 0)) {
    uVar1 = FUN_ffff0000be04cff4(s_blackbox_ffff0000be0bac45,&local_40,local_24,0x18);
    if ((uVar1 & 1) != 0) {
      FUN_ffff0000be05fd98
                (s__s__d____s___EIOEntryCount_is____ffff0000be0abce0,s_SetEIOCount_ffff0000be0b7e6b,
                 0xafa,s_BLACKBOX_ffff0000be0aa9ee,local_30 & 0xffffffff);
      if ((param_1 & 1) != 0) {
        local_30 = local_30 & 0xffffffff00000000;
        uVar1 = FUN_ffff0000be0501bc(&local_40);
        if ((uVar1 & 1) != 0) {
          DAT_ffff0000be126f20 = 1;
          return 1;
        }
        return 1;
      }
      if ((uint)local_30 < local_30._4_4_) {
        local_30 = CONCAT44(local_30._4_4_,(uint)local_30 + 1);
        FUN_ffff0000be05fd98
                  (s__s__d____s___EIOEntryCount_after_ffff0000be0c67b5,
                   s_SetEIOCount_ffff0000be0b7e6b,0xb02,s_BLACKBOX_ffff0000be0aa9ee);
        uVar1 = FUN_ffff0000be0501bc(&local_40);
        if ((uVar1 & 1) == 0) {
          return 1;
        }
        if ((uint)local_30 != local_30._4_4_) {
          return 1;
        }
        pcVar2 = s__s__d____s___EIOEntryCount_equal_ffff0000be0acec0;
        DAT_ffff0000be126f20 = 0;
        uVar3 = 0xb05;
      }
      else {
        if ((uint)local_30 != local_30._4_4_) {
          FUN_ffff0000be05fd98
                    (s__s__d____s___EIOEntryCount_is_no_ffff0000be0bb46b,
                     s_SetEIOCount_ffff0000be0b7e6b,0xb0b,s_BLACKBOX_ffff0000be0aa9ee);
          local_30 = 0x200000000;
          local_40 = 0x7f9d3a6b2e1c8f60;
          local_38 = 0;
          FUN_ffff0000be04fba4(&local_44,1);
          uVar1 = FUN_ffff0000be04d0b0(s_blackbox_ffff0000be0bac45,&local_40,local_44,0x18);
          if ((uVar1 & 1) == 0) {
            FUN_ffff0000be05fd98
                      (s__s__d____s___write_black_box_par_ffff0000be0a4f8c,
                       s_SetEIOCount_ffff0000be0b7e6b,0xb10,s_BLACKBOX_ffff0000be0aa9ee);
            return 0;
          }
          return 1;
        }
        pcVar2 = s__s__d____s___EIOEntryCount_has_r_ffff0000be0bfeff;
        uVar3 = 0xb08;
      }
      FUN_ffff0000be05fd98(pcVar2,s_SetEIOCount_ffff0000be0b7e6b,uVar3,s_BLACKBOX_ffff0000be0aa9ee);
      return 1;
    }
    FUN_ffff0000be05fd98
              (s__s__d____s___get_partition_contr_ffff0000be0aab53,
               s_GetBlackBoxPartitionControlConfi_ffff0000be0b033a,0xa8d,s_BLACKBOX_ffff0000be0aa9ee
              );
  }
  FUN_ffff0000be05fd98
            (s__s__d____s___Get_Black_Box_Confi_ffff0000be0abcb3,s_SetEIOCount_ffff0000be0b7e6b,
             0xaf7,s_BLACKBOX_ffff0000be0aa9ee,0);
  return 0;
}


