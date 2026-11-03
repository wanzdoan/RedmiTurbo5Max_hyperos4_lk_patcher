
void FUN_ffff0000b801c4dc(long param_1)

{
  if (param_1 != 0) {
    FUN_ffff0000b804cd40(&DAT_ffff0000b811b3a0,param_1,0x110);
    DAT_ffff0000b811b498 = param_1 + 0x110;
    DAT_ffff0000b811b398 = 1;
    FUN_ffff0000b804c33c(s_iframe_stored__ffff0000b80870e8);
    return;
  }
  return;
}


