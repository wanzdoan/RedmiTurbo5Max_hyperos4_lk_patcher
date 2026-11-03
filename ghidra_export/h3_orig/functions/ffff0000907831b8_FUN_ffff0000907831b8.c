// ffff0000907831b8  size=324  FUN_ffff0000907831b8

undefined4 FUN_ffff0000907831b8(undefined8 param_1)

{
  int iVar1;
  undefined4 uVar2;
  
  iVar1 = FUN_ffff00009077ede4(param_1,&DAT_ffff0000907e9e9d);
  if (iVar1 == 0) {
    uVar2 = 1;
  }
  else {
    iVar1 = FUN_ffff00009077ede4(param_1,&DAT_ffff0000907e395e);
    if (iVar1 == 0) {
      uVar2 = 2;
    }
    else {
      iVar1 = FUN_ffff00009077ede4(param_1,&DAT_ffff0000907f3d69);
      if (iVar1 == 0) {
        uVar2 = 5;
      }
      else {
        iVar1 = FUN_ffff00009077ede4(param_1,&DAT_ffff0000907ebd2f);
        if (iVar1 == 0) {
          uVar2 = 7;
        }
        else {
          iVar1 = FUN_ffff00009077ede4(param_1,s_partition_ffff0000907f35bb);
          if (iVar1 == 0) {
            uVar2 = 6;
          }
          else {
            iVar1 = FUN_ffff00009077ede4(param_1,s_internal_storage_ffff0000907e834c);
            if (iVar1 != 0) {
              FUN_ffff00009077de54
                        (s__s__d___s__unknown_output_device_ffff0000907e53fc,
                         s_mrdump_set_output_device_ffff0000907f0496,0x45,
                         s_mrdump_set_output_device_ffff0000907f0496,param_1);
              return 0;
            }
            uVar2 = 3;
          }
        }
      }
    }
  }
  iVar1 = FUN_ffff000090782e8c(s_mrdump_output_ffff0000907debee,param_1);
  if (iVar1 == 0) {
    FUN_ffff00009077de54
              (s__s__d___s__defaults___s_ffff000090801534,
               s_mrdump_set_output_device_ffff0000907f0496,0x41,
               s_mrdump_set_output_device_ffff0000907f0496,param_1);
  }
  else {
    FUN_ffff00009077de54
              (s__s__d__Set_output_dev___s_failed_ffff0000907f8c4e,
               s_mrdump_set_output_device_ffff0000907f0496,0x43,param_1);
  }
  return uVar2;
}


