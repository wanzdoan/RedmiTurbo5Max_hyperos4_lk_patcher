// File offset entry: 0xa7e10

undefined4 FUN_020a7d10(long param_1,uint param_2,uint param_3)

{
  undefined4 uVar1;
  int iVar2;
  ulong uVar3;
  undefined1 *puVar4;
  
  if ((param_2 & 0xffffffef) == 0x20) {
    if ((param_2 == 0x20) && (param_3 < 4)) {
      iVar2 = param_3 << 3;
    }
    else {
      if (param_2 != 0x30) {
        return 0x101020;
      }
      if (2 < param_3) {
        return 0x101020;
      }
      iVar2 = param_3 * 0xc;
    }
    uVar3 = (ulong)(param_2 >> 2);
    iVar2 = iVar2 + 0x38;
    puVar4 = (undefined1 *)(param_1 + 3);
    do {
      uVar1 = FUN_020ad45c(iVar2);
      iVar2 = iVar2 + 1;
      uVar3 = uVar3 - 1;
      puVar4[-3] = (char)uVar1;
      puVar4[-2] = (char)((uint)uVar1 >> 8);
      puVar4[-1] = (char)((uint)uVar1 >> 0x10);
      *puVar4 = (char)((uint)uVar1 >> 0x18);
      puVar4 = puVar4 + 4;
    } while (uVar3 != 0);
    uVar1 = 0;
  }
  else {
    uVar1 = 0x101024;
  }
  return uVar1;
}


