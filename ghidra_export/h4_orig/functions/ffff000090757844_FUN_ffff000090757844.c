// ffff000090757844  size=72  FUN_ffff000090757844

undefined8 FUN_ffff000090757844(int *param_1,int param_2,long param_3)

{
  long lVar1;
  int iVar2;
  uint uVar3;
  
  iVar2 = *param_1;
  uVar3 = *(uint *)(param_3 + 0x92c);
  lVar1 = (ulong)((param_2 - 0x5bU & 0xfffffffd) == 0) * 0x10000;
  *(undefined4 *)(lVar1 + -0xffffcdf30000) = 1;
  *(uint *)(lVar1 + -0xffffcdf2ffd0) = uVar3 | iVar2 << 0x10;
  *(undefined4 *)(lVar1 + -0xffffcdf2ffe0) = 1;
  return 0;
}


