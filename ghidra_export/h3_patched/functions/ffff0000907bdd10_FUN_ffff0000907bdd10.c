// ffff0000907bdd10  size=204  FUN_ffff0000907bdd10

void FUN_ffff0000907bdd10(long param_1,long param_2,ulong param_3)

{
  ulong uVar1;
  ulong uVar2;
  
  uVar1 = 0x80 - *(long *)(param_1 + 0x48);
  if (param_3 <= uVar1) {
    uVar1 = param_3;
  }
  thunk_FUN_ffff00009077e858(param_1 + *(long *)(param_1 + 0x48) + 0x50,param_2,uVar1);
  uVar2 = *(long *)(param_1 + 0x48) + param_3;
  if (0x7f < uVar2) {
    param_3 = param_3 - uVar1;
    FUN_ffff0000907bdddc(param_1,param_1 + 0x50,1);
    FUN_ffff0000907bdddc(param_1,param_2 + uVar1,param_3 >> 7);
    uVar2 = param_3 & 0x7f;
    thunk_FUN_ffff00009077e858
              (param_1 + 0x50,param_2 + uVar1 + (param_3 & 0xffffffffffffff80),uVar2);
    *(ulong *)(param_1 + 0x40) = (param_3 & 0xffffffffffffff80) + *(long *)(param_1 + 0x40) + 0x80;
  }
  *(ulong *)(param_1 + 0x48) = uVar2;
  return;
}


