
undefined8 FUN_ffff0000be037660(int param_1,int param_2)

{
  uint uVar1;
  long lVar2;
  
  if (-1 < param_2) {
    uVar1 = 0;
    if (param_1 != 0x2b) {
      uVar1 = (uint)(param_1 != 0x2d);
    }
    lVar2 = 1;
    if (param_1 != 0x2b) {
      lVar2 = 2;
    }
    lVar2 = lVar2 - (ulong)uVar1;
    do {
      lVar2 = lVar2 + -1;
    } while (lVar2 != 0);
  }
  return 0;
}


