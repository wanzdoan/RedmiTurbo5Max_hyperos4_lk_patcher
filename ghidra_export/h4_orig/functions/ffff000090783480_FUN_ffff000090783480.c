// ffff000090783480  size=324  FUN_ffff000090783480

undefined4 FUN_ffff000090783480(undefined8 param_1)

{
  int iVar1;
  undefined4 uVar2;
  
  iVar1 = FUN_ffff00009077f0ac(param_1,&DAT_ffff0000907e9fe1);
  if (iVar1 == 0) {
    uVar2 = 1;
  }
  else {
    iVar1 = FUN_ffff00009077f0ac(param_1,&DAT_ffff0000907e3a2f);
    if (iVar1 == 0) {
      uVar2 = 2;
    }
    else {
      iVar1 = FUN_ffff00009077f0ac(param_1,&DAT_ffff0000907f3ef5);
      if (iVar1 == 0) {
        uVar2 = 5;
      }
      else {
        iVar1 = FUN_ffff00009077f0ac(param_1,&DAT_ffff0000907ebeb2);
        if (iVar1 == 0) {
          uVar2 = 7;
        }
        else {
          iVar1 = FUN_ffff00009077f0ac(param_1,s_partition_ffff0000907f3747);
          if (iVar1 == 0) {
            uVar2 = 6;
          }
          else {
            iVar1 = FUN_ffff00009077f0ac(param_1,s_internal_storage_ffff0000907e8490);
            if (iVar1 != 0) {
              FUN_ffff00009077e11c
                        (s__s__d___s__unknown_output_device_ffff0000907e5500,
                         s_mrdump_set_output_device_ffff0000907f0607,0x45,
                         s_mrdump_set_output_device_ffff0000907f0607,param_1);
              return 0;
            }
            uVar2 = 3;
          }
        }
      }
    }
  }
  iVar1 = FUN_ffff000090783154(s_mrdump_output_ffff0000907dec91,param_1);
  if (iVar1 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___s__defaults___s_ffff000090801710,
               s_mrdump_set_output_device_ffff0000907f0607,0x41,
               s_mrdump_set_output_device_ffff0000907f0607,param_1);
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d__Set_output_dev___s_failed_ffff0000907f8dea,
               s_mrdump_set_output_device_ffff0000907f0607,0x43,param_1);
  }
  return uVar2;
}


