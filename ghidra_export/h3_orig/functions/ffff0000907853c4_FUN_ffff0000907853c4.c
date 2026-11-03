// ffff0000907853c4  size=96  FUN_ffff0000907853c4

undefined8 FUN_ffff0000907853c4(uint param_1,int param_2)

{
  int iVar1;
  undefined8 uVar2;
  ulong uVar3;
  
  uVar2 = 0xfffffffe;
  if ((param_1 < 0x10f) && (param_2 < 2)) {
    iVar1 = 1 << (ulong)(param_1 & 0x1f);
    uVar3 = (ulong)(param_1 >> 1) & 0x7ffffff0;
    if (param_2 == 0) {
      *(int *)(uVar3 - 0xffffeffd2ef8) = iVar1;
      return 0;
    }
    uVar2 = 0;
    *(int *)(uVar3 - 0xffffeffd2efc) = iVar1;
  }
  return uVar2;
}


