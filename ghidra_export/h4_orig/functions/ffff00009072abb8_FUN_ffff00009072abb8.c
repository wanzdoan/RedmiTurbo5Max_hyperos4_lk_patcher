// ffff00009072abb8  size=12  FUN_ffff00009072abb8

void FUN_ffff00009072abb8(long param_1)

{
  if (param_1 != 0) {
    FUN_ffff00009077eb20(0xffff00009086d580,param_1,0x90);
    FUN_ffff00009077eb20(0xffff00009086d670,param_1 + 0x90,8);
    FUN_ffff00009077eb20(0xffff00009086d680,param_1 + 0xa0,8);
    FUN_ffff00009077eb20(0xffff00009086d688,param_1 + 0xa8,8);
    FUN_ffff00009077eb20(&DAT_ffff00009086d678,param_1 + 0x98,8);
    DAT_ffff00009086d57d = 1;
    FUN_ffff00009077e11c(s_iframe_short_stored__ffff0000907d0dfd);
    return;
  }
  return;
}


