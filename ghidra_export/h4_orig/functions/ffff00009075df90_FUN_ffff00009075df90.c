// ffff00009075df90  size=412  FUN_ffff00009075df90

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff00009075df90(void)

{
  int iVar1;
  long lVar2;
  long lVar3;
  uint uVar4;
  
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  if ((_DAT_ffff00009087bf70 != 0) && (*(int *)(_DAT_ffff00009087bf70 + 0x1c) != 0)) {
    *(undefined4 *)(_DAT_ffff00009087bf70 + 0x1c) = 0;
    iVar1 = FUN_ffff000090756b18(_DAT_ffff00009087bf88,7,1000);
    if (0 < iVar1) {
      if ((DAT_ffff00009087a99c & 1) == 0) {
        FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
        DAT_ffff00009087a99c = 1;
      }
      iVar1 = FUN_ffff000090756b18(_DAT_ffff00009087bf88,7,1000);
      if (0 < iVar1) {
        lVar2 = FUN_ffff0000907168b0();
        if ((DAT_ffff00009087a99c & 1) == 0) {
          FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
          DAT_ffff00009087a99c = 1;
        }
        iVar1 = FUN_ffff000090756b18(_DAT_ffff00009087bf88,7,1000);
        if (0 < iVar1) {
          if ((DAT_ffff00009087a99c & 1) == 0) {
            FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
            DAT_ffff00009087a99c = 1;
          }
          iVar1 = FUN_ffff000090756b18(_DAT_ffff00009087bf88,7,1000);
          if (0 < iVar1) {
            lVar3 = FUN_ffff0000907168b0();
            if ((DAT_ffff00009087a99c & 1) == 0) {
              FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
              DAT_ffff00009087a99c = 1;
            }
            *(undefined4 *)(_DAT_ffff00009087bf70 + 0x1c) = 1;
            uVar4 = (uint)((ulong)(lVar3 - lVar2) >> 1);
            if (uVar4 != 0) {
              if (uVar4 == 0) {
                return 0;
              }
              return 100000000 / uVar4;
            }
            return 6000;
          }
        }
      }
    }
  }
  return 0;
}


