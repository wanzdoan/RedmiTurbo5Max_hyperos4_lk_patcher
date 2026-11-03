
void FUN_ffff0000be068ab0(undefined8 param_1)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000be029e30();
  if (iVar1 == 0) {
    FUN_ffff0000be068814(param_1);
    if (DAT_ffff0000be132614 != '\0') {
      DAT_ffff0000be132618 = 0;
      FUN_ffff0000be029df0(1,DAT_ffff0000be132608);
      FUN_ffff0000be029df0(2,DAT_ffff0000be132628);
      FUN_ffff0000be029df0(3,DAT_ffff0000be13262c);
      FUN_ffff0000be029df0(4,DAT_ffff0000be132630);
      FUN_ffff0000be029df0(5,DAT_ffff0000be132620);
      DAT_ffff0000be132618 = FUN_ffff0000be029dd4(5);
      if ((DAT_ffff0000be132614 != '\0') && (DAT_ffff0000be132618 == 0)) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000be05ee98
                  (s_platform_mediatek_common_scp_scp_ffff0000be0b26ca,0x211,
                   s___scp_configs_secure_dump_____sc_ffff0000be0bb58d);
      }
    }
  }
  return;
}


