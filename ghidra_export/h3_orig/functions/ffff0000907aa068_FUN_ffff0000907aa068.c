// ffff0000907aa068  size=76  FUN_ffff0000907aa068

ulong FUN_ffff0000907aa068(long param_1,uint param_2,uint param_3)

{
  undefined4 uVar1;
  ulong uVar2;
  uint uVar3;
  int iVar4;
  long lVar5;
  
  uVar3 = 0x10101a;
  if ((param_2 & 0xffffffef) == 0x20) {
    if ((param_2 == 0x20) && (param_3 < 4)) {
      uVar2 = FUN_ffff0000907aa0b4(param_3 << 3);
      return uVar2;
    }
    uVar3 = 0x101027;
    if ((param_2 == 0x30) && (param_3 < 3)) {
      lVar5 = 0xc;
      iVar4 = param_3 * 0xc + 0xdc;
      param_1 = param_1 + 3;
      do {
        uVar1 = thunk_FUN_ffff00009074af94(iVar4);
        iVar4 = iVar4 + 1;
        *(undefined4 *)(param_1 + -3) = uVar1;
        param_1 = param_1 + 4;
        lVar5 = lVar5 + -1;
      } while (lVar5 != 0);
      uVar3 = 0;
    }
  }
  return (ulong)uVar3;
}


