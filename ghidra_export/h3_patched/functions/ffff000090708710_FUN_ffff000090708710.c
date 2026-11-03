// ffff000090708710  size=96  FUN_ffff000090708710

void FUN_ffff000090708710(long param_1)

{
  long lVar1;
  ulong uVar2;
  
  uVar2 = *(long *)(param_1 + 0x270) + *(long *)(param_1 + 0x268) & 0xfffffffffffffff0;
  lVar1 = uVar2 - 0x80;
  FUN_ffff00009077eb88(lVar1,0,0x80);
  *(long *)(param_1 + 0x50) = lVar1;
  *(code **)(uVar2 - 0x80) = FUN_ffff000090708770;
  return;
}


