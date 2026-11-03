
undefined4 FUN_ffff0000be0650b4(undefined8 param_1)

{
  int iVar1;
  undefined4 uVar2;
  
  iVar1 = FUN_ffff0000be060cb8(param_1,&DAT_ffff0000be0b5b6c);
  if (iVar1 == 0) {
    uVar2 = 1;
  }
  else {
    iVar1 = FUN_ffff0000be060cb8(param_1,&DAT_ffff0000be0b14dc);
    if (iVar1 == 0) {
      uVar2 = 2;
    }
    else {
      iVar1 = FUN_ffff0000be060cb8(param_1,&DAT_ffff0000be0bce82);
      if (iVar1 == 0) {
        uVar2 = 5;
      }
      else {
        iVar1 = FUN_ffff0000be060cb8(param_1,&DAT_ffff0000be0b74cf);
        if (iVar1 == 0) {
          uVar2 = 7;
        }
        else {
          iVar1 = FUN_ffff0000be060cb8(param_1,s_partition_ffff0000be0bc6bf);
          if (iVar1 == 0) {
            uVar2 = 6;
          }
          else {
            iVar1 = FUN_ffff0000be060cb8(param_1,s_internal_storage_ffff0000be0b48f2);
            if (iVar1 != 0) {
              FUN_ffff0000be05fd98
                        (s__s__d___s__unknown_output_device_ffff0000be0b2641,
                         s_mrdump_set_output_device_ffff0000be0ba4f7,0x45,
                         s_mrdump_set_output_device_ffff0000be0ba4f7,param_1);
              return 0;
            }
            uVar2 = 3;
          }
        }
      }
    }
  }
  iVar1 = FUN_ffff0000be064d74(s_mrdump_output_ffff0000be0ade98,param_1);
  if (iVar1 == 0) {
    FUN_ffff0000be05fd98
              (s__s__d___s__defaults___s_ffff0000be0c593f,
               s_mrdump_set_output_device_ffff0000be0ba4f7,0x41,
               s_mrdump_set_output_device_ffff0000be0ba4f7,param_1);
  }
  else {
    FUN_ffff0000be05fd98
              (s__s__d__Set_output_dev___s_failed_ffff0000be0bffda,
               s_mrdump_set_output_device_ffff0000be0ba4f7,0x43,param_1);
  }
  return uVar2;
}


