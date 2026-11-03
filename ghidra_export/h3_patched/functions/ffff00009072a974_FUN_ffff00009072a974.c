// ffff00009072a974  size=12  FUN_ffff00009072a974

void FUN_ffff00009072a974(long param_1)

{
  if (param_1 != 0) {
    FUN_ffff00009077e858(0xffff00009086d4e8,param_1,0x90);
    FUN_ffff00009077e858(0xffff00009086d5d8,param_1 + 0x90,8);
    FUN_ffff00009077e858(0xffff00009086d5e8,param_1 + 0xa0,8);
    FUN_ffff00009077e858(0xffff00009086d5f0,param_1 + 0xa8,8);
    FUN_ffff00009077e858(&DAT_ffff00009086d5e0,param_1 + 0x98,8);
    DAT_ffff00009086d4e5 = 1;
    FUN_ffff00009077de54(s_iframe_short_stored__ffff0000907d0dc0);
    return;
  }
  return;
}


