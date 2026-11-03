// ffff0000907835c4  size=340  FUN_ffff0000907835c4

undefined8 FUN_ffff0000907835c4(void)

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
  iVar1 = FUN_ffff000090782eb8(s_mrdump_output_ffff0000907dec91,&local_50,0x40);
  if (iVar1 != 0) {
    FUN_ffff00009077e11c
              (s__s__d___s__mrdump_get_env_fail_ffff0000907d01fe,
               s_mrdump_get_default_output_device_ffff000090801729,0x4f,
               s_mrdump_get_default_output_device_ffff000090801729);
  }
  iVar1 = FUN_ffff00009077f0ac(&local_50,&DAT_ffff0000907df338);
  if (iVar1 == 0) {
    uVar2 = FUN_ffff000090783480(s_internal_storage_ffff0000907e8490);
  }
  else {
    iVar1 = FUN_ffff00009077f0ac(&local_50,&DAT_ffff0000907e9fe1);
    if (iVar1 == 0) {
      uVar2 = 1;
    }
    else {
      iVar1 = FUN_ffff00009077f0ac(&local_50,&DAT_ffff0000907e3a2f);
      if (iVar1 == 0) {
        uVar2 = 2;
      }
      else {
        iVar1 = FUN_ffff00009077f0ac(&local_50,s_internal_storage_ffff0000907e8490);
        if (iVar1 == 0) {
          uVar2 = 3;
        }
        else {
          iVar1 = FUN_ffff00009077f0ac(&local_50,&DAT_ffff0000907f3ef5);
          if (iVar1 == 0) {
            uVar2 = 5;
          }
          else {
            iVar1 = FUN_ffff00009077f0ac(&local_50,&DAT_ffff0000907ebeb2);
            if (iVar1 == 0) {
              uVar2 = 7;
            }
            else {
              iVar1 = FUN_ffff00009077f0ac(&local_50,s_partition_ffff0000907f3747);
              if (iVar1 == 0) {
                uVar2 = 6;
              }
              else {
                FUN_ffff00009077e11c
                          (s__s__d___s__unknown_output_device_ffff0000907e5500,
                           s_mrdump_get_default_output_device_ffff000090801729,0x5f,
                           s_mrdump_get_default_output_device_ffff000090801729,&local_50);
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


