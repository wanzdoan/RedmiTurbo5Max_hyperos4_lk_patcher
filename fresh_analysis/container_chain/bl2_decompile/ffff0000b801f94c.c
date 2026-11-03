
undefined8 FUN_ffff0000b801f94c(undefined8 param_1)

{
  int iVar1;
  uint uVar2;
  long lVar3;
  ulong uVar4;
  long lVar5;
  long *plVar6;
  long local_640 [192];
  
  plVar6 = local_640;
  FUN_ffff0000b804d070(local_640,0,0x600);
  iVar1 = FUN_ffff0000b801fb04();
  if (-1 < iVar1) {
    lVar3 = FUN_ffff0000b801fa14();
    iVar1 = FUN_ffff0000b801fa28();
    if (0 < iVar1) {
      lVar5 = 0;
      do {
        local_640[lVar5] = lVar3;
        lVar5 = lVar5 + 1;
        iVar1 = FUN_ffff0000b801fa28();
        lVar3 = lVar3 + 0x6d;
      } while (lVar5 < iVar1);
    }
    uVar2 = FUN_ffff0000b801fa28();
    if (0 < (int)uVar2) {
      uVar4 = (ulong)uVar2;
      do {
        iVar1 = FUN_ffff0000b804d4c4(param_1,*plVar6,0x48);
        if (iVar1 == 0) {
          return 1;
        }
        plVar6 = plVar6 + 1;
        uVar4 = uVar4 - 1;
      } while (uVar4 != 0);
    }
  }
  return 0;
}


