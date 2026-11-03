// ffff0000907669ac  size=596  FUN_ffff0000907669ac

undefined4 FUN_ffff0000907669ac(ulong param_1)

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
  uVar1 = FUN_ffff000090765f28(&local_24,1);
  FUN_ffff00009077de54
            (s__s__d____s___partition_control_o_ffff0000907eb52c,
             s_GetBlackBoxPartitionControlConfi_ffff0000907e1ce7,0xa86,s_BLACKBOX_ffff0000907d9988,
             local_24);
  if (((uVar1 & 1) != 0) && (local_24 != 0)) {
    uVar1 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c04,&local_40,local_24,0x18);
    if ((uVar1 & 1) != 0) {
      FUN_ffff00009077de54
                (s__s__d____s___EIOEntryCount_is____ffff0000907db540,s_SetEIOCount_ffff0000907eceb5,
                 0xafa,s_BLACKBOX_ffff0000907d9988,local_30 & 0xffffffff);
      if ((param_1 & 1) != 0) {
        local_30 = local_30 & 0xffffffff00000000;
        uVar1 = FUN_ffff000090766540(&local_40);
        if ((uVar1 & 1) != 0) {
          DAT_ffff00009087cc60 = 1;
          return 1;
        }
        return 1;
      }
      if ((uint)local_30 < local_30._4_4_) {
        local_30 = CONCAT44(local_30._4_4_,(uint)local_30 + 1);
        FUN_ffff00009077de54
                  (s__s__d____s___EIOEntryCount_after_ffff000090802b9c,
                   s_SetEIOCount_ffff0000907eceb5,0xb02,s_BLACKBOX_ffff0000907d9988);
        uVar1 = FUN_ffff000090766540(&local_40);
        if ((uVar1 & 1) == 0) {
          return 1;
        }
        if ((uint)local_30 != local_30._4_4_) {
          return 1;
        }
        pcVar2 = s__s__d____s___EIOEntryCount_equal_ffff0000907dd097;
        DAT_ffff00009087cc60 = 0;
        uVar3 = 0xb05;
      }
      else {
        if ((uint)local_30 != local_30._4_4_) {
          FUN_ffff00009077de54
                    (s__s__d____s___EIOEntryCount_is_no_ffff0000907f1b2e,
                     s_SetEIOCount_ffff0000907eceb5,0xb0b,s_BLACKBOX_ffff0000907d9988);
          local_30 = 0x200000000;
          local_40 = 0x7f9d3a6b2e1c8f60;
          local_38 = 0;
          FUN_ffff000090765f28(&local_44,1);
          uVar1 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c04,&local_40,local_44,0x18);
          if ((uVar1 & 1) == 0) {
            FUN_ffff00009077de54
                      (s__s__d____s___write_black_box_par_ffff0000907d1ba6,
                       s_SetEIOCount_ffff0000907eceb5,0xb10,s_BLACKBOX_ffff0000907d9988);
            return 0;
          }
          return 1;
        }
        pcVar2 = s__s__d____s___EIOEntryCount_has_r_ffff0000907f8b1d;
        uVar3 = 0xb08;
      }
      FUN_ffff00009077de54(pcVar2,s_SetEIOCount_ffff0000907eceb5,uVar3,s_BLACKBOX_ffff0000907d9988);
      return 1;
    }
    FUN_ffff00009077de54
              (s__s__d____s___get_partition_contr_ffff0000907d9b3e,
               s_GetBlackBoxPartitionControlConfi_ffff0000907e1ce7,0xa8d,s_BLACKBOX_ffff0000907d9988
              );
  }
  FUN_ffff00009077de54
            (s__s__d____s___Get_Black_Box_Confi_ffff0000907db513,s_SetEIOCount_ffff0000907eceb5,
             0xaf7,s_BLACKBOX_ffff0000907d9988,0);
  return 0;
}


