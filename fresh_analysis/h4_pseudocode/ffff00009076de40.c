
long FUN_ffff00009076de40(long param_1)

{
  uint uVar1;
  long lVar2;
  
  lVar2 = FUN_ffff00009076dc10();
  if (lVar2 != 0) {
    uVar1 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
            (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
    if (((uVar1 >> 0x10 | uVar1 << 0x10) < 0x10) &&
       (uVar1 = (*(uint *)(lVar2 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lVar2 + 4) & 0xff00ff) << 8,
       7 < (uVar1 >> 0x10 | uVar1 << 0x10))) {
      lVar2 = lVar2 + 0x10;
    }
    else {
      lVar2 = lVar2 + 0xc;
    }
  }
  return lVar2;
}


