// ffff000090784a4c  size=184  FUN_ffff000090784a4c

void FUN_ffff000090784a4c(undefined8 param_1)

{
  int iVar1;
  undefined4 local_34;
  undefined4 local_30;
  undefined4 local_2c;
  undefined4 local_28;
  undefined4 local_24;
  
  local_34 = 0x18;
  local_30 = FUN_ffff000090784a18();
  local_2c = FUN_ffff000090784a30();
  local_28 = FUN_ffff0000907849fc();
  local_24 = FUN_ffff0000907849e0();
  iVar1 = FUN_ffff00009076d75c(param_1,s__chosen_ffff0000907ea5b1);
  if (iVar1 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_devinfo_ffff0000907d1ec3,0x4f,
               s_offset_>__0_ffff0000907f3f8a);
  }
  iVar1 = FUN_ffff00009076e40c(param_1,iVar1,s_atag_chipid_ffff0000907ffc65,&local_34,0x14);
  if (iVar1 == 0) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_common_devinfo_ffff0000907d1ec3,0x52,s_ret____0_ffff0000907eb6f8);
}


