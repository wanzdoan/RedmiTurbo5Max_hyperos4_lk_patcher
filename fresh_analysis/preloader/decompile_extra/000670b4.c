// File offset entry: 0x670b4

undefined4 FUN_02066fb4(long param_1,long param_2)

{
  long lVar1;
  int iVar2;
  undefined8 uVar3;
  undefined8 extraout_x8;
  undefined8 extraout_x8_00;
  undefined1 auVar4 [16];
  
  if ((lRam000000000211be38 == 0) || (lRam000000000211be40 == 0)) {
    uRam000000000211be35 = 1;
    FUN_0208ada0(0x20c3ff0,0x20b58bc);
  }
  else {
    if (param_1 == 0) {
      return 0;
    }
    if (param_2 == 0) {
      return 0;
    }
    if (0x400000 - *(uint *)(lRam000000000211be38 + 4) < *(uint *)(param_1 + 8)) {
      return 0;
    }
    uRam000000000211be35 = 1;
    if (uRam000000000211be48 <
        (uRam000000000211be48 - *(uint *)(lRam000000000211be38 + 4)) +
        (ulong)*(uint *)(param_2 + 0xc) * 0xc + 0x34) {
      auVar4 = FUN_020685c0(0x20cf8ea);
      FUN_0208ada0(auVar4._0_8_,auVar4._8_8_,extraout_x8);
    }
    else {
      uVar3 = FUN_02068620();
      iVar2 = FUN_0207e954(uVar3,extraout_x8_00);
      lVar1 = lRam000000000211be38;
      if (iVar2 == 0xc) {
        *(uint *)(param_2 + 0xc) = *(int *)(param_2 + 0xc) + 1U & 0x1f;
        uRam000000000211be35 = 1;
        iVar2 = FUN_0207e954(0x20babff,uRam000000000211be48 - *(uint *)(lVar1 + 4),param_2,0x34);
        if (iVar2 == 0x34) {
          uRam000000000211be35 = 0;
          return 1;
        }
        uVar3 = 0x20bfdbe;
      }
      else {
        uVar3 = 0x20c81b9;
      }
      uRam000000000211be35 = 1;
      auVar4 = FUN_020685c0(uVar3);
      FUN_0208ada0(auVar4._0_8_,auVar4._8_8_,0x20cd3bc);
    }
  }
  uRam000000000211be35 = 0;
  return 0;
}


