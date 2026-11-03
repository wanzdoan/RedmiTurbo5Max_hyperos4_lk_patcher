// File offset entry: 0xad55c

uint FUN_020ad45c(uint param_1)

{
  uint uVar1;
  
  uVar1 = *puRam0000000002103868;
  if (uVar1 == 0) {
    uVar1 = uRam0000000013260050 & 0xff;
    *puRam0000000002103868 = uVar1;
  }
  if (0x80 < param_1) {
    FUN_0208ada0(0x20b2195,0x20bf320,0x81);
    FUN_0208ada0(0x20c14d3,param_1);
    FUN_0208ada0(0x20c24cb,0x81);
    FUN_0208ada0(0x20b7113,0x20bf320);
    return 0xffffffff;
  }
  if ((int)param_1 < 0x1d) {
    if (param_1 == 7) {
      uVar1 = uRam0000000013260050 & 0xffffff00 | uVar1 & 0xff;
      FUN_0208ada0(0x20cabaa,uVar1);
      return uVar1;
    }
    if (param_1 == 10) {
      return 0x56ab0004;
    }
  }
  else {
    if (param_1 == 0x1d) {
      if (uRam00000000132607a0 >> 0x1e != 0) {
        return uRam00000000132607cc;
      }
      return uRam00000000132607a0;
    }
    if (param_1 == 0x1e) {
      return uVar1 & 0xff;
    }
  }
  return **(uint **)((ulong)param_1 * 8 + 0x20e4508);
}


