// ffff00009075da38  size=728  FUN_ffff00009075da38

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009075da38(void)

{
  undefined4 uVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  undefined4 uVar4;
  int iVar5;
  
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  uVar1 = FUN_ffff00009075c168(_DAT_ffff00009087bf70);
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  uVar2 = FUN_ffff00009075c198(_DAT_ffff00009087bf70);
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  uVar3 = FUN_ffff00009075c0e8(_DAT_ffff00009087bf70);
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  uVar4 = FUN_ffff00009075c128(_DAT_ffff00009087bf70);
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  if (_DAT_ffff00009087bf68 == 0) {
    iVar5 = FUN_ffff00009075650c(_DAT_ffff00009087bf88);
    if (iVar5 != 0) {
      if ((DAT_ffff00009087a99c & 1) == 0) {
        FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
        DAT_ffff00009087a99c = 1;
      }
      iVar5 = FUN_ffff00009075c250(_DAT_ffff00009087bf70);
      if (iVar5 != 0) {
        return 0;
      }
      FUN_ffff00009074c368(0x2b);
    }
    if ((DAT_ffff00009087a99c & 1) == 0) {
      FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
      DAT_ffff00009087a99c = 1;
    }
    FUN_ffff00009075c1c8(_DAT_ffff00009087bf70,uVar1,uVar2,uVar3,uVar4,0);
    if (_DAT_ffff00009087bf48 == 0) {
      if ((DAT_ffff00009087a99c & 1) == 0) {
        FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
        DAT_ffff00009087a99c = 1;
      }
      iVar5 = FUN_ffff00009075c250(_DAT_ffff00009087bf70);
      if (iVar5 == 0) {
        if ((DAT_ffff00009087a99c & 1) == 0) {
          FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
          DAT_ffff00009087a99c = 1;
        }
        if (*(int *)(_DAT_ffff00009087bf70 + 0x1c) != 0) {
          FUN_ffff00009074c494(0x2b,0);
        }
      }
    }
    if ((DAT_ffff00009087a99c & 1) == 0) {
      FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
      DAT_ffff00009087a99c = 1;
    }
    FUN_ffff000090755b90(_DAT_ffff00009087bf88,_DAT_ffff00009087bf48);
    if (_DAT_ffff00009087bf48 == 0) {
      if ((DAT_ffff00009087a99c & 1) == 0) {
        FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
        DAT_ffff00009087a99c = 1;
        iVar5 = _DAT_ffff00009087bf48;
      }
      else {
        iVar5 = 0;
      }
      FUN_ffff000090756304(_DAT_ffff00009087bf88,0,iVar5);
    }
  }
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  FUN_ffff0000907569ac(_DAT_ffff00009087bf88);
  return 0;
}


