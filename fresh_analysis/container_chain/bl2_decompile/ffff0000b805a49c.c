
ulong FUN_ffff0000b805a49c(long param_1,uint param_2)

{
  ulong uVar1;
  ulong uVar2;
  
  if ((int)param_2 < 1) {
    uVar1 = 0;
  }
  else {
    uVar2 = 0;
    uVar1 = (ulong)param_2;
    do {
      if (*(byte *)(param_1 + uVar2) == 0) {
        return uVar2 & 0xffffffff;
      }
      if (*(byte *)(param_1 + uVar2) - 0x7f < 0xffffffa1) {
        *(undefined1 *)(param_1 + uVar2) = 0x20;
      }
      uVar2 = uVar2 + 1;
    } while (param_2 != (uint)uVar2);
  }
  return uVar1;
}


