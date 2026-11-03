
bool FUN_ffff0000be0101cc(void)

{
  ulong uVar1;
  
  if (DAT_ffff0000be0fd0c0 == 0xb7) {
    uVar1 = FUN_ffff0000be010288();
    if (uVar1 == 0) {
      return false;
    }
  }
  else {
    if (DAT_ffff0000be0fd0c0 != 0x28) {
      FUN_ffff0000be063b40(s_mrdump__unknown_machine__ffff0000be0c3bef);
      FUN_ffff0000be05fd98(s_mrdump__unknown_machine__ffff0000be0c3bef);
      return false;
    }
    uVar1 = FUN_ffff0000be010524();
    if (uVar1 == 0) {
      return false;
    }
  }
  if ((DAT_ffff0000be0fd038 != 0) && (DAT_ffff0000be0fd040 != 0)) {
    return (uVar1 >= DAT_ffff0000be0fd038 && uVar1 <= DAT_ffff0000be0fd040) &&
           (uVar1 < DAT_ffff0000be0fd038 || DAT_ffff0000be0fd040 != uVar1);
  }
  FUN_ffff0000be063b40(s_kedump_invalid_dram_range_0x_llx_ffff0000be0ae3f5);
  FUN_ffff0000be05fd98
            (s_kedump_invalid_dram_range_0x_llx_ffff0000be0ae3f5,DAT_ffff0000be0fd038,
             DAT_ffff0000be0fd040);
  return false;
}


