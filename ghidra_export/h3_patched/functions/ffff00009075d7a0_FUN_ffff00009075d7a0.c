// ffff00009075d7a0  size=728  FUN_ffff00009075d7a0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009075d7a0(void)

{
  undefined4 uVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  undefined4 uVar4;
  int iVar5;
  
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  uVar1 = FUN_ffff00009075bed0(_DAT_ffff00009087bed8);
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  uVar2 = FUN_ffff00009075bf00(_DAT_ffff00009087bed8);
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  uVar3 = FUN_ffff00009075be50(_DAT_ffff00009087bed8);
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  uVar4 = FUN_ffff00009075be90(_DAT_ffff00009087bed8);
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  if (_DAT_ffff00009087bed0 == 0) {
    iVar5 = FUN_ffff000090756274(_DAT_ffff00009087bef0);
    if (iVar5 != 0) {
      if ((DAT_ffff00009087a904 & 1) == 0) {
        FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
        DAT_ffff00009087a904 = 1;
      }
      iVar5 = FUN_ffff00009075bfb8(_DAT_ffff00009087bed8);
      if (iVar5 != 0) {
        return 0;
      }
      FUN_ffff00009074c0d0(0x2b);
    }
    if ((DAT_ffff00009087a904 & 1) == 0) {
      FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
      DAT_ffff00009087a904 = 1;
    }
    FUN_ffff00009075bf30(_DAT_ffff00009087bed8,uVar1,uVar2,uVar3,uVar4,0);
    if (_DAT_ffff00009087beb0 == 0) {
      if ((DAT_ffff00009087a904 & 1) == 0) {
        FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
        DAT_ffff00009087a904 = 1;
      }
      iVar5 = FUN_ffff00009075bfb8(_DAT_ffff00009087bed8);
      if (iVar5 == 0) {
        if ((DAT_ffff00009087a904 & 1) == 0) {
          FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
          DAT_ffff00009087a904 = 1;
        }
        if (*(int *)(_DAT_ffff00009087bed8 + 0x1c) != 0) {
          FUN_ffff00009074c1fc(0x2b,0);
        }
      }
    }
    if ((DAT_ffff00009087a904 & 1) == 0) {
      FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
      DAT_ffff00009087a904 = 1;
    }
    FUN_ffff0000907558f8(_DAT_ffff00009087bef0,_DAT_ffff00009087beb0);
    if (_DAT_ffff00009087beb0 == 0) {
      if ((DAT_ffff00009087a904 & 1) == 0) {
        FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
        DAT_ffff00009087a904 = 1;
        iVar5 = _DAT_ffff00009087beb0;
      }
      else {
        iVar5 = 0;
      }
      FUN_ffff00009075606c(_DAT_ffff00009087bef0,0,iVar5);
    }
  }
  if ((DAT_ffff00009087a904 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087bec8,0,0x38);
    DAT_ffff00009087a904 = 1;
  }
  FUN_ffff000090756714(_DAT_ffff00009087bef0);
  return 0;
}


