// File offset entry: 0x5d748

int FUN_0205d648(undefined8 param_1,undefined4 param_2,long param_3,undefined4 param_4)

{
  int iVar1;
  long lVar2;
  
  lVar2 = FUN_020036c8(1);
  if (lVar2 == 0) {
    FUN_0208ada0(0x20c2bf1,0x20ca029);
    iVar1 = 1;
  }
  else {
    iVar1 = FUN_02003ac0(lVar2,(ulong)*(uint *)(lVar2 + 4) * param_3,param_4,param_1,param_2);
    if (iVar1 != 0) {
      FUN_0208ada0(0x20be8f4,0x20ca029,iVar1);
    }
  }
  return iVar1;
}


