// File offset entry: 0x26500

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b8026500(void)

{
  long lVar1;
  long lVar2;
  uint uVar3;
  
  DAT_ffff0000b811ce10 = _DAT_ffff00001c34004c;
  lVar2 = 0;
  uVar3 = _DAT_ffff00001c34004c;
  do {
    uVar3 = uVar3 - 1 & 0xf;
    lVar1 = lVar2 + 4;
    _DAT_ffff00001c34015c = uVar3;
    *(undefined4 *)((long)&DAT_ffff0000b811ce14 + lVar2) = _DAT_ffff00001c340154;
    *(undefined4 *)((long)&DAT_ffff0000b811ce54 + lVar2) = _DAT_ffff00001c340158;
    lVar2 = lVar1;
  } while (lVar1 != 0x40);
  return;
}


