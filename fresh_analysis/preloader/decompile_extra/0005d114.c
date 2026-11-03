// File offset entry: 0x5d114

ulong FUN_0205d014(undefined8 param_1)

{
  long *plVar1;
  int iVar2;
  uint uVar3;
  undefined8 uVar4;
  ulong uVar5;
  long lVar6;
  ulong auStack_38 [3];
  
  auStack_38[2] = *plRam0000000002103280;
  auStack_38[0] = 0;
  auStack_38[1] = 0;
  iVar2 = func_0x0205cf7c(param_1,auStack_38);
  uVar5 = auStack_38[0];
  if (iVar2 != 0) {
    FUN_0208ada0(0x20b07a9,param_1);
    uVar5 = 0;
  }
  if (lRam0000000002100880 == auStack_38[2]) {
    return uVar5;
  }
  uVar4 = FUN_02091090(uVar5);
  uVar5 = FUN_02091294(3);
  plVar1 = plRam0000000002103338;
  uRam000000000211bce8 = uVar4;
  iVar2 = func_0x0205d16c(uVar5,1,*plRam0000000002103338 + 0x614a0,*plRam0000000002103338 + 0x624a0)
  ;
  if (iVar2 != 0) {
    lVar6 = FUN_0205cef8(uVar5 & 0xffffffff);
    if (lVar6 == 0) {
      uVar5 = 1;
      uVar4 = 0x20bb9d4;
      goto LAB_0205d148;
    }
    uVar3 = func_0x0205d16c(uVar5 & 0xffffffff,lVar6,*plVar1 + 0x614a0,*plVar1 + 0x624a0);
    if (uVar3 != 0) {
      uVar5 = (ulong)uVar3;
      uVar4 = 0x20bfc67;
      goto LAB_0205d148;
    }
  }
  uVar5 = 0;
  uVar4 = 0x20ca032;
LAB_0205d148:
  FUN_0208ada0(uVar4,0x20ca029);
  return uVar5;
}


