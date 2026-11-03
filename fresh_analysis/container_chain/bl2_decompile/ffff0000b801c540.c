
void FUN_ffff0000b801c540(long param_1)

{
  if (param_1 != 0) {
    FUN_ffff0000b804cd40(&DAT_ffff0000b811b3a0,param_1,0x90);
    FUN_ffff0000b804cd40(&DAT_ffff0000b811b490,param_1 + 0x90,8);
    FUN_ffff0000b804cd40(&DAT_ffff0000b811b4a0,param_1 + 0xa0,8);
    FUN_ffff0000b804cd40(&DAT_ffff0000b811b4a8,param_1 + 0xa8,8);
    FUN_ffff0000b804cd40(&DAT_ffff0000b811b498,param_1 + 0x98,8);
    DAT_ffff0000b811b398 = 1;
    FUN_ffff0000b804c33c(s_iframe_short_stored__ffff0000b8086253);
    return;
  }
  return;
}


