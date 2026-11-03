// ffff0000907832fc  size=340  FUN_ffff0000907832fc

undefined8 FUN_ffff0000907832fc(void)

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
  iVar1 = FUN_ffff000090782bf0(s_mrdump_output_ffff0000907debd7,&local_50,0x40);
  if (iVar1 != 0) {
    FUN_ffff00009077de54
              (s__s__d___s__mrdump_get_env_fail_ffff0000907d01df,
               s_mrdump_get_default_output_device_ffff00009080154d,0x4f,
               s_mrdump_get_default_output_device_ffff00009080154d);
  }
  iVar1 = FUN_ffff00009077ede4(&local_50,&DAT_ffff0000907df27e);
  if (iVar1 == 0) {
    uVar2 = FUN_ffff0000907831b8(s_internal_storage_ffff0000907e8335);
  }
  else {
    iVar1 = FUN_ffff00009077ede4(&local_50,&DAT_ffff0000907e9e86);
    if (iVar1 == 0) {
      uVar2 = 1;
    }
    else {
      iVar1 = FUN_ffff00009077ede4(&local_50,&DAT_ffff0000907e3947);
      if (iVar1 == 0) {
        uVar2 = 2;
      }
      else {
        iVar1 = FUN_ffff00009077ede4(&local_50,s_internal_storage_ffff0000907e8335);
        if (iVar1 == 0) {
          uVar2 = 3;
        }
        else {
          iVar1 = FUN_ffff00009077ede4(&local_50,&DAT_ffff0000907f3d52);
          if (iVar1 == 0) {
            uVar2 = 5;
          }
          else {
            iVar1 = FUN_ffff00009077ede4(&local_50,&DAT_ffff0000907ebd18);
            if (iVar1 == 0) {
              uVar2 = 7;
            }
            else {
              iVar1 = FUN_ffff00009077ede4(&local_50,s_partition_ffff0000907f35a4);
              if (iVar1 == 0) {
                uVar2 = 6;
              }
              else {
                FUN_ffff00009077de54
                          (s__s__d___s__unknown_output_device_ffff0000907e53e5,
                           s_mrdump_get_default_output_device_ffff00009080154d,0x5f,
                           s_mrdump_get_default_output_device_ffff00009080154d,&local_50);
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


