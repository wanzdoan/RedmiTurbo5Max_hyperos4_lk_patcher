// ffff00009078561c  size=112  FUN_ffff00009078561c

undefined4 FUN_ffff00009078561c(int param_1,int param_2)

{
  int iVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  ulong uVar4;
  
  uVar2 = 0xfffffffe;
  if ((param_1 < 0x10f) && (param_2 < 2)) {
    iVar1 = param_1 + 0x1f;
    if (-1 < param_1) {
      iVar1 = param_1;
    }
    uVar4 = (ulong)(uint)((iVar1 >> 5) << 4);
    uVar3 = (undefined4)(1L << ((ulong)(uint)(param_1 % 0x20) & 0x3f));
    if (param_2 == 0) {
      uVar2 = 0;
      *(undefined4 *)(uVar4 - 0xffffeffd2ff8) = uVar3;
    }
    else {
      uVar2 = 0;
      *(undefined4 *)(uVar4 - 0xffffeffd2ffc) = uVar3;
    }
  }
  return uVar2;
}


