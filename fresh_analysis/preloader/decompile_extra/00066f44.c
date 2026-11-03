// File offset entry: 0x66f44

undefined4 FUN_02066e44(int *param_1)

{
  int iVar1;
  undefined8 uVar2;
  undefined1 auVar3 [16];
  
  uRam000000000211be35 = 1;
  if ((lRam000000000211be38 == 0) || (lRam000000000211be40 == 0)) {
    FUN_0208ada0(0x20cc61a,0x20b58bc);
  }
  else {
    iVar1 = FUN_0207e78c(0x20babff,lRam000000000211be48 - (ulong)*(uint *)(lRam000000000211be38 + 4)
                         ,param_1,0x34);
    uRam000000000211be35 = 0;
    if (iVar1 == 0x34) {
      if (*param_1 == 0x785690ef) {
        uRam000000000211be35 = 0;
        return 1;
      }
      FUN_020685c0(lRam000000000211be38,0x20d0c94);
      uRam000000000211be35 = 1;
      FUN_0208ada0();
      uRam000000000211be35 = 0;
      FUN_02091664(param_1,0,0x34);
      *param_1 = 0x785690ef;
      uVar2 = func_0x02068620();
      uRam000000000211be35 = 1;
      iVar1 = FUN_0207e954(uVar2,lRam000000000211be48 - (ulong)*(uint *)(lRam000000000211be38 + 4),
                           param_1,0x34);
      uRam000000000211be35 = 0;
      if (iVar1 == 0x34) {
        uRam000000000211be35 = 0;
        return 1;
      }
      auVar3 = FUN_020685c0(1,0x20b782c);
      FUN_0208ada0(auVar3._0_8_,auVar3._8_8_,0x20b0831);
    }
    else {
      uRam000000000211be35 = 1;
      FUN_0208ada0(0x20c4e3e,0x20b58bc);
    }
  }
  uRam000000000211be35 = 0;
  return 0;
}


