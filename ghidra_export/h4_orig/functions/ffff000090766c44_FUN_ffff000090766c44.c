// ffff000090766c44  size=596  FUN_ffff000090766c44

undefined4 FUN_ffff000090766c44(ulong param_1)

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
  uVar1 = FUN_ffff0000907661c0(&local_24,1);
  FUN_ffff00009077e11c
            (s__s__d____s___partition_control_o_ffff0000907eb6c6,
             s_GetBlackBoxPartitionControlConfi_ffff0000907e1dcf,0xa86,s_BLACKBOX_ffff0000907d9a1f,
             local_24);
  if (((uVar1 & 1) != 0) && (local_24 != 0)) {
    uVar1 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,&local_40,local_24,0x18);
    if ((uVar1 & 1) != 0) {
      FUN_ffff00009077e11c
                (s__s__d____s___EIOEntryCount_is____ffff0000907db662,s_SetEIOCount_ffff0000907ed04f,
                 0xafa,s_BLACKBOX_ffff0000907d9a1f,local_30 & 0xffffffff);
      if ((param_1 & 1) != 0) {
        local_30 = local_30 & 0xffffffff00000000;
        uVar1 = FUN_ffff0000907667d8(&local_40);
        if ((uVar1 & 1) != 0) {
          DAT_ffff00009087ccf8 = 1;
          return 1;
        }
        return 1;
      }
      if ((uint)local_30 < local_30._4_4_) {
        local_30 = CONCAT44(local_30._4_4_,(uint)local_30 + 1);
        FUN_ffff00009077e11c
                  (s__s__d____s___EIOEntryCount_after_ffff000090802da0,
                   s_SetEIOCount_ffff0000907ed04f,0xb02,s_BLACKBOX_ffff0000907d9a1f);
        uVar1 = FUN_ffff0000907667d8(&local_40);
        if ((uVar1 & 1) == 0) {
          return 1;
        }
        if ((uint)local_30 != local_30._4_4_) {
          return 1;
        }
        pcVar2 = s__s__d____s___EIOEntryCount_equal_ffff0000907dd184;
        DAT_ffff00009087ccf8 = 0;
        uVar3 = 0xb05;
      }
      else {
        if ((uint)local_30 != local_30._4_4_) {
          FUN_ffff00009077e11c
                    (s__s__d____s___EIOEntryCount_is_no_ffff0000907f1cb6,
                     s_SetEIOCount_ffff0000907ed04f,0xb0b,s_BLACKBOX_ffff0000907d9a1f);
          local_30 = 0x200000000;
          local_40 = 0x7f9d3a6b2e1c8f60;
          local_38 = 0;
          FUN_ffff0000907661c0(&local_44,1);
          uVar1 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,&local_40,local_44,0x18);
          if ((uVar1 & 1) == 0) {
            FUN_ffff00009077e11c
                      (s__s__d____s___write_black_box_par_ffff0000907d1be3,
                       s_SetEIOCount_ffff0000907ed04f,0xb10,s_BLACKBOX_ffff0000907d9a1f);
            return 0;
          }
          return 1;
        }
        pcVar2 = s__s__d____s___EIOEntryCount_has_r_ffff0000907f8cb9;
        uVar3 = 0xb08;
      }
      FUN_ffff00009077e11c(pcVar2,s_SetEIOCount_ffff0000907ed04f,uVar3,s_BLACKBOX_ffff0000907d9a1f);
      return 1;
    }
    FUN_ffff00009077e11c
              (s__s__d____s___get_partition_contr_ffff0000907d9bd5,
               s_GetBlackBoxPartitionControlConfi_ffff0000907e1dcf,0xa8d,s_BLACKBOX_ffff0000907d9a1f
              );
  }
  FUN_ffff00009077e11c
            (s__s__d____s___Get_Black_Box_Confi_ffff0000907db635,s_SetEIOCount_ffff0000907ed04f,
             0xaf7,s_BLACKBOX_ffff0000907d9a1f,0);
  return 0;
}


