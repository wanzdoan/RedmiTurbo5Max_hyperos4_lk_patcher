// ffff000090784d14  size=184  FUN_ffff000090784d14

void FUN_ffff000090784d14(undefined8 param_1)

{
  int iVar1;
  undefined4 local_34;
  undefined4 local_30;
  undefined4 local_2c;
  undefined4 local_28;
  undefined4 local_24;
  
  local_34 = 0x18;
  local_30 = FUN_ffff000090784ce0();
  local_2c = FUN_ffff000090784cf8();
  local_28 = FUN_ffff000090784cc4();
  local_24 = FUN_ffff000090784ca8();
  iVar1 = FUN_ffff00009076da24(param_1,s__chosen_ffff0000907ea70b);
  if (iVar1 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_devinfo_ffff0000907d1f00,0x4f,
               s_offset_>__0_ffff0000907f4116);
  }
  iVar1 = FUN_ffff00009076e6d4(param_1,iVar1,s_atag_chipid_ffff0000907ffe0d,&local_34,0x14);
  if (iVar1 == 0) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_common_devinfo_ffff0000907d1f00,0x52,s_ret____0_ffff0000907eb87b);
}


