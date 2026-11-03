// ffff00009075e200  size=100  FUN_ffff00009075e200

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009075e200(void)

{
  int iVar1;
  long lVar2;
  
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  lVar2 = FUN_ffff00009075c088(_DAT_ffff00009087bf70);
  if ((lVar2 == 0) || (iVar1 = *(int *)(lVar2 + 0x6b4), iVar1 == 0)) {
    iVar1 = 0;
  }
  return iVar1;
}


