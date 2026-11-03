
undefined8 FUN_ffff0000be0651f8(void)

{
  int iVar1;
  undefined8 uVar2;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  undefined8 local_20;
  undefined8 uStack_18;
  
  local_20 = 0;
  uStack_18 = 0;
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  iVar1 = FUN_ffff0000be064ad8(s_mrdump_output_ffff0000be0ade98,&local_50,0x40);
  if (iVar1 != 0) {
    FUN_ffff0000be05fd98
              (s__s__d___s__mrdump_get_env_fail_ffff0000be0a30af,
               s_mrdump_get_default_output_device_ffff0000be0c5958,0x4f,
               s_mrdump_get_default_output_device_ffff0000be0c5958);
  }
  iVar1 = FUN_ffff0000be060cb8(&local_50,&DAT_ffff0000be0ae340);
  if (iVar1 == 0) {
    uVar2 = FUN_ffff0000be0650b4(s_internal_storage_ffff0000be0b48f2);
  }
  else {
    iVar1 = FUN_ffff0000be060cb8(&local_50,&DAT_ffff0000be0b5b6c);
    if (iVar1 == 0) {
      uVar2 = 1;
    }
    else {
      iVar1 = FUN_ffff0000be060cb8(&local_50,&DAT_ffff0000be0b14dc);
      if (iVar1 == 0) {
        uVar2 = 2;
      }
      else {
        iVar1 = FUN_ffff0000be060cb8(&local_50,s_internal_storage_ffff0000be0b48f2);
        if (iVar1 == 0) {
          uVar2 = 3;
        }
        else {
          iVar1 = FUN_ffff0000be060cb8(&local_50,&DAT_ffff0000be0bce82);
          if (iVar1 == 0) {
            uVar2 = 5;
          }
          else {
            iVar1 = FUN_ffff0000be060cb8(&local_50,&DAT_ffff0000be0b74cf);
            if (iVar1 == 0) {
              uVar2 = 7;
            }
            else {
              iVar1 = FUN_ffff0000be060cb8(&local_50,s_partition_ffff0000be0bc6bf);
              if (iVar1 == 0) {
                uVar2 = 6;
              }
              else {
                FUN_ffff0000be05fd98
                          (s__s__d___s__unknown_output_device_ffff0000be0b2641,
                           s_mrdump_get_default_output_device_ffff0000be0c5958,0x5f,
                           s_mrdump_get_default_output_device_ffff0000be0c5958,&local_50);
                uVar2 = 0;
              }
            }
          }
        }
      }
    }
  }
  return uVar2;
}


