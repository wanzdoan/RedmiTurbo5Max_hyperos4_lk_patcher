// File offset entry: 0x5d07c

undefined8 FUN_0205cf7c(undefined8 param_1,long *param_2)

{
  long lVar1;
  long *plVar2;
  undefined8 uVar3;
  undefined1 auVar4 [16];
  
  lVar1 = FUN_020036c8(1);
  if (lVar1 == 0) {
    auVar4 = FUN_0205d900(0x20b587e);
    FUN_0208ada0(auVar4._0_8_,auVar4._8_8_,0x20bfc85);
    uVar3 = 1;
  }
  else {
    plVar2 = (long *)FUN_0205cb14(param_1);
    uVar3 = 1;
    if ((param_2 != (long *)0x0) && (plVar2 != (long *)0x0)) {
      uVar3 = 0;
      *param_2 = *plVar2 * (ulong)*(uint *)(lVar1 + 4);
      *(byte *)(param_2 + 1) = *(byte *)(plVar2 + 2) & 4;
    }
  }
  return uVar3;
}


