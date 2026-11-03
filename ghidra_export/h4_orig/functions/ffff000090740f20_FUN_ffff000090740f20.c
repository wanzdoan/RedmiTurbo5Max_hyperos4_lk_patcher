// ffff000090740f20  size=364  FUN_ffff000090740f20

undefined8 FUN_ffff000090740f20(undefined8 param_1,int param_2,int param_3)

{
  byte bVar1;
  int iVar2;
  undefined4 uVar3;
  undefined1 local_30 [4];
  undefined1 local_2c [4];
  byte local_28 [4];
  undefined1 local_24 [4];
  
  if ((param_2 == 2) || ((param_2 == 1 && (param_3 == 1)))) {
    local_30[0] = 1;
    local_2c[0] = 0xe9;
    FUN_ffff0000907404b4(param_1,0x3b1,local_2c,1);
    local_2c[0] = 0xe6;
    FUN_ffff0000907404b4(param_1,0x3b2,local_2c,1);
    FUN_ffff0000907404b4(param_1,0x13a,local_30,1);
    FUN_ffff00009074056c(param_1,0x217,0,1,1);
    FUN_ffff00009074108c(param_1,1,0);
    local_28[0] = 0;
    iVar2 = FUN_ffff00009074046c(param_1,0xa2c,local_28,1);
    bVar1 = local_28[0];
    if (-1 < iVar2) {
      local_28[0] = local_28[0] >> 5;
      local_24[0] = 0xdc;
      FUN_ffff0000907404b4(param_1,0x3aa,local_24,1);
      local_24[0] = 0xf1;
      FUN_ffff0000907404b4(param_1,0x3ab,local_24,1);
      uVar3 = 5;
      if ((bVar1 & 0x60) != 0) {
        uVar3 = 0;
      }
      FUN_ffff00009074056c(param_1,0x994,uVar3,0xf,4);
      FUN_ffff00009074108c(param_1,2,0);
    }
    FUN_ffff00009074056c(param_1,0x143,local_30[0],1,0);
  }
  return 0;
}


