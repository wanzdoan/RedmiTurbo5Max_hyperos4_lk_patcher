// ffff00009077d808  size=68  FUN_ffff00009077d808

bool FUN_ffff00009077d808(uint param_1)

{
  if ((param_1 < 0x21) && ((1L << ((ulong)param_1 & 0x3f) & 0x100003600U) != 0)) {
    return true;
  }
  return param_1 == 0xb;
}


