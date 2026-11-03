
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be0304f8(void)

{
  long lVar1;
  long lVar2;
  uint uVar3;
  
  DAT_ffff0000be119600 = _DAT_ffff00001c34004c;
  lVar2 = 0;
  uVar3 = _DAT_ffff00001c34004c;
  do {
    uVar3 = uVar3 - 1 & 0xf;
    lVar1 = lVar2 + 4;
    _DAT_ffff00001c34015c = uVar3;
    *(undefined4 *)((long)&DAT_ffff0000be119604 + lVar2) = _DAT_ffff00001c340154;
    *(undefined4 *)((long)&DAT_ffff0000be119644 + lVar2) = _DAT_ffff00001c340158;
    lVar2 = lVar1;
  } while (lVar1 != 0x40);
  return;
}


